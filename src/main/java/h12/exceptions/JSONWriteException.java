package h12.exceptions;

public class JSONWriteException extends RuntimeException {

    public JSONWriteException(String message) {
        super("An Exception occurred while trying to write to a JSON file. %s".formatted(message));
    }

}
