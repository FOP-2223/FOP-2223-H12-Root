package h12.json;

/**
 * An interface for all JSON elements that represent a constant.
 */
public interface JSONConstant extends JSONElement {

    /**
     * Returns the constant this JSON element represents.
     *
     * @return The constant this JSON element represents.
     */
    @Override
    JSONConstants getConstant();
}
