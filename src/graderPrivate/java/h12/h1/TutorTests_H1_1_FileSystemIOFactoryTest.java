package h12.h1;

import h12.ioFactory.FileSystemIOFactory;
import h12.ioFactory.ResourceIOFactory;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import java.io.*;
import java.util.Objects;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotNull;

@TestForSubmission()
public class TutorTests_H1_1_FileSystemIOFactoryTest {

    @Test
    public void testCreateBufferedReader() throws IOException {
        Context context = new BasicContext.Builder.Factory().builder().property("file content", "\"Hello World!\"")
            .subject("FileSystemIOFactory#createReader(String)").build();

        String resourceName = "h12/h1/CreateReaderTest.txt";
        File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(resourceName)).getFile());

        try (BufferedReader reader = new FileSystemIOFactory().createReader(file.getAbsolutePath())) {
            assertNotNull(reader, context, TR -> "The methode returned null");

            assertEquals("Hello World!", reader.readLine(), context, TR ->
                "The BufferedReader contain the correct content");

            assertEquals(-1, reader.read(), context, TR ->
                "The BufferedReader did contain the correct content");
        }
    }

    @Test
    public void testCreateBufferedWriter() throws IOException {
        Context context = new BasicContext.Builder.Factory().builder().property("file content", "\"Hello World!\"")
            .subject("FileSystemIOFactory#createReader(String)").build();

        ClassLoader classLoader = getClass().getClassLoader();
        String resourceName = "h12/h1/CreateWriterTest.txt";
        File file = new File(Objects.requireNonNull(classLoader.getResource(resourceName)).getFile());

        //delete contents of File
        new PrintWriter(file.getAbsolutePath()).close();

        try (BufferedWriter writer = new FileSystemIOFactory().createWriter(file.getAbsolutePath())) {
            assertNotNull(writer, context, TR -> "The methode returned null");
            writer.write("Hello World!");
        }

        try (BufferedReader reader = new ResourceIOFactory(classLoader).createReader(resourceName)) {
            assertEquals("Hello World!", reader.readLine(), context, TR ->
                "The BufferedWriter did not write the content to the correct file");
        }
    }

}
