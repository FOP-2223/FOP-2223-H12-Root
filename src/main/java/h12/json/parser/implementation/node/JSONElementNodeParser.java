package h12.json.parser.implementation.node;

import h12.exceptions.BadFileEndingException;
import h12.exceptions.JSONParseException;
import h12.exceptions.UnexpectedCharacterException;
import h12.json.JSONElement;
import h12.json.LookaheadReader;
import h12.json.parser.JSONElementParser;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * A parser based on a node implementation that parses a JSON element.
 */
public class JSONElementNodeParser implements JSONElementParser {

    final JSONObjectNodeParser objectParser = new JSONObjectNodeParser(this);
    final JSONArrayNodeParser arrayParser = new JSONArrayNodeParser(this);
    final JSONStringNodeParser stringParser = new JSONStringNodeParser(this);
    final JSONConstantNodeParser constantParser = new JSONConstantNodeParser(this);
    final JSONNumberNodeParser integerParser = new JSONNumberNodeParser(this);

    private final LookaheadReader reader;

    /**
     * Creates a new {@link JSONArrayNodeParser}-Instance.
     *
     * @param reader The reader containing the contents of the JSON file to parse.
     */
    public JSONElementNodeParser(LookaheadReader reader) {
        this.reader = reader;
    }

    /**
     * Parses the next JSON element by calling the responsible {@link JSONNodeParser}.
     *
     * @return The parsed {@link JSONElement} or {@code null} if the end of the {@link LookaheadReader} has been reached.
     * @throws IOException        If an {@link IOException} occurs while reading the contents of the reader.
     * @throws JSONParseException If the parsed JSON file is invalid.
     */
    @Override
    public JSONElement parse() throws IOException {
        if (peek() == '{') {
            return objectParser.parse();
        } else if (reader.peek() == '[') {
            return arrayParser.parse();
        } else if (reader.peek() == '"') {
            return stringParser.parse();
        } else if (Character.isDigit(reader.peek()) || reader.peek() == '+' || reader.peek() == '-') {
            return integerParser.parse();
        } else if (reader.peek() == -1) {
            return null;
        } else {
            return constantParser.parse();
        }
    }

    /**
     * Skips every whitespace Character until the next char is a non-whitespace character.
     *
     * <p> For the definition of a whitespace character see method {@link Character#isWhitespace(char)}.
     *
     * @throws IOException If an {@link IOException} occurs while reading the contents of the reader.
     */
    private void skipIndentation() throws IOException {
        while (Character.isWhitespace(reader.peek())) {
            reader.read();
        }
    }

    /**
     * Reads the next non-whitespace character.
     *
     * @return The next character.
     * @throws IOException If an {@link IOException} occurs while reading the contents of the reader.
     * @see #skipIndentation()
     */
    int acceptIt() throws IOException {
        skipIndentation();
        return reader.read();
    }

    /**
     * Reads the next non-whitespace character and checks if it equals the expected character.
     *
     * @param expected The expected character.
     * @throws IOException                  If an {@link IOException} occurs while reading the contents of the reader.
     * @throws UnexpectedCharacterException if the character read does not equal the expected character.
     * @see #skipIndentation()
     */
    void accept(char expected) throws IOException, UnexpectedCharacterException {
        skipIndentation();
        int actual = reader.read();
        if (actual != expected) throw new UnexpectedCharacterException(expected, actual);
    }

    /**
     * Retrieves the next character without skipping that character.
     *
     * @return The next character or -1 if the end of the reader is reached.
     * @throws IOException If an {@link IOException} occurs while reading the contents of the reader.
     * @see LookaheadReader
     */
    int peek() throws IOException {
        skipIndentation();
        return reader.peek();
    }

    /**
     * Collects every character read until {@code stopPred.test()} returns true for the current character and returns them as a {@link String}.
     *
     * <p> The character that causes {@code stopPred.test()} to return true is neither read by the reader nor included in the returned String.
     *
     * @param stopPred The predicate to determines whether to stop reading more characters or to continue.
     * @return A String containing the collected characters.
     * @throws IOException If an {@link IOException} occurs while reading the contents of the reader.
     */
    String readUntil(Predicate<Integer> stopPred) throws IOException {
        StringBuilder builder = new StringBuilder();

        while (!stopPred.test(reader.peek())) {
            builder.append((char) acceptIt());
        }

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkEndOfFile() throws IOException {
        skipIndentation();
        if (reader.read() != -1) throw new BadFileEndingException();
    }

}
