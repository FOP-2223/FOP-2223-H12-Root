package h12.h3;

import h12.exceptions.InvalidConstantException;
import h12.json.JSONConstants;
import h12.json.JSONElement;
import h12.json.parser.implementation.node.JSONConstantNodeParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.io.IOException;

@TestForSubmission()
public class TutorTests_H3_3_JSONConstantNodeParserTest extends TutorTests_JSONParseTest {

    @ParameterizedTest
    @CsvSource("}")
    public void testParseSuccess(String nonAlphabeticExtension) throws Throwable {
        for (JSONConstants constant : JSONConstants.values()) {
            testParseSuccess(JSONConstantNodeParser::new,
                constant.getSpelling() + nonAlphabeticExtension,
                constant,
                nonAlphabeticExtension,
                JSONElement::getConstant);
        }
    }

    @ParameterizedTest
    @CsvSource("invalidConstant")
    public void testParseException(String input) throws IOException {
        testParseException(InvalidConstantException.class, JSONConstantNodeParser::new, input);
    }


}
