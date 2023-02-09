package h12.h3;

import h12.json.LookaheadReader;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class TutorLookaheadReader extends LookaheadReader {

    private final BufferedReader reader;
    private boolean isClosed = false;
    private int lookahead;

    /**
     * Creates a new {@link LookaheadReader}-Instance based on the given reader.
     *
     * @param reader The Reader the constructed lookahead reader is based on.
     * @throws IOException If reading from the underlying reader causes an {@link IOException}.
     */
    public TutorLookaheadReader(BufferedReader reader) throws IOException {
        super(new BufferedReader(new StringReader("")));
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
     *
     * @param characterBuffer Destination buffer
     * @param off             Offset at which to start storing characters
     * @param len             Maximum number of characters to read
     * @return The number of characters read, or -1 if the end of the
     * stream has been reached
     * @throws IndexOutOfBoundsException If {@code off} is negative, or {@code len} is negative,
     *                                   or {@code len} is greater than {@code characterBuffer.length - off}
     * @throws IOException               If reading from the underlying Reader causes an {@link IOException}.
     */
    @Override
    public int read(@NotNull char[] characterBuffer, int off, int len) throws IOException, IndexOutOfBoundsException {
        if (off < 0 || len < 0 || len > characterBuffer.length - off) {
            throw new IndexOutOfBoundsException();
        }

        if (peek() == -1) return -1;

        int i;
        for (i = 0; i < len; i++) {
            if (peek() == -1) {
                break;
            }
            characterBuffer[i + off] = (char) read();
        }

        return i;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        reader.close();
        isClosed = true;
    }

    /**
     * Returns whether this {@link LookaheadReader} has been closed.
     *
     * @return {@code true}, if this {@link LookaheadReader} has been closed.
     */
    public boolean isClosed() {
        return isClosed;
    }

}
