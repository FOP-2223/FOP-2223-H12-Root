package h12.h3;

import h12.exceptions.BadFileEndingException;
import h12.exceptions.TrailingCommaException;
import h12.exceptions.UnexpectedCharacterException;
import h12.json.JSONElement;
import h12.json.JSONNumber;
import h12.json.parser.implementation.node.JSONElementNodeParser;
import h12.json.parser.implementation.node.JSONObjectNodeParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.io.IOException;
import java.util.Set;

import static h12.json.JSONObject.JSONObjectEntry;

@TestForSubmission
public class TutorTests_H3_3_JSONObjectNodeParserTest extends TutorTests_JSONParseTest {


    @ParameterizedTest
    @CsvSource("a, 1, b, 2, c, 3, ]") //single digit
    public void testParseSuccess(String k1, Integer v1, String k2, Integer v2, String k3, Integer v3, String extension) throws Throwable {
        testParseSuccess(JSONObjectNodeParser::new,
            "{\"%s\": %d, \"%s\": %d, \"%s\": %d}%s".formatted(k1, v1, k2, v2, k3, v3, extension),
            Set.of(JSONObjectEntry.of(k1, JSONNumber.of(v1)), JSONObjectEntry.of(k2, JSONNumber.of(v2)), JSONObjectEntry.of(k3, JSONNumber.of(v3))),
            extension,
            JSONElement::getObjectEntries,
            elementNodeParser -> mockObjectEntryParser(elementNodeParser, new String[]{k1, k2, k3}, new Integer[]{v1, v2, v3}),
            createNodeParserVerifier(3, JSONElementNodeParser::getObjectEntryParser, "JSONObjectEntryNodeParser"));
    }

    @ParameterizedTest
    @CsvSource("a, 1, b, 2, c, 3") //single digit
    public void testParseException(String k1, Integer v1, String k2, Integer v2, String k3, Integer v3) throws IOException {

        //missing opening bracket
        testParseExceptionWithMessage(UnexpectedCharacterException.class, JSONObjectNodeParser::new, "\"%s\": %d, \"%s\": %d, \"%s\": %d}"
            .formatted(k1, v1, k2, v2, k3, v3), elementNodeParser -> mockObjectEntryParser(elementNodeParser, new String[]{k1, k2, k3}, new Integer[]{v1, v2, v3}),
            "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <{>, but was: <\">");

        //wrong opening bracket
        testParseExceptionWithMessage(UnexpectedCharacterException.class, JSONObjectNodeParser::new, "[\"%s\": %d, \"%s\": %d, \"%s\": %d}"
                .formatted(k1, v1, k2, v2, k3, v3), elementNodeParser -> mockObjectEntryParser(elementNodeParser, new String[]{k1, k2, k3}, new Integer[]{v1, v2, v3}),
            "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <{>, but was: <[>");

        //missing closing bracket
        testParseException(BadFileEndingException.class, JSONObjectNodeParser::new, "{\"%s\": %d, \"%s\": %d, \"%s\": %d"
            .formatted(k1, v1, k2, v2, k3, v3), elementNodeParser -> mockObjectEntryParser(elementNodeParser, new String[]{k1, k2, k3}, new Integer[]{v1, v2, v3}));

        //wrong closing bracket
        testParseExceptionWithMessage(UnexpectedCharacterException.class, JSONObjectNodeParser::new, "{\"%s\": %d, \"%s\": %d, \"%s\": %d]"
                .formatted(k1, v1, k2, v2, k3, v3), elementNodeParser -> mockObjectEntryParser(elementNodeParser, new String[]{k1, k2, k3}, new Integer[]{v1, v2, v3}),
            "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <,>, but was: <]>",
            "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <}>, but was: <]>");

        //missing closing bracket, no elements
        testParseException(BadFileEndingException.class, JSONObjectNodeParser::new, "{");

        //trailing comma
        testParseException(TrailingCommaException.class, JSONObjectNodeParser::new, "{\"%s\": %d, \"%s\": %d, \"%s\": %d,}"
            .formatted(k1, v1, k2, v2, k3, v3), elementNodeParser -> mockObjectEntryParser(elementNodeParser, new String[]{k1, k2, k3}, new Integer[]{v1, v2, v3}));

        //missing comma
        testParseExceptionWithMessage(UnexpectedCharacterException.class, JSONObjectNodeParser::new,
            "{\"%s\": %d, \"%s\": %d \"%s\": %d}".formatted(k1, v1, k2, v2, k3, v3), elementNodeParser -> mockObjectEntryParser(elementNodeParser, new String[]{k1, k2, k3}, new Integer[]{v1, v2, v3}),
            "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <,>, but was: <\">",
            "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <}>, but was: <\">");

        //wrong comma
        testParseExceptionWithMessage(UnexpectedCharacterException.class, JSONObjectNodeParser::new,
            "{\"%s\": %d, \"%s\": %d; \"%s\": %d}".formatted(k1, v1, k2, v2, k3, v3), elementNodeParser -> mockObjectEntryParser(elementNodeParser, new String[]{k1, k2, k3}, new Integer[]{v1, v2, v3}),
            "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <,>, but was: <;>",
            "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <}>, but was: <;>");
    }

}
