package h12.h1;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.Writer;
import java.util.function.Function;

public class TutorBufferedWriter extends BufferedWriter {

    public static final Function<String, TutorBufferedWriter> FS_IO_TUTOR =
        resourceName -> new TutorBufferedWriter(new TutorFileWriter(resourceName));
    public static @Nullable Writer OUT;
    public static @Nullable TutorBufferedWriter INSTANCE;

    public static void reset0() {
        OUT = null;
        INSTANCE = null;
    }

    public TutorBufferedWriter(final Writer out) {
        super(out);
        OUT = out;
        INSTANCE = this;
    }

    public TutorBufferedWriter(final Writer out, final int sz) {
        super(out, sz);
        OUT = out;
        INSTANCE = this;
    }
}
