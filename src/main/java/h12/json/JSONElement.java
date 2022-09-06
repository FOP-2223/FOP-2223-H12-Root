package h12.json;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * An interface for all elements a JSON File can contain
 */
public interface JSONElement {

    /**
     * Transforms this JSON Element into a string representation and writes it using the given reader.
     *
     * @param writer      The writer used to write the string representation.
     * @param indentation The current indentation.
     * @throws IOException If writing to the writer causes an IOException.
     */
    void write(BufferedWriter writer, int indentation) throws IOException;

    /**
     * If present, returns the array this JSON element represents.
     *
     * @return The array this JSON element represents.
     * @throws UnsupportedOperationException If this JSON element does not represent an array.
     */
    default JSONElement[] getArray() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This JSON Element does not support the operation getArray().");
    }

    /**
     * If present, returns the constant this JSON element represents.
     *
     * @return The constant this JSON element represents.
     * @throws UnsupportedOperationException If this JSON element does not represent a constant.
     */
    default JSONConstants getConstant() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This JSON Element does not support the operation getConstant().");
    }

    /**
     * If present, returns the number this JSON element represents.
     *
     * @return The number this JSON element represents.
     * @throws UnsupportedOperationException If this JSON element does not represent a number.
     */
    default Number getNumber() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This JSON Element does not support the operation getNumber().");
    }

    /**
     * If present, returns the number this JSON element represents as an {@link Integer}.
     *
     * @return The number this JSON element represents as an {@link Integer}.
     * @throws UnsupportedOperationException If this JSON element does not represent a number.
     */
    default Integer getInteger() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This JSON Element does not support the operation getInteger().");
    }

    /**
     * If present, returns the number this JSON element represents as a {@link Double}.
     *
     * @return The number this JSON element represents as a {@link Double}.
     * @throws UnsupportedOperationException If this JSON element does not represent a number.
     */
    default Double getDouble() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This JSON Element does not support the operation getDouble().");
    }

    /**
     * If present, returns a map containing the objectEntries of the object this JSON element represents.
     *
     * @return A map containing the objectEntries of the object this JSON element represents.
     * @throws UnsupportedOperationException If this JSON element does not represent an object.
     */
    default Map<JSONString, JSONElement> getObjectEntries() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This JSON Element does not support the operation getObjectEntries().");
    }

    /**
     * If present, returns the string this JSON element represents.
     *
     * @return The string this JSON element represents.
     * @throws UnsupportedOperationException If this JSON element does not represent a string.
     */
    default String getString() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This JSON Element does not support the operation getString().");
    }

    /**
     * Returns the JSON element that is mapped to the given string.
     *
     * @param key The key to look for in the object entries.
     * @return the JSON element that is mapped to given string or null if this object does not contain a mapping for this key.
     */
    default JSONElement getEntry(String key) {
        throw new UnsupportedOperationException("This JSON Element does not support the operation getEntry(String).");
    }
}
