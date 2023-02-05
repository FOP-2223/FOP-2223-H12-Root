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
        return new LookaheadReader(new BufferedReader(new StringReader(input)));
    }

    public JSONElementNodeParser createJSONElementNodeParser(LookaheadReader reader) {
        return spy(new JSONElementNodeParser(reader));
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

    protected void mockNumberParser(JSONElementNodeParser elementNodeParser, Integer[] integers) {
        JSONNumberNodeParser numberParser = mock(JSONNumberNodeParser.class);
        try {
            when(numberParser.parse()).thenAnswer(invocation -> {
                Field readerField = JSONElementNodeParser.class.getDeclaredField("reader");
                readerField.setAccessible(true);
                LookaheadReader reader = (LookaheadReader) readerField.get(elementNodeParser);
                int read = reader.read();

                for (Integer integer : integers) {
                    if (Character.toString(read).equals(Integer.toString(integer))) return JSONNumber.of(integer);
                }

                throw new InvalidNumberException(Character.toString(read));
            });
        } catch (IOException exc) {
            throw new JSONParseException(exc.getMessage());
        }

        elementNodeParser.setNumberParser(numberParser);
    }

    @SuppressWarnings({"ignored", "ResultOfMethodCallIgnored"})
    protected void mockStringParser(JSONElementNodeParser elementNodeParser, String[] strings) {
        JSONStringNodeParser stringParser = mock(JSONStringNodeParser.class);
        try {
            when(stringParser.parse()).thenAnswer(invocation -> {
                Field readerField = JSONElementNodeParser.class.getDeclaredField("reader");
                readerField.setAccessible(true);
                LookaheadReader reader = (LookaheadReader) readerField.get(elementNodeParser);
                reader.read(); //quotation mark
                int read = reader.read();

                for (String string : strings) {
                    if (read == string.charAt(0)) {
                        reader.read(); //quotation mark
                        return JSONString.of(string);
                    }
                }

                throw new JSONParseException("invalid string");
            });
        } catch (IOException exc) {
            throw new JSONParseException(exc.getMessage());
        }

        elementNodeParser.setStringParser(stringParser);
    }

    @SuppressWarnings({"ignored", "ResultOfMethodCallIgnored"})
    protected void mockObjectEntryParser(JSONElementNodeParser elementNodeParser, String[] keys, Integer[] values) {
        JSONObjectEntryNodeParser objectEntryParser = mock(JSONObjectEntryNodeParser.class);
        try {
            when(objectEntryParser.parse()).thenAnswer(invocation -> {
                Field readerField = JSONElementNodeParser.class.getDeclaredField("reader");
                readerField.setAccessible(true);
                LookaheadReader reader = (LookaheadReader) readerField.get(elementNodeParser);
                reader.read(); //read quotation mark
                int read = reader.read();

                if (read == -1) {
                    throw new BadFileEndingException();
                }

                int i = 0;
                for (String key : keys) {
                    if (key.startsWith(Character.toString(read))) {
                        for (int j = 0; j < 4; j++) reader.read(); // read: ": 1
                        return JSONObject.JSONObjectEntry.of(key, JSONNumber.of(values[i]));
                    }
                    i++;
                }

                throw new JSONParseException("Unknown JSON object entry");
            });
        } catch (IOException exc) {
            throw new JSONParseException(exc.getMessage());
        }

        elementNodeParser.setObjectEntryParser(objectEntryParser);
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
