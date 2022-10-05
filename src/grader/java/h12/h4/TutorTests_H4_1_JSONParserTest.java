package h12.h4;

import h12.exceptions.JSONParseException;
import h12.json.JSONElement;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.parser.JSONParser;
import h12.json.parser.implementation.node.JSONElementNodeParser;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertThrows;

@TestForSubmission()
public class TutorTests_H4_1_JSONParserTest {

    @Test
    public void testParseSuccess() throws IOException {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "1")
            .subject("JSONParser#parse()")
            .build();

        JSONElement result = new JSONNumberNode(1);

        JSONElementNodeParser elementNodeParser = mock(JSONElementNodeParser.class);

        when(elementNodeParser.parse()).thenReturn(result);

        JSONParser parser = new JSONParser(elementNodeParser);
        JSONElement actual = parser.parse();

        verify(elementNodeParser, times(1)).parse();
        verify(elementNodeParser, times(1)).checkEndOfFile();

        assertEquals(result, actual, context, TR -> "The methode did not return the expected value");
    }

    @Test
    public void testParseIOException() throws IOException {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "Invalid File")
            .subject("JSONParser#parse()")
            .build();

        JSONElementNodeParser elementNodeParser = mock(JSONElementNodeParser.class);

        String exceptionMessage = "An IOException occurred";
        when(elementNodeParser.parse()).thenThrow(new IOException(exceptionMessage));

        JSONParser parser = new JSONParser(elementNodeParser);

        assertThrows(JSONParseException.class, parser::parse, context,
            TR -> "The method did not the throw the correct exception when the JSONElementNodeParser throws an IOException");

        try {
            parser.parse();
        } catch (JSONParseException exc) {
            assertEquals(new JSONParseException(exceptionMessage).getMessage(), exc.getMessage(), context,
                TR -> "The thrown exception does not contain the correct message");
        }
    }

}
