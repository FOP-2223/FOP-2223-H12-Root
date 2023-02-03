package h12.h1;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.Writer;
import java.nio.charset.Charset;

public class TutorFileWriter extends Writer {

    @FunctionalInterface
    public interface FunctionalWrite {
        void write(final char[] cbuf, final int off, final int len);
    }

    public static @Nullable FunctionalWrite FUNCTIONAL_WRITE;

    public static @Nullable String FILE_NAME;
    public static @Nullable File FILE;
    public static @Nullable FileDescriptor FD;
    public static @Nullable TutorFileWriter INSTANCE;
    public static boolean FLUSHED;
    public static boolean CLOSED;

    public static void reset0() {
        FILE_NAME = null;
        FILE = null;
        FD = null;
        INSTANCE = null;
        FLUSHED = false;
        CLOSED = false;
    }

    public TutorFileWriter(final String fileName) {
        FILE_NAME = fileName;
        INSTANCE = this;
    }

    public TutorFileWriter(final String fileName, final boolean append) {
        FILE_NAME = fileName;
        INSTANCE = this;
    }

    public TutorFileWriter(final File file) {
        FILE = file;
        INSTANCE = this;
    }

    public TutorFileWriter(final File file, final boolean append) {
        FILE = file;
        INSTANCE = this;
    }

    public TutorFileWriter(final FileDescriptor fd) {
        FD = fd;
        INSTANCE = this;
    }

    public TutorFileWriter(final String fileName, final Charset charset) {
        FILE_NAME = fileName;
        INSTANCE = this;
    }

    public TutorFileWriter(final String fileName, final Charset charset, final boolean append) {
        FILE_NAME = fileName;
        INSTANCE = this;
    }

    public TutorFileWriter(final File file, final Charset charset) {
        FILE = file;
        INSTANCE = this;
    }

    public TutorFileWriter(final File file, final Charset charset, final boolean append) {
        FILE = file;
        INSTANCE = this;
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) {
        if (FUNCTIONAL_WRITE != null) {
            FUNCTIONAL_WRITE.write(cbuf, off, len);
        }
    }

    @Override
    public void flush() {
        FLUSHED = true;
    }

    @Override
    public void close() {
        CLOSED = true;
    }
}
