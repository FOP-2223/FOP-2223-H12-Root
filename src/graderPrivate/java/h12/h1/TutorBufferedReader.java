package h12.h1;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.function.Function;

public class TutorBufferedReader extends BufferedReader {

    public static final Function<String, TutorBufferedReader> FS_IO_TUTOR =
        resourceName -> new TutorBufferedReader(new TutorFileReader(resourceName));
    public static @Nullable Reader IN;
    public static @Nullable TutorBufferedReader INSTANCE;

    public static void reset0() {
        IN = null;
        INSTANCE = null;
    }

    public TutorBufferedReader(final Reader in, final int sz) {
        super(in, sz);
        IN = in;
        INSTANCE = this;
    }

    public TutorBufferedReader(final Reader in) {
        super(in);
        IN = in;
        INSTANCE = this;
    }

}
