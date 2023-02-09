package h12.h3;

import h12.exceptions.BadFileEndingException;
import h12.exceptions.TrailingCommaException;
import h12.exceptions.UnexpectedCharacterException;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.parser.implementation.node.JSONArrayNodeParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.io.IOException;
import java.util.List;

@TestForSubmission
public class TutorTests_H3_3_JSONArrayNodeParserTest extends TutorTests_JSONParseTest {

    @ParameterizedTest
    @CsvSource("1, 2, 3, }") //single digit
    public void testParseSuccess(Integer v1, Integer v2, Integer v3, String extension) throws Throwable {
        testParseSuccess(JSONArrayNodeParser::new,
            "[%d, %d, %d]%s".formatted(v1, v2, v3, extension),
            List.of(new JSONNumberNode(v1), new JSONNumberNode(v2), new JSONNumberNode(v3)),
            extension,
            element -> List.of(element.getArray()),
            this::mockNumberParser,
            createElementParserVerifier(3));
    }

    @ParameterizedTest
    @CsvSource("1, 2, 3") //single digit
    public void testParseException(Integer v1, Integer v2, Integer v3) throws IOException {
        //missing opening bracket
        testParseExceptionWithMessage(UnexpectedCharacterException.class, JSONArrayNodeParser::new, "%d, %d, %d]".formatted(v1, v2, v3),
            this::mockNumberParser, "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <[>, but was: <%d>".formatted(v1));

        //wrong opening bracket
        testParseExceptionWithMessage(UnexpectedCharacterException.class, JSONArrayNodeParser::new, "{%d, %d, %d]".formatted(v1, v2, v3),
            this::mockNumberParser, "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <[>, but was: <{>");

        //missing closing bracket
        testParseException(BadFileEndingException.class, JSONArrayNodeParser::new, "[%d, %d, %d".formatted(v1, v2, v3),
            this::mockNumberParser);

        //wrong closing bracket
        testParseExceptionWithMessage(UnexpectedCharacterException.class, JSONArrayNodeParser::new, "[%d, %d, %d}".formatted(v1, v2, v3),
            this::mockNumberParser, "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <,>, but was: <}>",
            "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <]>, but was: <}>");

        //missing closing bracket, no elements
        testParseException(BadFileEndingException.class, JSONArrayNodeParser::new, "[");

        //trailing comma
        testParseException(TrailingCommaException.class, JSONArrayNodeParser::new, "[%d, %d, %d,]".formatted(v1, v2, v3),
            this::mockNumberParser);

        //missing comma
        testParseExceptionWithMessage(UnexpectedCharacterException.class, JSONArrayNodeParser::new, "[%d, %d %d]".formatted(v1, v2, v3),
            this::mockNumberParser, "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <,>, but was: <%d>".formatted(v3),
            "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <]>, but was: <%d>".formatted(v3));

        //wrong comma
        testParseExceptionWithMessage(UnexpectedCharacterException.class, JSONArrayNodeParser::new, "[%d, %d; %d]".formatted(v1, v2, v3),
            this::mockNumberParser, "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <,>, but was: <;>",
            "An exception occurred while trying to parse a JSON file. Received an unexpected character. Expected: <]>, but was: <;>");
    }

}
