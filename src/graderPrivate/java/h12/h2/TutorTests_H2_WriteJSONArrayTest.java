package h12.h2;

import h12.json.JSONArray;
import h12.json.implementation.node.JSONNumberNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

@TestForSubmission
public class TutorTests_H2_WriteJSONArrayTest extends TutorTests_WriteJSONTest {

    @ParameterizedTest
    @CsvSource("1, 2, 3")
    public void testWriteJSONArray(int v1, int v2, int v3) throws Throwable {
        JSONNumberNode mockedJSONNumber1 = createMockedJSONNumber(v1);
        JSONNumberNode mockedJSONNumber2 = createMockedJSONNumber(v2);
        JSONNumberNode mockedJSONNumber3 = createMockedJSONNumber(v3);
        JSONArray array = JSONArray.of(mockedJSONNumber1, mockedJSONNumber2, mockedJSONNumber3);
        String expected = "[\n    %d,\n    %d,\n    %d\n  ]".formatted(v1, v2, v3);
        testWriteJSONNode(array, expected, 1, createVerifier(mockedJSONNumber1, 1)
            .andThen(createVerifier(mockedJSONNumber2, 1).andThen(createVerifier(mockedJSONNumber3, 1))));
    }
}
