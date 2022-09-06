package h12.json;

/**
 * An interface for all JSON elements that represent an array.
 */
public interface JSONArray extends JSONElement {

    /**
     * Returns the array this JSON element represents.
     *
     * @return The array this JSON element represents.
     **/
    @Override
    JSONElement[] getArray();

}
