package h12.json.implementation.node;

import h12.json.JSONElement;
import h12.json.JSONObject;
import h12.json.JSONString;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * A class representing a JSON object implemented as a node.
 * <p> The object entries of the object are stored in a {@code Map&lt;JSONString, JSONElement&gt;}.
 */
public class JSONObjectNode extends JSONNode implements JSONObject {

    Map<JSONString, JSONElement> objectEntries;

    /**
     * Creates a new {@link JSONObjectNode}-Instance.
     *
     * @param map A map containing all the object entries of the object.
     */
    public JSONObjectNode(Map<JSONString, JSONElement> map) {
        this.objectEntries = map;
    }

    /**
     * Writes the string representation of this JSON array to the given writer using the current indentation.
     * <p> The formatting follows these rules:
     * <p> 1. Every {@code JSONElement} is written to the writer using their write methode.
     * <p> 2. The opening bracket ({@code '&#123'}) and every object entry (including the {@code ','}) is followed by a line break ({@code '\n'}).
     * <p> 3. Every line break ({@code '\n'}) is followed by an indentation which is created using {@link JSONNode#writeIndentation(BufferedWriter, int)}.
     * <p> 4. The indentation in front of an object entry is one higher than the current indentation. The indentation in front of the closing bracket ({@code  '&#125'}) is the current indentation.
     * <p> 5. The colon ({@code ':'}) in the middle of an object entry is followed by a single space character({@code ' '}).
     *
     * @param writer      The writer used to write the string representation.
     * @param indentation The current indentation.
     * @throws IOException If an {@link IOException} occurs while writing to the writer.
     */
    @Override
    public void write(BufferedWriter writer, int indentation) throws IOException {
        writer.write("{\n");
        int i = 0;
        for (Map.Entry<JSONString, JSONElement> entry : objectEntries.entrySet()) {
            writeIndentation(writer, indentation + 1);
            entry.getKey().write(writer, indentation);
            writer.write(": ");
            entry.getValue().write(writer, indentation + 1);
            if (i < objectEntries.size() - 1) writer.write(",");
            writer.write("\n");
            i++;
        }
        writeIndentation(writer, indentation);
        writer.write("}");
    }

    /**
     * Returns a map containing the object entries of this JSON object.
     *
     * @return a map containing the object entries.
     */
    @Override
    public Map<JSONString, JSONElement> getObjectEntries() {
        return Collections.unmodifiableMap(objectEntries);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSONObjectNode that = (JSONObjectNode) o;
        return Objects.equals(objectEntries, that.objectEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectEntries);
    }
}
