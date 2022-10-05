package h12.h2;

import h12.json.JSONString;
import h12.json.implementation.node.JSONStringNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

@TestForSubmission()
public class TutorTests_H2_WriteJSONStringTest extends TutorTests_WriteJSONTest {

    @ParameterizedTest
    @CsvSource("Hello World!")
    public void testWriteJSONString(String input) throws Throwable {
        JSONString string = new JSONStringNode(input);
        String expected = "\"" + input + "\"";

        testWriteJSONNode(string, expected, 1, null);
    }

}
