package h12.h4;

import h12.exceptions.JSONParseException;
import h12.exceptions.JSONWriteException;
import h12.ioFactory.IOFactory;
import h12.json.JSON;
import h12.json.JSONElement;
import h12.json.LookaheadReader;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.parser.JSONParser;
import h12.json.parser.JSONParserFactory;
import h12.json.parser.implementation.node.JSONNodeParserFactory;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import java.io.*;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission()
public class TutorTests_H4_2_JSONTest {

    @Test
    public void testParseSuccess() throws NoSuchFieldException, IllegalAccessException {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "1")
            .subject("JSON#parse()")
            .build();

        JSON json = new JSON();

        StringIOFactory ioFactory = new StringIOFactory();
        json.setIOFactory(ioFactory);

        JSONParser parser = mock(JSONParser.class);
        JSONElement result = new JSONNumberNode(1);
        when(parser.parse()).thenReturn(result);

        JSONParserFactory parserFactory = mock(JSONNodeParserFactory.class);
        json.setParserFactory(parserFactory);

        ArgumentCaptor<LookaheadReader> argumentCaptor = ArgumentCaptor.forClass(LookaheadReader.class);
        when(parserFactory.createParser(argumentCaptor.capture())).thenReturn(parser);

        JSONElement actual = json.parse("test.json");

        LookaheadReader actualReader = argumentCaptor.getValue();
        Field readerField = LookaheadReader.class.getDeclaredField("reader");
        readerField.setAccessible(true);
        BufferedReader reader = (BufferedReader) readerField.get(actualReader);

        assertEquals(ioFactory.getLastCreatedReader(), reader, context,
            TR -> "The parserFactory wasn't invoked with a Lookahead based on a reader created by the ioFactory");

        assertTrue(actualReader.isClosed(), context,
            TR -> "The created LookaheadReader hasn't been closed. Make sure you have used a try-with-resource block");

        assertEquals(result, actual, context, TR -> "The method did not return the correct value");
    }

    @Test
    public void testParseExceptionUnsupportedReading() {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("ioFactory", "Does not support reading")
            .subject("JSON#parse()")
            .build();

        JSON json = new JSON();
        json.setIOFactory(new UnsupportedIOFactory());

        assertThrows(JSONParseException.class, () -> json.parse("test.json"), context,
            TR -> "The method did not throw the correct exception when given an IOFactory that does not support reading");

        try {
            json.parse("test.json");
        } catch (JSONParseException exc) {
            assertEquals("An exception occurred while trying to parse a JSON file. The current ioFactory does not support reading!",
                exc.getMessage(), context, TR -> "The thrown exception does not contain the correct message");
        }
    }

    @Test
    public void testParseIOException() {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "invalid File")
            .subject("JSON#parse()")
            .build();
        String exceptionMessage = "An IOException occurred";

        JSON json = new JSON();

        IOFactory ioFactory = new ThrowingIOFactory(exceptionMessage);
        json.setIOFactory(ioFactory);

        assertThrows(JSONParseException.class, () -> json.parse("test.json"), context,
            TR -> "The method did not throw the correct exception when an IOException is thrown");

        try {
            json.parse("test.json");
        } catch (JSONParseException exc) {
            assertEquals("An exception occurred while trying to parse a JSON file. %s".formatted(exceptionMessage),
                exc.getMessage(), context, TR -> "The thrown exception does not contain the correct message");
        }
    }

    @Test
    public void testWriteSuccess() throws IOException {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "1")
            .subject("JSON#write(String, JSONElement)")
            .build();

        JSON json = new JSON();
        JSONElement input = mock(JSONNumberNode.class);

        StringIOFactory ioFactory = new StringIOFactory();
        json.setIOFactory(ioFactory);

        ArgumentCaptor<BufferedWriter> argumentCaptorWriter = ArgumentCaptor.forClass(BufferedWriter.class);
        ArgumentCaptor<Integer> argumentCaptorIndentation = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(input).write(argumentCaptorWriter.capture(), argumentCaptorIndentation.capture());

        json.write("test.json", input);

        BufferedWriter actualWriter = argumentCaptorWriter.getValue();
        Integer actualIndentation = argumentCaptorIndentation.getValue();

        assertEquals(ioFactory.getLastCreatedWriter(), actualWriter, context,
            TR -> "The method write(BufferedWriter, int) wasn't invoked with a BufferedWriter created by the ioFactory");

        assertEquals(0, actualIndentation, context,
            TR -> "The method write(BufferedWriter, int) wasn't invoked with the correct indentation");

        boolean isClosed;
        try {
            actualWriter.flush();
            isClosed = false;
        } catch (IOException exc) {
            isClosed = true;
        }

        assertTrue(isClosed, context, TR -> "The created LookaheadReader hasn't been closed. Make sure you have used a try-with-resource block");
    }

    @Test
    public void testWriteExceptionUnsupportedWriting() {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("ioFactory", "Does not support writing")
            .subject("JSON#write(String, JSONElement)")
            .build();

        JSON json = new JSON();
        JSONElement input = new JSONNumberNode(1);

        json.setIOFactory(new UnsupportedIOFactory());

        assertThrows(JSONWriteException.class, () -> json.write("test.json", input), context,
            TR -> "The method did not throw the correct exception when given an IOFactory that does not support writing");

        try {
            json.write("test.json", input);
        } catch (JSONWriteException exc) {
            assertEquals("An exception occurred while trying to write to a JSON file. The current ioFactory does not support writing!",
                exc.getMessage(), context, TR -> "The thrown exception does not contain the correct message");
        }
    }

    @Test
    public void testWriteIOException() {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "invalid File")
            .subject("JSON#write(String, JSONElement)")
            .build();
        String exceptionMessage = "An IOException occurred";

        JSON json = new JSON();
        JSONElement input = new JSONNumberNode(1);

        IOFactory ioFactory = new ThrowingIOFactory(exceptionMessage);
        json.setIOFactory(ioFactory);

        assertThrows(JSONWriteException.class, () -> json.write("test.json", input), context,
            TR -> "The method did not throw the correct exception when an IOException is thrown");

        try {
            json.write("test.json", input);
        } catch (JSONWriteException exc) {
            assertEquals("An exception occurred while trying to write to a JSON file. %s".formatted(exceptionMessage),
                exc.getMessage(), context, TR -> "The thrown exception does not contain the correct message");
        }
    }

    private static class StringIOFactory implements IOFactory {

        BufferedReader reader;
        BufferedWriter writer;

        @Override
        public BufferedReader createReader(String input) throws UnsupportedOperationException {
            return reader = new BufferedReader(new StringReader(input));
        }

        @Override
        public BufferedWriter createWriter(String ignored) throws UnsupportedOperationException {
            return writer = new BufferedWriter(new StringWriter());
        }

        @Override
        public boolean supportsReader() {
            return true;
        }

        @Override
        public boolean supportsWriter() {
            return true;
        }

        public BufferedWriter getLastCreatedWriter() {
            return writer;
        }

        public BufferedReader getLastCreatedReader() {
            return reader;
        }
    }

    private static class UnsupportedIOFactory implements IOFactory {
        @Override
        public BufferedReader createReader(String resourceName) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("%s does not support reading!".formatted(getClass().getSimpleName()));
        }

        @Override
        public BufferedWriter createWriter(String resourceName) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("%s does not support writing!".formatted(getClass().getSimpleName()));
        }

        @Override
        public boolean supportsReader() {
            return false;
        }

        @Override
        public boolean supportsWriter() {
            return false;
        }
    }

    private static class ThrowingIOFactory implements IOFactory {

        private final String exceptionMessage;

        public ThrowingIOFactory(String exceptionMessage) {
            this.exceptionMessage = exceptionMessage;
        }

        @Override
        public BufferedReader createReader(String resourceName) throws UnsupportedOperationException, IOException {
            throw new IOException(exceptionMessage);
        }

        @Override
        public BufferedWriter createWriter(String resourceName) throws UnsupportedOperationException, IOException {
            throw new IOException(exceptionMessage);
        }

        @Override
        public boolean supportsReader() {
            return true;
        }

        @Override
        public boolean supportsWriter() {
            return true;
        }
    }
}
