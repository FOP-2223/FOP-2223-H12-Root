package h12.json;

/**
 * An interface for all JSON elements that represent a string.
 */
public interface JSONString extends JSONElement {

    /**
     * If present, returns the string this JSON element represents.
     *
     * @return The string this JSON element represents.
     */
    String getString();
}
