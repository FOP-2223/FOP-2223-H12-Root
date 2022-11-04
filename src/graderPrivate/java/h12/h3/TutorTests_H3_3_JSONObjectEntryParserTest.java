package h12.h3;

import h12.exceptions.BadFileEndingException;
import h12.exceptions.UnexpectedCharacterException;
import h12.json.JSONNumber;
import h12.json.parser.implementation.node.JSONObjectEntryNodeParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.io.IOException;
import java.util.List;

import static h12.json.JSONObject.JSONObjectEntry;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestForSubmission()
public class TutorTests_H3_3_JSONObjectEntryParserTest extends TutorTests_JSONParseTest {

    @ParameterizedTest
    @CsvSource("a, 1, }") //single digit
    public void testParseSuccess(String k1, Integer v1, String extension) throws Throwable {
        testParseSuccess(JSONObjectEntryNodeParser::new,
            "\"%s\": %d%s".formatted(k1, v1, extension),
            List.of(k1, JSONNumber.of(v1)),
            extension,
            element -> List.of(((JSONObjectEntry) element).getIdentifier(), ((JSONObjectEntry) element).getValue()),
            elementNodeParser -> mockStringParser(elementNodeParser, new String[]{k1}),
            elementNodeParser -> verify(elementNodeParser.getStringParser(), times(1)).parse());
    }

    @ParameterizedTest
    @CsvSource("a, 1") //single digit
    public void testParseException(String k1, Integer v1) throws IOException {

        //missing value
        testParseException(BadFileEndingException.class, JSONObjectEntryNodeParser::new, "\"%s\": ".formatted(k1));

        //missing colon
        testParseException(UnexpectedCharacterException.class, JSONObjectEntryNodeParser::new, "\"%s\" %d".formatted(k1, v1));
    }
}
