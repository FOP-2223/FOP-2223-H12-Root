package h12.h4;

import h12.exceptions.JSONParseException;
import h12.json.JSONElement;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.parser.JSONParser;
import h12.json.parser.implementation.node.JSONElementNodeParser;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class TutorTests_H4_1_JSONParserTest {

    @Test
    public void testParseSuccess() throws IOException {
        Context context = contextBuilder()
            .add("input", "1")
            .subject("JSONParser#parse()")
            .build();

        JSONElement result = new JSONNumberNode(1);

        JSONElementNodeParser elementNodeParser = mock(JSONElementNodeParser.class);
        InOrder inOrder = inOrder(elementNodeParser);

        when(elementNodeParser.parse()).thenReturn(result);

        JSONParser parser = new JSONParser(elementNodeParser);

        JSONElement actual = callObject(parser::parse, context, TR -> "Unexpected exception was thrown");

        try {
            verify(elementNodeParser, times(1)).parse();
        } catch (MockitoAssertionError e) {
            fail(context, TR -> "Expected method parse of class elementNodeParser to be called exactly once and before checkEndOfFile but it wasn't"
                + "\n Original message: " + e.getMessage());
        }

        try {
            verify(elementNodeParser, times(1)).checkEndOfFile();
        } catch (MockitoAssertionError e) {
            fail(context, TR -> "Expected method checkEndOfFile of class elementNodeParser to be called exactly once and after parse but it wasn't"
                + "\n Original message: " + e.getMessage());
        }

        inOrder.verify(elementNodeParser, times(1)).parse();
        inOrder.verify(elementNodeParser, times(1)).checkEndOfFile();

        assertEquals(result, actual, context, TR -> "Method did not return the expected value");
    }

    @Test
    public void testParseIOException() throws IOException {
        Context context = contextBuilder()
            .add("input", "Invalid File")
            .subject("JSONParser#parse()")
            .build();

        JSONElementNodeParser elementNodeParser = mock(JSONElementNodeParser.class);

        String exceptionMessage = "An IOException occurred";
        when(elementNodeParser.parse()).thenThrow(new IOException(exceptionMessage));

        JSONParser parser = new JSONParser(elementNodeParser);

        assertThrows(JSONParseException.class, parser::parse, context,
            TR -> "Method did not the throw the correct exception when the JSONElementNodeParser throws an IOException");

        try {
            parser.parse();
        } catch (JSONParseException exc) {
            assertEquals(new JSONParseException(exceptionMessage).getMessage(), exc.getMessage(), context,
                TR -> "The thrown exception does not contain the correct message");
        }
    }

}
