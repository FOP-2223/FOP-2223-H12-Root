package h12.json;

import h12.exceptions.JSONParseException;
import h12.exceptions.JSONWriteException;
import h12.ioFactory.FileSystemIOFactory;
import h12.ioFactory.IOFactory;
import h12.json.parser.JSONParser;
import h12.json.parser.JSONParserFactory;
import h12.json.parser.implementation.node.JSONNodeParserFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * A class for handling JSON files.
 */
public class JSON {

    private static JSONParserFactory parserFactory = new JSONNodeParserFactory();
    private static IOFactory ioFactory = new FileSystemIOFactory();

    private JSONElement root;

    /**
     * Creates a new {@link JSON}-Instance with the given root element.
     *
     * @param root The root element of this {@link JSON}-Instance.
     */
    public JSON(JSONElement root) {
        this.root = root;
    }

    /**
     * Creates a new, empty {@link JSON}-Instance.
     */
    public JSON() {
    }

    /**
     * Writes a string representation of this {@link JSON} object to the given file.
     *
     * @param fileName The name of the file to write the information.
     * @throws JSONWriteException If an exception occurs while writing to the JSON file or the {@link #ioFactory} does not support writing.
     */
    public void write(String fileName) throws JSONWriteException {

        if (!ioFactory.supportsWriter()) {
            throw new JSONWriteException("The current ioFactory does not support writing!");
        }

        if (root == null) {
            return;
        }

        try (BufferedWriter writer = ioFactory.createWriter(fileName)) {
            root.write(writer, 0);
        } catch (IOException exc) {
            throw new JSONWriteException(exc.getMessage());
        }
    }

    /**
     * Reads the content of the given JSON file and parses the content of it.
     * The parsed content will be saved to the {@link #root} object of this {@link JSON} object.
     *
     * @param fileName The fileName to the JSON file to parse.
     * @throws JSONParseException If an exception occurs while trying to parse the JSON file or the {@link #ioFactory} does not support reading.
     */
    public void parse(String fileName) throws JSONParseException {

        if (!ioFactory.supportsReader()) {
            throw new JSONParseException("The current ioFactory does not support reading!");
        }

        try (LookaheadReader reader = new LookaheadReader(ioFactory.createReader(fileName))) {
            JSONParser parser = parserFactory.createParser(reader);
            root = parser.parse();
        } catch (IOException exc) {
            throw new JSONParseException(exc.getMessage());
        }
    }

    /**
     * returns the root element of this {@link JSON}-Instance.
     *
     * @return the root element of this {@link JSON}-Instance.
     */
    public JSONElement getRoot() {
        return root;
    }

    /**
     * Sets the default {@link JSONParserFactory} that is used to create a {@link JSONParser}
     *
     * @param parserFactory The new default {@link JSONParserFactory}.
     */
    public static void setParserFactory(JSONParserFactory parserFactory) {
        JSON.parserFactory = parserFactory;
    }

    /**
     * Sets the default {@link IOFactory} that is used to create the {@link Reader} and {@link Writer} for the JSON files..
     *
     * @param ioFactory The new default {@link IOFactory}.
     */
    public static void setIOFactory(IOFactory ioFactory) {
        JSON.ioFactory = ioFactory;
    }
}
