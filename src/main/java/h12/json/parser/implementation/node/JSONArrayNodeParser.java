package h12.json.parser.implementation.node;

import h12.exceptions.JSONParseException;
import h12.json.JSONElement;
import h12.json.implementation.node.JSONArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A parser based on a node implementation that parses a JSON array.
 *
 * <p> Example:
 * <p> Input:
 * <pre>
 * [
 *   420,
 *   "abc"
 * ]</pre>
 * <p> Output: {@code new JSONArrayNode(List.of(new JSONIntegerNode(420), new JSONStringNode("abc")))}
 */
public class JSONArrayNodeParser implements JSONNodeParser {

    private final JSONElementNodeParser parser;

    /**
     * Creates a new {@link JSONArrayNodeParser}-Instance.
     *
     * @param parser The main {@link JSONElementNodeParser}.
     */
    public JSONArrayNodeParser(JSONElementNodeParser parser) {
        this.parser = parser;
    }

    /**
     * Parses a JSON array.
     *
     * @return The parsed {@link JSONArrayNode}.
     * @throws IOException        If an {@link IOException} occurs while reading the contents of the reader.
     * @throws JSONParseException If the parsed JSON file is invalid.
     */
    @Override
    public JSONArrayNode parse() throws IOException, JSONParseException {
        List<JSONElement> list = new ArrayList<>();

        parser.accept('[');

        while (parser.peek() != ']') {
            list.add(parser.parse());
            if (parser.peek() != ']') parser.accept(',');
        }

        parser.accept(']');

        return new JSONArrayNode(list);
    }

}
