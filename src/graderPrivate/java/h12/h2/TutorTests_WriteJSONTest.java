package h12.h2;

import h12.json.JSONElement;
import h12.json.JSONNumber;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.implementation.node.JSONStringNode;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static h12.json.implementation.node.JSONObjectNode.JSONObjectEntryNode;
import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

public class TutorTests_WriteJSONTest {

    public void testWriteJSONNode(JSONElement element, String expected, int indentation) throws Throwable {
        testWriteJSONNode(element, expected, indentation, null);
    }

    public void testWriteJSONNode(JSONElement element, String expected, int indentation, Consumer<Context> verifier) throws Throwable {
        Context context = contextBuilder()
            .add("input", expected)
            .add("indentation", indentation)
            .subject(element.getClass().getSimpleName() + "#write(BufferedWriter, int)")
            .build();

        String actual = getActual(element, indentation, context);

        String message = "Methode did not write the correct String to the BufferedWriter.";

        if (actual.replace(" ", "").replace("\n", "")
            .equals(expected.replace(" ", "").replace("\n", ""))) {
            message += " The contents of the expected and actual json element are equal but the whitespaces do not match.";
        }

        String finalMessage = message;
        assertEquals(createJSONString(expected), createJSONString(actual), context, TR -> finalMessage);

        if (verifier != null) verifier.accept(context);
    }

    public void testWriteJSONNodeNoIndent(JSONElement element, String expected, Consumer<Context> verifier) throws IOException {
        Context context = contextBuilder()
            .add("input", expected)
            .subject(element.getClass().getSimpleName() + "#write(BufferedWriter, int)")
            .build();

        String actual = removeWhiteSpace(getActual(element, 0, context));

        assertEquals(createJSONString(removeWhiteSpace(expected)), createJSONString(actual), context,
            TR -> "The method did not return the correct value");

        if (verifier != null) verifier.accept(context);
    }

    public String removeWhiteSpace(String string) {
        return string.chars().filter(i -> !Character.isWhitespace(i)).mapToObj(Character::toString).collect(Collectors.joining());
    }

    public String createJSONString(String json) {
        return "\\<span style=\"white-space: pre;\"\\>\n" + json + "\n\\</span\\>";
    }

    public String getActual(JSONElement element, int indentation, Context context) throws IOException {
        StringWriter writer = new StringWriter();
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            call(() -> element.write(bufferedWriter, indentation), context, TR -> "Unexpected exception was thrown");
        }

        return writer.getBuffer().toString();
    }

    public JSONNumberNode createMockedJSONNumber(Integer value, ArgumentCaptor<Integer> indentCaptor) throws IOException {
        JSONNumberNode number = spy(new JSONNumberNode(value));

        doAnswer(invocation -> {
            ((BufferedWriter) invocation.getArgument(0)).write(Integer.toString(value));
            return null;
        }).when(number).write(any(), indentCaptor == null ? anyInt() : indentCaptor.capture());

        doReturn(value).when(number).getNumber();

        return number;
    }

    public JSONStringNode createMockedJSONString(String value) throws IOException {
        JSONStringNode string = spy(new JSONStringNode(value));

        doAnswer(invocation -> {
            ((BufferedWriter) invocation.getArgument(0)).write("\"" + value + "\"");
            return null;
        }).when(string).write(any(), anyInt());

        doReturn(value).when(string).getString();

        return string;
    }

    public JSONObjectEntryNode createMockedJSONObjectEntry(String identifier, Integer value, ArgumentCaptor<Integer> indentCaptor) throws IOException {
        JSONObjectEntryNode entry = spy(new JSONObjectEntryNode(new JSONStringNode(identifier), new JSONNumberNode(value)));

        doAnswer(invocation -> {
            ((BufferedWriter) invocation.getArgument(0)).write("\"" + identifier + "\": " + value);
            return null;
        }).when(entry).write(any(), indentCaptor == null ? anyInt() : indentCaptor.capture());

        doReturn(identifier).when(entry).getIdentifier();
        doReturn(JSONNumber.of(value)).when(entry).getValue();

        return entry;
    }

    protected Consumer<Context> createVerifier(JSONElement jsonElement, int count) {
        return context -> {
            try {
                verify(jsonElement, times(count)).write(any(), anyInt());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (MockitoAssertionError e) {
                fail(context, TR -> "Expected the method write of the object " + jsonElement.toString() +
                    " to be called exactly " + count + " times but it wasn't.\n Original message: " + e.getMessage());
            }
        };
    }

    protected Consumer<Context> createIndentVerifier(int expected, ArgumentCaptor<Integer> indentCaptor, JSONElement element) {
        return context -> {
            assertEquals(expected, indentCaptor.getValue(), context,
                TR -> "The method write of the object " + element.toString() + " wasn't called with the correct indentation." +
                    " (Not tested by public grader)");
        };
    }
}
