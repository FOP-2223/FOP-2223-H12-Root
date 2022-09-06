package h12.json.parser.implementation.node;

import h12.exceptions.JSONParseException;
import h12.json.implementation.node.JSONNode;

import java.io.IOException;

/**
 * An interface for parsers based on a node implementation that parses a specific JSON Element.
 */
public interface JSONNodeParser {

    /**
     * Parses a specific JSON Element using the {@link JSONElementNodeParser} and returns the parsed {@link JSONNode}.
     *
     * @return the parsed {@link JSONNode}.
     * @throws IOException        If an {@link IOException} occurs while reading the contents of the reader.
     * @throws JSONParseException If the parsed JSON file is invalid.
     */
    JSONNode parse() throws IOException, JSONParseException;

}
