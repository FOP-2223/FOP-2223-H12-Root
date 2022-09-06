package h12.exceptions;

public class InvalidConstantException extends JSONParseException {

    public InvalidConstantException(String constant) {
        super("Invalid constant: %s!".formatted(constant));
    }
}
