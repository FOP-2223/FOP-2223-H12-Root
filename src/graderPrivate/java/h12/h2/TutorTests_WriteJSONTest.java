package h12.h2;

import h12.json.JSONElement;
import h12.json.JSONNumber;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.implementation.node.JSONStringNode;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.function.Consumer;

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

    public JSONNumberNode createMockedJSONNumber(Integer value) throws IOException {
        JSONNumberNode number = mock(JSONNumberNode.class);

        doAnswer(invocation -> {
            ((BufferedWriter) invocation.getArgument(0)).write(Integer.toString(value));
            return null;
        }).when(number).write(any(), anyInt());

        doReturn(value).when(number).getNumber();

        return number;
    }

    public JSONStringNode createMockedJSONString(String value) throws IOException {
        JSONStringNode string = mock(JSONStringNode.class);

        doAnswer(invocation -> {
            ((BufferedWriter) invocation.getArgument(0)).write("\"" + value + "\"");
            return null;
        }).when(string).write(any(), anyInt());

        doReturn(value).when(string).getString();

        return string;
    }

    public JSONObjectEntryNode createMockedJSONObjectEntry(String identifier, Integer value) throws IOException {
        JSONObjectEntryNode entry = mock(JSONObjectEntryNode.class);

        doAnswer(invocation -> {
            ((BufferedWriter) invocation.getArgument(0)).write("\"" + identifier + "\": " + value);
            return null;
        }).when(entry).write(any(), anyInt());

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
}
