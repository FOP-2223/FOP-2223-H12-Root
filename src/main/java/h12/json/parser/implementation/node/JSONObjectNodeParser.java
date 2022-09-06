package h12.json.parser.implementation.node;

import h12.exceptions.JSONParseException;
import h12.json.JSONElement;
import h12.json.JSONString;
import h12.json.implementation.node.JSONObjectNode;

import java.io.IOException;
import java.util.HashMap;

/**
 * A parser based on a node implementation that parses a JSON object.
 *
 * <p> Example:
 * <P> Input:
 * <pre>
 * {
 *   420,
 *   "abc"
 * }</pre>
 * <p> Output: {@code new JSONObjectNode(Map.of(new JSONIntegerNode(420), new JSONStringNode("abc")))}
 */
public class JSONObjectNodeParser implements JSONNodeParser {

    private final JSONElementNodeParser parser;

    /**
     * Creates a new {@link JSONArrayNodeParser}-Instance.
     *
     * @param parser The main {@link JSONElementNodeParser}.
     */
    public JSONObjectNodeParser(JSONElementNodeParser parser) {
        this.parser = parser;
    }

    /**
     * Parses a JSON object. If a string is used multiple time as an identifier, the last occurrence is used.
     *
     * @return The parsed {@link JSONObjectNode}.
     * @throws IOException        If an {@link IOException} occurs while reading the contents of the reader.
     * @throws JSONParseException If the parsed JSON file is invalid.
     */
    @Override
    public JSONObjectNode parse() throws IOException, JSONParseException {

        parser.accept('{');

        HashMap<JSONString, JSONElement> map = new HashMap<>();

        while (parser.peek() != '}') {

            JSONString stringNode = parser.stringParser.parse();

            parser.accept(':');

            map.put(stringNode, parser.parse());

            if (parser.peek() != '}') {
                parser.accept(',');
            }
        }

        parser.accept('}');

        return new JSONObjectNode(map);
    }

}
