package h12.h2;

import h12.json.implementation.node.JSONNumberNode;
import h12.json.implementation.node.JSONStringNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import static h12.json.JSONObject.JSONObjectEntry;
import static h12.json.implementation.node.JSONObjectNode.JSONObjectEntryNode;

@TestForSubmission
public class TutorTests_H2_WriteJSONObjectEntryTest extends TutorTests_WriteJSONTest {

    @ParameterizedTest
    @CsvSource("a, 1")
    public void testWriteJSONObjectEntry(String key, Integer value) throws Throwable {
        JSONStringNode mockedJSONString = createMockedJSONString(key);
        JSONNumberNode mockedJSONNumber = createMockedJSONNumber(value);
        JSONObjectEntry entry = new JSONObjectEntryNode(mockedJSONString, mockedJSONNumber);
        String expected = "\"" + key + "\": " + value;

        testWriteJSONNode(entry, expected, 1, createVerifier(mockedJSONString, 1)
            .andThen(createVerifier(mockedJSONNumber, 1)));
    }

}
