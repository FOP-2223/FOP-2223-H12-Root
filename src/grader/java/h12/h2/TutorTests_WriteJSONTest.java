package h12.h2;

import h12.json.JSONElement;
import h12.json.JSONNumber;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.implementation.node.JSONStringNode;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import static h12.json.implementation.node.JSONObjectNode.JSONObjectEntryNode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TutorTests_WriteJSONTest {

    public void testWriteJSONNode(JSONElement element, String expected, int indentation, ThrowingConsumer<JSONElement> verifier) throws Throwable {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", expected)
            .property("indentation", indentation)
            .subject(element.getClass().getSimpleName() + "#write(BufferedWriter, int)")
            .build();

        String actual = getActual(element, indentation);

        if (verifier != null) verifier.accept(element);

        Assertions2.assertEquals(expected, actual, context,
            TR -> "The methode did not write the correct String to the BufferedWriter");
    }

    public String getActual(JSONElement element, int indentation) throws IOException {
        StringWriter writer = new StringWriter();
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            element.write(bufferedWriter, indentation);
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

}
