package h12.h3;

import h12.exceptions.BadFileEndingException;
import h12.exceptions.InvalidNumberException;
import h12.exceptions.JSONParseException;
import h12.json.*;
import h12.json.parser.implementation.node.*;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

public class TutorTests_JSONParseTest {

    public LookaheadReader createLookaheadReader(String input) throws IOException {
        return spy(new TutorLookaheadReader(new BufferedReader(new StringReader(input))));
    }

    public JSONElementNodeParser createJSONElementNodeParser(LookaheadReader reader) {
        return spy(new TutorJSONElementNodeParser(reader));
    }

    public String getContent(LookaheadReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();

        while (reader.peek() != -1) {
            builder.append((char) reader.read());
        }

        return builder.toString();
    }

    public <T> void testParseSuccess(Function<JSONElementNodeParser, JSONNodeParser> parserCreator, String input, T expected,
                                     String expectedContent, Function<JSONElement, T> actualFunction) throws Throwable {
        testParseSuccess(parserCreator, input, expected, expectedContent, actualFunction, null, null);
    }

    public <T> void testParseSuccess(Function<JSONElementNodeParser, JSONNodeParser> parserCreator,
                                     String input,
                                     T expected,
                                     String expectedContent,
                                     Function<JSONElement, T> actualFunction,
                                     Consumer<JSONElementNodeParser> mocker,
                                     BiConsumer<JSONElementNodeParser, Context> verifier) throws Throwable {

        LookaheadReader reader = createLookaheadReader(input);
        JSONElementNodeParser elementParser = createJSONElementNodeParser(reader);
        JSONNodeParser parser = parserCreator.apply(elementParser);
        Context context = contextBuilder()
            .add("input", input)
            .subject(parser.getClass().getSimpleName() + "#parse()")
            .build();

        if (mocker != null) mocker.accept(elementParser);

        JSONElement actual = callObject(parser::parse, context, TR -> "Unexpected exception was thrown");

        assertNotNull(actual, context, TR -> "Method returned null");

        assertEquals(expected, actualFunction.apply(actual), context,
            TR -> "The returned JSONElement does not contain the expected value");

        assertEquals(expectedContent, getContent(reader), context,
            TR -> "Method did not read the correct amount of character");

        if (verifier != null) verifier.accept(elementParser, context);
    }

    public void testParseException(Class<? extends Exception> expected, Function<JSONElementNodeParser, JSONNodeParser> parserCreator,
                                   String input) throws IOException {
        testParseException(expected, parserCreator, input, null);
    }

    public void testParseException(Class<? extends Exception> expected, Function<JSONElementNodeParser, JSONNodeParser> parserCreator,
                                   String input, Consumer<JSONElementNodeParser> mocker) throws IOException {
        LookaheadReader reader = createLookaheadReader(input);
        JSONElementNodeParser elementParser = createJSONElementNodeParser(reader);
        JSONNodeParser parser = parserCreator.apply(elementParser);
        Context context = contextBuilder()
            .add("input", input)
            .subject(parser.getClass().getSimpleName())
            .build();

        if (mocker != null) mocker.accept(elementParser);

        assertThrows(expected, parser::parse, context,
            TR -> "Method parse() did not throw the correct exception when given an invalid input");
    }

    public void testParseExceptionWithMessage(Class<? extends Exception> expected, Function<JSONElementNodeParser, JSONNodeParser> parserCreator,
                                   String input, Consumer<JSONElementNodeParser> mocker, String... expectedMessages) throws IOException {
        testParseException(expected, parserCreator, input, mocker);

        LookaheadReader reader = createLookaheadReader(input);
        JSONElementNodeParser elementParser = createJSONElementNodeParser(reader);
        JSONNodeParser parser = parserCreator.apply(elementParser);

        if (mocker != null) mocker.accept(elementParser);

        Context context = contextBuilder()
            .add("input", input)
            .subject(parser.getClass().getSimpleName())
            .build();

        try {
            parser.parse();
        } catch (JSONParseException e) {
            for (String expectedMessage : expectedMessages) {
                if (expectedMessage.equals(e.getMessage())) {
                    return;
                }
            }
            assertEquals(String.join(" \\<b\\>or\\</b\\> ", expectedMessages), e.getMessage(), context,
                TR -> "The message of the thrown exception is not correct");
        }
    }

    protected void mockNumberParser(JSONElementNodeParser elementNodeParser) {
        JSONNumberNodeParser numberParser = spy(new TutorNumberParser(elementNodeParser));
        elementNodeParser.setNumberParser(numberParser);
    }

    protected void mockStringParser(JSONElementNodeParser elementNodeParser) {
        JSONStringNodeParser stringParser = spy(new JSONStringNodeParser(elementNodeParser));
        elementNodeParser.setStringParser(stringParser);
    }

    protected void mockObjectEntryParser(JSONElementNodeParser elementNodeParser) {
        JSONObjectEntryNodeParser objectEntryParser = spy(new TutorObjectEntryParser(elementNodeParser));
        elementNodeParser.setObjectEntryParser(objectEntryParser);
        mockNumberParser(elementNodeParser);
    }

    protected BiConsumer<JSONElementNodeParser, Context> createElementParserVerifier(int count) {
        return (jsonElementNodeParser, context) -> {
            try {
                verify(jsonElementNodeParser, times(count)).parse();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (MockitoAssertionError e) {
                fail(context, TR -> "Expected the method parse of class JSONElementNodeParser to be called exactly " + count + "times but it wasn't"
                    + "\n Original message: " + e.getMessage());
            }
        };
    }

    protected BiConsumer<JSONElementNodeParser, Context> createNodeParserVerifier(
        int count, Function<JSONElementNodeParser,
        JSONNodeParser> parserFunction,
        String parser) {
        return (jsonElementNodeParser, context) -> {
            try {
                verify(parserFunction.apply(jsonElementNodeParser), times(count)).parse();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (MockitoAssertionError e) {
                fail(context, TR -> "Expected the method parse of class " + parser + " to be called exactly " + count + "times but it wasn't"
                    + "\n Original message: " + e.getMessage());
            }
        };
    }
}
