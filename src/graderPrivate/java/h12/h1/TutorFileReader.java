package h12.h1;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.Charset;
import java.util.function.Supplier;

public class TutorFileReader extends Reader {

    public static @Nullable Supplier<Reader> DELEGATE_SUPPLIER;

    public static @Nullable String FILE_NAME;
    public static @Nullable File FILE;
    public static @Nullable FileDescriptor FD;
    public static @Nullable TutorFileReader INSTANCE;
    public static boolean CLOSED;

    public static void reset0() {
        FILE_NAME = null;
        FILE = null;
        FD = null;
        INSTANCE = null;
        CLOSED = false;
    }

//    public static void createFakeTable() throws IOException {
//        reset0();
//        TutorStudentExamTableIO.reset();
//        final StringWriter stringWriter = new StringWriter();
//        final TableWithTitle table = SolutionTableGenerator.createTable(20, 20);
//        SolutionStudentExamTableIO.writeStudentExamTable(stringWriter, table.getEntries(), table.getTitle());
//        final String tableString = stringWriter.toString();
//        DELEGATE_SUPPLIER = () -> new StringReader(tableString);
//        TutorStudentExamTableIO.EXECUTION_TYPE = TutorStudentExamTableIO.ExecutionType.USE_SOLUTION_NO_INFORM;
//    }

    private final @Nullable Reader delegate;

    private TutorFileReader() {
        delegate = DELEGATE_SUPPLIER == null ? null : DELEGATE_SUPPLIER.get();
    }

    public TutorFileReader(final String fileName) {
        this();
        FILE_NAME = fileName;
        INSTANCE = this;
    }

    public TutorFileReader(final File file) {
        this();
        FILE = file;
        INSTANCE = this;
    }

    public TutorFileReader(final FileDescriptor fd) {
        this();
        FD = fd;
        INSTANCE = this;
    }

    public TutorFileReader(final String fileName, final Charset charset) {
        this();
        FILE_NAME = fileName;
        INSTANCE = this;
    }

    public TutorFileReader(final File file, final Charset charset) {
        this();
        FILE = file;
        INSTANCE = this;
    }

    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException {
        if (delegate != null) {
            return delegate.read(cbuf, off, len);
        }
        return -1;
    }

    @Override
    public void close() throws IOException {
        if (delegate != null) {
            delegate.close();
        }
        CLOSED = true;
    }

}
