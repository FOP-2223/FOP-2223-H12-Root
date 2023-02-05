package h12.h3;

import h12.exceptions.InvalidNumberException;
import h12.json.JSONElement;
import h12.json.parser.implementation.node.JSONNumberNodeParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.io.IOException;
import java.util.function.Function;

@TestForSubmission
public class TutorTests_H3_3_JSONNumberNodeParserTest extends TutorTests_JSONParseTest {

    @ParameterizedTest
    @CsvSource("1, abc")
    public void testParseSuccess(String input, String alphabeticExtension) throws Throwable {
        testParseVariantsSuccess(input, alphabeticExtension, Integer::parseInt);
        testParseVariantsSuccess("." + input, alphabeticExtension, Double::parseDouble);
        testParseVariantsSuccess(input + "." + input, alphabeticExtension, Double::parseDouble);
    }

    public void testParseVariantsSuccess(String input, String alphabeticExtension, Function<String, Number> numberParser) throws Throwable {
        testParseSuccess(JSONNumberNodeParser::new,
            input + alphabeticExtension,
            numberParser.apply(input),
            alphabeticExtension,
            JSONElement::getNumber
        );
        testParseSuccess(JSONNumberNodeParser::new,
            "+" + input + alphabeticExtension,
            numberParser.apply(input),
            alphabeticExtension,
            JSONElement::getNumber
        );
        testParseSuccess(JSONNumberNodeParser::new,
            "-" + input + alphabeticExtension,
            numberParser.apply("-" + input),
            alphabeticExtension,
            JSONElement::getNumber
        );
    }

    @ParameterizedTest
    @CsvSource("1.1.1, 1.1-, ++1.0, 1+")
    public void testParseException(String input) throws IOException {
        testParseException(InvalidNumberException.class, JSONNumberNodeParser::new, input);
    }
}
