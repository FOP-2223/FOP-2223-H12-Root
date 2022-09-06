package h12.json.parser.implementation.node;

import h12.exceptions.InvalidNumberException;
import h12.json.implementation.node.JSONNumberNode;

import java.io.IOException;

/**
 * A parser based on a node implementation that parses a JSON number.
 *
 * <p> Example:
 * <p> Input: -69.420
 * <p> Output: {@code new JSONIntegerNode(-69.420)}
 */
public class JSONNumberNodeParser implements JSONNodeParser {

    private final JSONElementNodeParser parser;

    public JSONNumberNodeParser(JSONElementNodeParser parser) {
        this.parser = parser;
    }

    /**
     * Parses a JSON number.
     *
     * @return The parsed {@link JSONNumberNode}.
     * @throws IOException            If an {@link IOException} occurs while reading the contents of the reader.
     * @throws InvalidNumberException If the parsed number is invalid.
     */
    @Override
    public JSONNumberNode parse() throws IOException, InvalidNumberException {

        String number = parser.readUntil(i -> !(Character.isDigit(i) || i == '+' || i == '-' || i == '.'));

        try {
            if (number.contains(".")) {
                return new JSONNumberNode(Integer.parseInt(number));
            } else {
                return new JSONNumberNode(Double.parseDouble(number));
            }
        } catch (NumberFormatException exc) {
            throw new InvalidNumberException(exc.getMessage());
        }
    }

}
