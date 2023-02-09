package h12.h3;

import h12.exceptions.InvalidNumberException;
import h12.json.JSONNumber;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.parser.implementation.node.JSONElementNodeParser;
import h12.json.parser.implementation.node.JSONNumberNodeParser;

import java.io.IOException;

public class TutorNumberParser extends JSONNumberNodeParser {

    private final JSONElementNodeParser parser;

    /**
     * Creates a new {@link JSONNumberNodeParser}-Instance.
     *
     * @param parser The main {@link JSONElementNodeParser}.
     */
    public TutorNumberParser(JSONElementNodeParser parser) {
        super(parser);
        this.parser = parser;
    }

    /**
     * Parses a {@link JSONNumber}.
     *
     * @return The parsed {@link JSONNumberNode}.
     * @throws IOException            If an {@link IOException} occurs while reading the contents of the reader.
     * @throws InvalidNumberException If the parsed {@link JSONNumber} is invalid.
     */
    @Override
    public JSONNumberNode parse() throws IOException, InvalidNumberException {

        String number = parser.readUntil(i -> !(Character.isDigit(i) || i == '+' || i == '-' || i == '.'));

        try {
            if (number.contains(".")) {
                return new JSONNumberNode(Double.parseDouble(number));
            } else {
                return new JSONNumberNode(Integer.parseInt(number));
            }
        } catch (NumberFormatException exc) {
            throw new InvalidNumberException(number);
        }
    }

}
