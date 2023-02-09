package h12.h2;

import h12.json.JSONArray;
import h12.json.implementation.node.JSONNumberNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestForSubmission
public class TutorTests_H2_WriteJSONArrayTest extends TutorTests_WriteJSONTest {

    @ParameterizedTest
    @CsvSource("1, 2, 3")
    public void testWriteJSONArrayNoIndent(int v1, int v2, int v3) throws Throwable {
        JSONNumberNode mockedJSONNumber1 = createMockedJSONNumber(v1, null);
        JSONNumberNode mockedJSONNumber2 = createMockedJSONNumber(v2, null);
        JSONNumberNode mockedJSONNumber3 = createMockedJSONNumber(v3, null);
        JSONArray array = JSONArray.of(mockedJSONNumber1, mockedJSONNumber2, mockedJSONNumber3);
        String expected = "[\n    %d,\n    %d,\n    %d\n  ]".formatted(v1, v2, v3);
        testWriteJSONNodeNoIndent(array, expected, createVerifier(mockedJSONNumber1, 1)
            .andThen(createVerifier(mockedJSONNumber2, 1))
            .andThen(createVerifier(mockedJSONNumber3, 1)));
    }

    @ParameterizedTest
    @CsvSource("1, 2, 3")
    public void testWriteJSONArray(int v1, int v2, int v3) throws Throwable {
        ArgumentCaptor<Integer> JSONElementIndentCaptor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> JSONElementIndentCaptor2 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> JSONElementIndentCaptor3 = ArgumentCaptor.forClass(Integer.class);

        JSONNumberNode mockedJSONNumber1 = createMockedJSONNumber(v1, JSONElementIndentCaptor1);
        JSONNumberNode mockedJSONNumber2 = createMockedJSONNumber(v2, JSONElementIndentCaptor2);
        JSONNumberNode mockedJSONNumber3 = createMockedJSONNumber(v3, JSONElementIndentCaptor3);

        JSONArray array = JSONArray.of(mockedJSONNumber1, mockedJSONNumber2, mockedJSONNumber3);

        String expected = "[\n    %d,\n    %d,\n    %d\n  ]".formatted(v1, v2, v3);
        testWriteJSONNode(array, expected, 1, createVerifier(mockedJSONNumber1, 1)
            .andThen(createVerifier(mockedJSONNumber2, 1))
            .andThen(createVerifier(mockedJSONNumber3, 1))
            .andThen(createIndentVerifier(2, JSONElementIndentCaptor1, mockedJSONNumber1))
            .andThen(createIndentVerifier(2, JSONElementIndentCaptor2, mockedJSONNumber2))
            .andThen(createIndentVerifier(2, JSONElementIndentCaptor3, mockedJSONNumber3))
        );
    }
}
