package h12.json.parser.implementation.node;

import h12.exceptions.JSONParseException;
import h12.exceptions.TrailingCommaException;
import h12.json.JSONObject;
import h12.json.implementation.node.JSONObjectNode;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static h12.json.JSONObject.JSONObjectEntry;

/**
 * A parser based on a node implementation that parses a {@link JSONObject}.
 *
 * <p> Example:
 * <P> Input:
 * <pre>
 * {
 *   "abc": 69,
 *   "def": 70,
 * }</pre>
 * <p> Output: {@code JSONObject.of(JSONObjectEntry.of("abc", JSONNumber.of(69)), JSONObjectEntry.of("def", JSONNumber.of(70)))}
 */
public class JSONObjectNodeParser implements JSONNodeParser {

    private final JSONElementNodeParser parser;

    /**
     * Creates a new {@link JSONObjectNodeParser}-Instance.
     *
     * @param parser The main {@link JSONElementNodeParser}.
     */
    public JSONObjectNodeParser(JSONElementNodeParser parser) {
        this.parser = parser;
    }

    /**
     * Parses a {@link JSONObject}.
     * <p>If a string is used multiple time as an identifier, the last occurrence is used.
     *
     * @return The parsed {@link JSONObjectNode}.
     * @throws IOException            If an {@link IOException} occurs while reading the contents of the reader.
     * @throws TrailingCommaException If the parsed {@link JSONObject} contains a trailing comma.
     * @throws JSONParseException     If the parsed {@link JSONObject} is invalid in any other way.
     */
    @Override
    public JSONObjectNode parse() throws IOException, JSONParseException {

        parser.accept('{');

        Set<JSONObjectEntry> set = new HashSet<>();

        while (parser.peek() != '}') {
            JSONObjectEntry parsed = parser.getObjectEntryParser().parse();

            set.add(parsed);

            if (parser.peek() != '}') {
                parser.accept(',');

                if (parser.peek() == '}') {
                    throw new TrailingCommaException();
                }
            }
        }

        parser.accept('}');

        return new JSONObjectNode(set);
    }

}
