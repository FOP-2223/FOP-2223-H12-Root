package h12.h3;

import h12.exceptions.BadFileEndingException;
import h12.exceptions.UnexpectedCharacterException;
import h12.json.JSONConstants;
import h12.json.JSONElement;
import h12.json.JSONObject;
import h12.json.LookaheadReader;
import h12.json.implementation.node.*;
import h12.json.parser.implementation.node.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import java.io.IOException;
import java.util.List;

import static h12.json.JSONObject.JSONObjectEntry;
import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertThrows;

@TestForSubmission()
public class TutorTests_H3_1_H3_2_JSONElementNodeParserTest extends TutorTests_JSONParseTest {

    @ParameterizedTest
    @CsvSource("abc")
    public void testSkipIndentation(String input) throws IOException {
        String originalInput = input;
        input = "  \n \r\t   " + input + " "; //@CSVSource trims trailing whitespace
        String expectedContent = originalInput + " ";

        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", input)
            .subject("JSONElementNodeParser#skipIndentation()")
            .build();

        try (LookaheadReader reader = createLookaheadReader(input)) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);
            elementParser.skipIndentation();
            String actualContent = getContent(reader);

            assertEquals(expectedContent, actualContent, context,
                TR -> "The lookaheadReader does not contain the expected content after the methode has been called");
        }
    }

    @Test
    public void testSkipIndentationEmpty() throws IOException {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "empty")
            .subject("JSONElementNodeParser#skipIndentation()")
            .build();

        try (LookaheadReader reader = createLookaheadReader("")) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);
            elementParser.skipIndentation();
            String actualContent = getContent(reader);

            assertEquals("", actualContent, context,
                TR -> "The lookaheadReader does not contain the expected content after the methode has been called");
        }
    }

    @ParameterizedTest
    @CsvSource("abc")
    public void testAcceptIt(String input) throws IOException {
        String originalInput = input;
        input = "  \n" + input;

        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", input)
            .subject("JSONElementNodeParser#acceptIt()")
            .build();

        try (LookaheadReader reader = createLookaheadReader(input)) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);

            assertEquals(originalInput.charAt(0), (char) elementParser.acceptIt(), context,
                TR -> "The method did not return the correct value");

            String actualContent = getContent(reader);

            assertEquals(originalInput.substring(1), actualContent, context,
                TR -> "The lookaheadReader does not contain the expected content after the methode has been called");
        }
    }

    @Test
    public void testAcceptItEmpty() throws IOException {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "empty")
            .subject("JSONElementNodeParser#acceptIt()")
            .build();

        try (LookaheadReader reader = createLookaheadReader("")) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);

            assertEquals(-1, elementParser.acceptIt(), context,
                TR -> "The method did not return the correct value");
        }
    }

    @ParameterizedTest
    @CsvSource("abc")
    public void testAcceptSuccess(String input) throws IOException {
        String originalInput = input;
        input = "  \n" + input;

        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", input)
            .property("expected", originalInput.charAt(0))
            .subject("JSONElementNodeParser#accept(char)")
            .build();

        try (LookaheadReader reader = createLookaheadReader(input)) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);
            elementParser.accept(originalInput.charAt(0));
            String actualContent = getContent(reader);

            assertEquals(originalInput.substring(1), actualContent, context,
                TR -> "The lookaheadReader does not contain the expected content after the methode has been called");
        }
    }

    @ParameterizedTest
    @CsvSource("abc")
    public void testAcceptException(String input) throws IOException {
        String originalInput = input;
        input = "  \n" + (char) (input.charAt(0) + 1) + input.substring(1);

        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", input)
            .property("expected", originalInput.charAt(0))
            .subject("JSONElementNodeParser#accept(char)")
            .build();

        try (LookaheadReader reader = createLookaheadReader(input)) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);

            assertThrows(UnexpectedCharacterException.class, () -> elementParser.accept(originalInput.charAt(0)), context,
                TR -> "The methode did not throw an UnexpectedCharacterException when the next char in the reader does not match the parameter");
        }

        try (LookaheadReader reader = createLookaheadReader(input)) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);

            elementParser.accept(originalInput.charAt(0));
        } catch (UnexpectedCharacterException exc) {
            assertEquals(new UnexpectedCharacterException(originalInput.charAt(0), (char) (originalInput.charAt(0) + 1)).getMessage(), exc.getMessage(), context,
                TR -> "The thrown exception does not contain the correct message");
        }
    }

    @ParameterizedTest
    @CsvSource("abc")
    public void testPeek(String input) throws IOException {
        String originalInput = input;
        input = "  \n" + input;

        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", input)
            .subject("JSONElementNodeParser#peek()")
            .build();

        try (LookaheadReader reader = createLookaheadReader(input)) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);

            assertEquals(originalInput.charAt(0), (char) elementParser.peek(), context,
                TR -> "The method did not return the correct value");

            String actualContent = getContent(reader);

            assertEquals(originalInput, actualContent, context,
                TR -> "The lookaheadReader does not contain the expected content after the methode has been called");
        }
    }

    @Test
    public void testPeekEmpty() throws IOException {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "empty")
            .subject("JSONElementNodeParser#peek()")
            .build();

        try (LookaheadReader reader = createLookaheadReader("")) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);

            assertEquals(-1, elementParser.peek(), context,
                TR -> "The method did not return the correct value");
        }
    }

    @Test
    public void testCheckEndOfFileSuccess() throws IOException {
        String input = "  ";
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "\"  \"")
            .subject("JSONElementNodeParser#checkEndOfFile(char)")
            .build();

        try (LookaheadReader reader = createLookaheadReader(input)) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);
            elementParser.checkEndOfFile();
            String actualContent = getContent(reader);

            assertEquals("", actualContent, context,
                TR -> "The lookaheadReader does not contain the expected content after the methode has been called");
        }
    }

    @Test
    public void testCheckEndOfFileException() throws IOException {
        String input = "  \na";

        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", input)
            .subject("JSONElementNodeParser#checkEndOfFile(char)")
            .build();

        try (LookaheadReader reader = createLookaheadReader(input)) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);

            assertThrows(BadFileEndingException.class, elementParser::checkEndOfFile, context,
                TR -> "The methode did not throw an BadFileEndingException when the end of the file has not been reached");
        }
    }

    @ParameterizedTest
    @CsvSource("abc")
    public void testReadUntil(String input) throws IOException {
        input = " " + input + " ";
        String expected = input;
        char terminator = 'T';
        String end = "xyz";
        input = input + terminator + end;

        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", input)
            .subject("JSONElementNodeParser#readUntil(Predicate)")
            .build();

        try (LookaheadReader reader = createLookaheadReader(input)) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);

            assertEquals(expected, elementParser.readUntil(integer -> integer == terminator), context,
                TR -> "The method did not return the correct value");

            String actualContent = getContent(reader);

            assertEquals(terminator + end, actualContent, context,
                TR -> "The lookaheadReader does not contain the expected content after the methode has been called");
        }
    }

    @Test
    public void testReadUntilException() throws IOException {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "empty")
            .subject("JSONElementNodeParser#readUntil(Predicate)")
            .build();

        try (LookaheadReader reader = createLookaheadReader("")) {
            JSONElementNodeParser elementParser = new JSONElementNodeParser(reader);

            assertThrows(BadFileEndingException.class, () -> elementParser.readUntil(integer -> integer == 'T'), context,
                TR -> "The method did not return the correct value");
        }
    }

    public void testParse(String input, ThrowingConsumer<JSONElementNodeParser> verifier, ThrowingConsumer<JSONElementNodeParser> overWriter, JSONElement expected) throws Throwable {
        String originalInput = input;
        input = "  \n" + input;
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", input)
            .subject("JSONElementNodeParser#parse()")
            .build();

        try (LookaheadReader reader = createLookaheadReader(input)) {
            JSONElementNodeParser elementParser = createMockedJSONElementNodeParser(reader);
            overWriter.accept(elementParser);

            JSONElement actual = elementParser.parse();

            verifier.accept(elementParser);

            String content = getContent(reader);
            assertEquals(originalInput, content, context,
                TR -> "The lookaheadReader does not contain the expected content after the methode has been called");

            assertEquals(expected, actual, context,
                TR -> "The methode did not return the correct value");
        }
    }

    @ParameterizedTest
    @CsvSource("a, 1")
    public void testParseObject(String key, Integer value) throws Throwable {
        JSONObjectNode expected = JSONObject.of(JSONObjectEntry.of(key, new JSONNumberNode(value)));

        testParse("{\"%s\", %d}".formatted(key, value),
            elementParser -> verify(elementParser.getObjectParser(), times(1)).parse(),
            elementNodeParser -> when(elementNodeParser.getObjectParser().parse()).thenReturn(expected),
            expected);
    }


    @ParameterizedTest
    @CsvSource("1")
    public void testParseArray(Integer value) throws Throwable {
        JSONArrayNode expected = new JSONArrayNode(List.of(new JSONNumberNode(value)));

        testParse("[%d]".formatted(value),
            elementParser -> verify(elementParser.getArrayParser(), times(1)).parse(),
            elementNodeParser -> when(elementNodeParser.getArrayParser().parse()).thenReturn(expected),
            expected);
    }

    @ParameterizedTest
    @CsvSource("1")
    public void testParseNumber(Integer value) throws Throwable {
        JSONNumberNode expected = new JSONNumberNode(value);

        testParse("%d".formatted(value),
            elementParser -> verify(elementParser.getNumberParser(), times(1)).parse(),
            elementNodeParser -> when(elementNodeParser.getNumberParser().parse()).thenReturn(expected),
            expected);

        testParse("+%d".formatted(value),
            elementParser -> verify(elementParser.getNumberParser(), times(1)).parse(),
            elementNodeParser -> when(elementNodeParser.getNumberParser().parse()).thenReturn(expected),
            expected);

        JSONNumberNode expectedDecimal = new JSONNumberNode((int) Math.log(value) + 1);

        testParse(".%d".formatted(value),
            elementParser -> verify(elementParser.getNumberParser(), times(1)).parse(),
            elementNodeParser -> when(elementNodeParser.getNumberParser().parse()).thenReturn(expectedDecimal),
            expectedDecimal);

        JSONNumberNode expectedNegative = new JSONNumberNode(-value);

        testParse("-%d".formatted(value),
            elementParser -> verify(elementParser.getNumberParser(), times(1)).parse(),
            elementNodeParser -> when(elementNodeParser.getNumberParser().parse()).thenReturn(expectedNegative),
            expectedNegative);
    }

    @ParameterizedTest
    @CsvSource("a")
    public void testParseString(String input) throws Throwable {
        JSONStringNode expected = new JSONStringNode(input);

        testParse("\"" + input + "\"",
            elementParser -> verify(elementParser.getStringParser(), times(1)).parse(),
            elementNodeParser -> when(elementNodeParser.getStringParser().parse()).thenReturn(expected),
            expected);
    }

    @ParameterizedTest
    @CsvSource("TRUE")
    public void testParseConstant(String constant) throws Throwable {
        JSONConstantNode expected = new JSONConstantNode(JSONConstants.valueOf(constant));

        testParse(expected.getConstant().getSpelling(),
            elementParser -> verify(elementParser.getConstantParser(), times(1)).parse(),
            elementNodeParser -> when(elementNodeParser.getConstantParser().parse()).thenReturn(expected),
            expected);
    }

    @Test
    public void testParseEmpty() throws Throwable {
        testParse("", elementParser -> {
        }, elementNodeParser -> {
        }, null);
    }

    private JSONElementNodeParser createMockedJSONElementNodeParser(LookaheadReader reader) {
        JSONElementNodeParser elementParser = spy(new JSONElementNodeParser(reader));

        elementParser.setObjectParser(mock(JSONObjectNodeParser.class));
        elementParser.setArrayParser(mock(JSONArrayNodeParser.class));
        elementParser.setStringParser(mock(JSONStringNodeParser.class));
        elementParser.setNumberParser(mock(JSONNumberNodeParser.class));
        elementParser.setConstantParser(mock(JSONConstantNodeParser.class));

        return elementParser;
    }

}
