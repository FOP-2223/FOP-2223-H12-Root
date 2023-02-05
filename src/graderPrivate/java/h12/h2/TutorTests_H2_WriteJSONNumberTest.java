package h12.h2;

import h12.json.JSONNumber;
import h12.json.implementation.node.JSONNumberNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

@TestForSubmission
public class TutorTests_H2_WriteJSONNumberTest extends TutorTests_WriteJSONTest {

    @ParameterizedTest
    @CsvSource("-1")
    public void testWriteJSONInteger(Integer input) throws Throwable {
        JSONNumber number = new JSONNumberNode(input);
        String expected = input.toString();

        testWriteJSONNode(number, expected, 1);
    }

    @ParameterizedTest
    @CsvSource("-1.2")
    public void testWriteJSONDouble(Double input) throws Throwable {
        JSONNumber number = new JSONNumberNode(input);
        String expected = input.toString();

        testWriteJSONNode(number, expected, 1);
    }
}
