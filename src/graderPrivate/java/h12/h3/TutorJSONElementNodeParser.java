package h12.h3;

import h12.exceptions.BadFileEndingException;
import h12.exceptions.JSONParseException;
import h12.exceptions.UnexpectedCharacterException;
import h12.json.JSONElement;
import h12.json.LookaheadReader;
import h12.json.parser.implementation.node.*;

import java.io.IOException;
import java.util.function.Predicate;

public class TutorJSONElementNodeParser extends JSONElementNodeParser {

    private JSONObjectNodeParser objectParser = new JSONObjectNodeParser(this);
    private JSONArrayNodeParser arrayParser = new JSONArrayNodeParser(this);
    private JSONStringNodeParser stringParser = new JSONStringNodeParser(this);
    private JSONConstantNodeParser constantParser = new JSONConstantNodeParser(this);
    private JSONNumberNodeParser numberParser = new JSONNumberNodeParser(this);
    private JSONObjectEntryNodeParser objectEntryParser = new JSONObjectEntryNodeParser(this);

    private final LookaheadReader reader;

    /**
     * Creates a new {@link JSONArrayNodeParser}-Instance.
     *
     * @param reader The reader containing the contents of the JSON file to parse.
     */
    public TutorJSONElementNodeParser(LookaheadReader reader) {
        super(reader);
        this.reader = reader;
    }

    /**
     * Skips every whitespace Character until the next char is a non-whitespace character.
     *
     * <p> For the definition of a whitespace character see method {@link Character#isWhitespace(char)}.
     *
     * @throws IOException If an {@link IOException} occurs while reading the contents of the reader.
     */
    @SuppressWarnings({"ignored", "ResultOfMethodCallIgnored"})
    public void skipIndentation() throws IOException {
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
    public int acceptIt() throws IOException {
        skipIndentation();
        return reader.read();
    }

    /**
     * Reads the next non-whitespace character and checks if it equals the expected character.
     *
     * @param expected The expected character.
     * @throws IOException                  If an {@link IOException} occurs while reading the contents of the reader.
     * @throws UnexpectedCharacterException if the character read does not equal the expected character.
     * @throws BadFileEndingException       If the reader has already reached the end.
     * @see #skipIndentation()
     */
    public void accept(char expected) throws IOException, UnexpectedCharacterException, BadFileEndingException {
        skipIndentation();
        int actual = reader.read();
        if (actual == -1) throw new BadFileEndingException();
        if (actual != expected) throw new UnexpectedCharacterException(expected, actual);
    }

    /**
     * Retrieves the next character without skipping that character.
     *
     * @return The next character or -1 if the end of the reader is reached.
     * @throws IOException If an {@link IOException} occurs while reading the contents of the reader.
     * @see LookaheadReader
     */
    public int peek() throws IOException {
        skipIndentation();
        return reader.peek();
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException            If an {@link IOException} occurs while reading the contents of the reader.
     * @throws BadFileEndingException If the reader contains any characters that yet have to be processed.
     */
    @Override
    public void checkEndOfFile() throws IOException, BadFileEndingException {
        skipIndentation();
        if (reader.peek() != -1) throw new BadFileEndingException();
    }

    /**
     * Collects every character read until {@code stopPred.test()} returns true for the current character and returns them as a {@link String}.
     *
     * <p> The character that causes {@code stopPred.test()} to return true is neither read by the reader nor included in the returned String.
     *
     * @param stopPred The predicate to determines whether to stop reading more characters or to continue.
     * @return A String containing the collected characters.
     * @throws IOException            If an {@link IOException} occurs while reading the contents of the reader.
     * @throws BadFileEndingException If the end of the File is reached before the {@link Predicate} returned true.
     */
    public String readUntil(Predicate<Integer> stopPred) throws IOException, BadFileEndingException {
        StringBuilder builder = new StringBuilder();

        while (!stopPred.test(reader.peek())) {

            if (reader.peek() == -1) {
                throw new BadFileEndingException();
            }

            builder.append((char) reader.read());
        }

        return builder.toString();
    }

    /**
     * Parses the next {@link JSONElement} by calling the responsible {@link JSONNodeParser}.
     *
     * @return The parsed {@link JSONElement} or {@code null} if the end of the {@link LookaheadReader} has been reached.
     * @throws IOException        If an {@link IOException} occurs while reading the contents of the reader.
     * @throws JSONParseException If the parsed JSON file is invalid.
     */
    @Override
    public JSONElement parse() throws IOException {
        int peek = peek();

        if (peek == '{') {
            return objectParser.parse();
        } else if (peek == '[') {
            return arrayParser.parse();
        } else if (peek == '"') {
            return stringParser.parse();
        } else if (Character.isDigit(peek) || peek == '+' || peek == '-' || peek == '.') {
            return numberParser.parse();
        } else if (peek == -1) {
            return null;
        } else {
            return constantParser.parse();
        }
    }

    /**
     * Sets the {@link JSONObjectNodeParser} used by this {@link JSONElementNodeParser} to the given {@link JSONObjectNodeParser}.
     *
     * @param objectParser The new {@link JSONObjectNodeParser}.
     */
    public void setObjectParser(JSONObjectNodeParser objectParser) {
        this.objectParser = objectParser;
    }

    /**
     * Sets the {@link JSONArrayNodeParser} used by this {@link JSONElementNodeParser} to the given {@link JSONArrayNodeParser}.
     *
     * @param arrayParser The new {@link JSONArrayNodeParser}.
     */
    public void setArrayParser(JSONArrayNodeParser arrayParser) {
        this.arrayParser = arrayParser;
    }

    /**
     * Sets the {@link JSONStringNodeParser} used by this {@link JSONElementNodeParser} to the given {@link JSONStringNodeParser}.
     *
     * @param stringParser The new {@link JSONStringNodeParser}.
     */
    public void setStringParser(JSONStringNodeParser stringParser) {
        this.stringParser = stringParser;
    }

    /**
     * Sets the {@link JSONConstantNodeParser} used by this {@link JSONElementNodeParser} to the given {@link JSONConstantNodeParser}.
     *
     * @param constantParser The new {@link JSONConstantNodeParser}.
     */
    public void setConstantParser(JSONConstantNodeParser constantParser) {
        this.constantParser = constantParser;
    }

    /**
     * Sets the {@link JSONNumberNodeParser} used by this {@link JSONElementNodeParser} to the given {@link JSONNumberNodeParser}.
     *
     * @param numberParser The new {@link JSONNumberNodeParser}.
     */
    public void setNumberParser(JSONNumberNodeParser numberParser) {
        this.numberParser = numberParser;
    }

    /**
     * Sets the {@link JSONObjectEntryNodeParser} used by this {@link JSONElementNodeParser} to the given {@link JSONObjectEntryNodeParser}.
     *
     * @param objectEntryParser The new {@link JSONObjectEntryNodeParser}.
     */
    public void setObjectEntryParser(JSONObjectEntryNodeParser objectEntryParser) {
        this.objectEntryParser = objectEntryParser;
    }

    /**
     * Returns the {@link JSONObjectNodeParser} used by this {@link JSONElementNodeParser}.
     *
     * @return the {@link JSONObjectNodeParser} used by this {@link JSONElementNodeParser}.
     */
    public JSONObjectNodeParser getObjectParser() {
        return objectParser;
    }

    /**
     * Returns the {@link JSONArrayNodeParser} used by this {@link JSONElementNodeParser}.
     *
     * @return the {@link JSONArrayNodeParser} used by this {@link JSONElementNodeParser}.
     */
    public JSONArrayNodeParser getArrayParser() {
        return arrayParser;
    }

    /**
     * Returns the {@link JSONStringNodeParser} used by this {@link JSONElementNodeParser}.
     *
     * @return the {@link JSONStringNodeParser} used by this {@link JSONElementNodeParser}.
     */
    public JSONStringNodeParser getStringParser() {
        return stringParser;
    }

    /**
     * Returns the {@link JSONConstantNodeParser} used by this {@link JSONElementNodeParser}.
     *
     * @return the {@link JSONConstantNodeParser} used by this {@link JSONElementNodeParser}.
     */
    public JSONConstantNodeParser getConstantParser() {
        return constantParser;
    }

    /**
     * Returns the {@link JSONNumberNodeParser} used by this {@link JSONElementNodeParser}.
     *
     * @return the {@link JSONNumberNodeParser} used by this {@link JSONElementNodeParser}.
     */
    public JSONNumberNodeParser getNumberParser() {
        return numberParser;
    }

    /**
     * Returns the {@link JSONObjectEntryNodeParser} used by this {@link JSONElementNodeParser}.
     *
     * @return the {@link JSONObjectEntryNodeParser} used by this {@link JSONElementNodeParser}.
     */
    public JSONObjectEntryNodeParser getObjectEntryParser() {
        return objectEntryParser;
    }

}
