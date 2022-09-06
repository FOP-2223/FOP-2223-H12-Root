package h12.json;

import h12.json.implementation.node.JSONStringNode;

import java.util.Map;

/**
 * An interface for all JSON elements that represent an integer.
 */
public interface JSONObject extends JSONElement {

    /**
     * If present, returns a map containing the objectEntries of the object this JSON element represents.
     *
     * @return A map containing the objectEntries of the object this JSON element represents.
     */
    Map<JSONString, JSONElement> getObjectEntries();

    /**
     * Returns the JSON element that is mapped to the given string.
     *
     * @param key The key to look for in the object entries.
     * @return the JSON element that is mapped to given string or null if this object does not contain a mapping for this key.
     */
    default JSONElement getEntry(String key) {
        return getObjectEntries().get(new JSONStringNode(key));
    }

}
