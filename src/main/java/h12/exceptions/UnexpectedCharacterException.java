package h12.exceptions;

public class UnexpectedCharacterException extends JSONParseException {

    public UnexpectedCharacterException(char expected, int actual) {
        super("Received an unexpected character. Expected: <%s>, but was: <%s>".formatted(expected, actual == -1 ? "EOF" : (char) actual));
    }
}
