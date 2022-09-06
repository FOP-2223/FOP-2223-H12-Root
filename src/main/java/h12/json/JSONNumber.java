package h12.json;

/**
 * An interface for all JSON elements that represent a number.
 */
public interface JSONNumber extends JSONElement {

    /**
     * Returns the number this JSON element represents.
     *
     * @return The number this JSON element represents.
     */
    @Override
    Number getNumber();

    /**
     * Returns the number this JSON element represents as an {@link Integer}.
     *
     * @return The number this JSON element represents as an {@link Integer}.
     */
    @Override
    Integer getInteger();

    /**
     * Returns the number this JSON element represents as a {@link Double}.
     *
     * @return The number this JSON element represents as a {@link Double}.
     */
    @Override
    Double getDouble();

}
