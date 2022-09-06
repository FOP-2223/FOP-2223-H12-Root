package h12.exceptions;

public class JSONParseException extends RuntimeException {

    public JSONParseException(String message) {
        super("An Exception occurred while trying to parse a JSON file. %s".formatted(message));
    }
}

