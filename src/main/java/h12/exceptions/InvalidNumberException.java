package h12.exceptions;

public class InvalidNumberException extends JSONParseException {

    public InvalidNumberException(String constant) {
        super("Invalid number: %s!".formatted(constant));
    }
}
