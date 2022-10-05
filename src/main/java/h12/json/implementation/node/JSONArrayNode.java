package h12.json.implementation.node;

import h12.json.JSONArray;
import h12.json.JSONElement;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a JSON array implemented as a node.
 * <p> The contents of the array are stored in a {@code List&lt;JSONElement&gt;}.
 */
public class JSONArrayNode extends JSONNode implements JSONArray {

    private final List<JSONElement> list;

    /**
     * Creates a new {@link JSONArrayNode}-Instance.
     *
     * @param list A list containing all the elements of the array.
     */
    public JSONArrayNode(List<JSONElement> list) {
        this.list = list;
    }

    /**
     * Writes the string representation of this {@link JSONArray} to the given writer using the current indentation.
     * <p> The formatting follows these rules:
     * <p> 1. Every {@link JSONElement} is written to the writer using their write methode.
     * <p> 2. The opening bracket ({@code '['}) and every {@link JSONElement} (including the {@code ','}) is followed by a line break ({@code '\n'}).
     * <p> 3. Every line break ({@code '\n'}) is followed by an indentation which is created using {@link JSONNode#writeIndentation(BufferedWriter, int)}.
     * <p> 4. The indentation in front of a {@link JSONElement} is one higher than the current indentation. The indentation in front of the closing bracket ({@code  ']'}) is the current indentation.
     *
     * @param writer      The writer used to write the string representation.
     * @param indentation The current indentation.
     * @throws IOException If an {@link IOException} occurs while writing to the writer.
     */
    @Override
    public void write(BufferedWriter writer, int indentation) throws IOException {
        writer.write("[\n");
        for (int i = 0; i < list.size(); i++) {
            writeIndentation(writer, indentation + 1);
            list.get(i).write(writer, indentation + 1);
            if (i < list.size() - 1) writer.write(",");
            writer.write("\n");
        }
        writeIndentation(writer, indentation);
        writer.write("]");
    }

    /**
     * Returns the array this JSON element represents.
     *
     * @return The array this JSON element represents.
     */
    @Override
    public JSONElement[] getArray() {
        return list.toArray(JSONElement[]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSONArrayNode that = (JSONArrayNode) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
