package h12.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Reads a character stream from a {@link Reader}.
 *
 * <p> The method peek() allows the user the retrieve the next incoming char without skipping it.
 * Given the sequence {@code "abc"} the {@link LookaheadReader} would behave as followed:
 *
 * <pre>
 *     reader.peek(); // 'a'
 *     reader.read(); // 'a'
 *     reader.read(); // 'b'
 *     reader.peek(); // 'c'
 *     reader.peek(); // 'c'
 *     reader.read(); // 'c'
 *     reader.peek(); // -1
 *     reader.read(); // -1
 * </pre>
 */
public class LookaheadReader implements AutoCloseable {

    private final BufferedReader reader;

    private int lookahead;

    /**
     * Creates a new {@link LookaheadReader}-Instance based on the given reader.
     *
     * @param reader The Reader the constructed lookahead reader is based on.
     * @throws IOException If reading from the underlying reader causes an {@link IOException}.
     */
    public LookaheadReader(BufferedReader reader) throws IOException {
        this.reader = reader;
        lookahead = reader.read();
    }

    /**
     * Reads a single character.
     *
     * @return The character read or -1 if the end of the reader is reached.
     * @throws IOException If reading from the underlying reader causes an {@link IOException}.
     */
    public int read() throws IOException {
        int tmp = lookahead;
        lookahead = reader.read();
        return tmp;
    }

    /**
     * Retrieves the next character without skipping that character.
     *
     * @return The next character or -1 if the end of the reader is reached.
     * @throws IOException If reading from the underlying Reader causes an {@link IOException}.
     */
    public int peek() throws IOException {
        return lookahead;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }
}
