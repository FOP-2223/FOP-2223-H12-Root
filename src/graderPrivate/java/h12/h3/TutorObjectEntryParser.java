package h12.h3;

import h12.exceptions.BadFileEndingException;
import h12.exceptions.JSONParseException;
import h12.json.JSONArray;
import h12.json.JSONElement;
import h12.json.JSONObject;
import h12.json.implementation.node.JSONObjectNode;
import h12.json.implementation.node.JSONStringNode;
import h12.json.parser.implementation.node.JSONElementNodeParser;
import h12.json.parser.implementation.node.JSONObjectEntryNodeParser;

import java.io.IOException;

public class TutorObjectEntryParser extends JSONObjectEntryNodeParser {

    private final JSONElementNodeParser parser;

    /**
     * Creates a new {@link JSONObjectEntryNodeParser}-Instance.
     *
     * @param parser The main {@link JSONElementNodeParser}.
     */
    public TutorObjectEntryParser(JSONElementNodeParser parser) {
        super(parser);
        this.parser = parser;
    }

    /**
     * Parses a {@link JSONObject.JSONObjectEntry}.
     *
     * @return The parsed {@link JSONObjectNode.JSONObjectEntryNode}.
     * @throws IOException            If an {@link IOException} occurs while reading the contents of the reader.
     * @throws BadFileEndingException If the reader ends before the {@link JSONObject.JSONObjectEntry} is completed.
     * @throws JSONParseException     If the parsed {@link JSONArray} is invalid in any other way.
     */
    @Override
    public JSONObjectNode.JSONObjectEntryNode parse() throws IOException, JSONParseException {
        JSONStringNode identifier = parser.getStringParser().parse();

        parser.accept(':');
        JSONElement value = parser.parse();

        if (value == null) {
            throw new BadFileEndingException();
        }

        return JSONObject.JSONObjectEntry.of(identifier.getString(), value);
    }

}
