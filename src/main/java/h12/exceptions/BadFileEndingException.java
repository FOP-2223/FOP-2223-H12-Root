package h12.exceptions;

public class BadFileEndingException extends JSONParseException {


    public BadFileEndingException() {
        super("The file didn't end properly after the content was parsed!");
    }
}
