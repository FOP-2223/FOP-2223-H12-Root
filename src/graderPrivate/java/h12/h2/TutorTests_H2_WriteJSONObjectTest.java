package h12.h2;

import h12.json.JSONObject;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.implementation.node.JSONObjectNode;
import h12.json.implementation.node.JSONStringNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static h12.json.JSONObject.JSONObjectEntry;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@SuppressWarnings("DuplicatedCode")
@TestForSubmission
public class TutorTests_H2_WriteJSONObjectTest extends TutorTests_WriteJSONTest {

    @ParameterizedTest
    @CsvSource("a, 1, b, 2, c, 3, 1")
    public void testWriteJSONObjectNoIndent(String k1, int v1, String k2, int v2, String k3, int v3) throws IOException {
        JSONObjectNode.JSONObjectEntryNode mockedJSONObjectEntry1 = createMockedJSONObjectEntry(k1, v1, null);
        JSONObjectNode.JSONObjectEntryNode mockedJSONObjectEntry2 = createMockedJSONObjectEntry(k2, v2, null);
        JSONObjectNode.JSONObjectEntryNode mockedJSONObjectEntry3 = createMockedJSONObjectEntry(k3, v3, null);

        JSONObject object = JSONObject.of(
            mockedJSONObjectEntry1,
            mockedJSONObjectEntry2,
            mockedJSONObjectEntry3
        );

        Context context = contextBuilder()
            .add("input", "{\n\"%s\": %d,\n\"%s\": %d,\n\"%s\": %d\n}"
                .formatted(k1, v1, k2, v2, k3, v3))
            .subject("JSONObjectNode#write(BufferedWriter, int)")
            .build();

        String actual = removeWhiteSpace(getActual(object, 0, context));

        createVerifier(mockedJSONObjectEntry1, 1)
            .andThen(createVerifier(mockedJSONObjectEntry2, 1))
            .andThen(createVerifier(mockedJSONObjectEntry3, 1))
            .accept(context);

        String expected = "";

        List<JSONObjectEntry> entries = new ArrayList<>(object.getObjectEntries());
        for (List<JSONObjectEntry> permutation : getAllPermutations(entries)) { //the order of the elements is not relevant
            expected = removeWhiteSpace("{\n\"%s\": %d,\n\"%s\": %d,\n\"%s\": %d\n}".formatted(
                permutation.get(0).getIdentifier(), permutation.get(0).getValue().getInteger(),
                permutation.get(1).getIdentifier(), permutation.get(1).getValue().getInteger(),
                permutation.get(2).getIdentifier(), permutation.get(2).getValue().getInteger()
            ));

            if (expected.equals(actual)) return;
        }

        context = contextBuilder()
            .add("input", expected)
            .subject("JSONObjectNode#write(BufferedWriter, int)")
            .build();

        String message = "Methode did not write the correct String to the BufferedWriter.";

        message += "\nNote that the order of the object entries does not matter";

        String finalMessage = message;
        assertEquals(createJSONString(expected), createJSONString(actual), context, TR -> finalMessage);
    }

    @ParameterizedTest
    @CsvSource("a, 1, b, 2, c, 3, 1")
    public void testWriteJSONObject(String k1, int v1, String k2, int v2, String k3, int v3, int indentation) throws IOException {
        ArgumentCaptor<Integer> JSONObjectEntryIndentCaptor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> JSONObjectEntryIndentCaptor2 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> JSONObjectEntryIndentCaptor3 = ArgumentCaptor.forClass(Integer.class);

        JSONObjectNode.JSONObjectEntryNode mockedJSONObjectEntry1 = createMockedJSONObjectEntry(k1, v1, JSONObjectEntryIndentCaptor1);
        JSONObjectNode.JSONObjectEntryNode mockedJSONObjectEntry2 = createMockedJSONObjectEntry(k2, v2, JSONObjectEntryIndentCaptor2);
        JSONObjectNode.JSONObjectEntryNode mockedJSONObjectEntry3 = createMockedJSONObjectEntry(k3, v3, JSONObjectEntryIndentCaptor3);

        JSONObject object = JSONObject.of(
            mockedJSONObjectEntry1,
            mockedJSONObjectEntry2,
            mockedJSONObjectEntry3
        );

        StringBuilder entryIndentation = new StringBuilder("  ");
        StringBuilder endIndentation = new StringBuilder();

        for (int i = 0; i < indentation; i++) {
            entryIndentation.append("  ");
            endIndentation.append("  ");
        }

        Context context = contextBuilder()
            .add("input", "{\n%s\"%s\": %d,\n%s\"%s\": %d,\n%s\"%s\": %d\n%s}"
                .formatted(entryIndentation, k1, v1, entryIndentation, k2, v2, entryIndentation, k3, v3, endIndentation))
            .add("indentation", indentation)
            .subject("JSONObjectNode#write(BufferedWriter, int)")
            .build();

        String actual = getActual(object, indentation, context);

        createVerifier(mockedJSONObjectEntry1, 1)
            .andThen(createVerifier(mockedJSONObjectEntry2, 1))
            .andThen(createVerifier(mockedJSONObjectEntry3, 1))
            .andThen(createIndentVerifier(2, JSONObjectEntryIndentCaptor1, mockedJSONObjectEntry1))
            .andThen(createIndentVerifier(2, JSONObjectEntryIndentCaptor2, mockedJSONObjectEntry2))
            .andThen(createIndentVerifier(2, JSONObjectEntryIndentCaptor3, mockedJSONObjectEntry3))
            .accept(context);

        boolean withoutWhitespaceEqual = false;
        String closestExpected = null;

        List<JSONObjectEntry> entries = new ArrayList<>(object.getObjectEntries());
        for (List<JSONObjectEntry> permutation : getAllPermutations(entries)) { //the order of the elements is not relevant
            String expected = "{\n%s\"%s\": %d,\n%s\"%s\": %d,\n%s\"%s\": %d\n%s}".formatted(
                entryIndentation, permutation.get(0).getIdentifier(), permutation.get(0).getValue().getInteger(),
                entryIndentation, permutation.get(1).getIdentifier(), permutation.get(1).getValue().getInteger(),
                entryIndentation, permutation.get(2).getIdentifier(), permutation.get(2).getValue().getInteger(),
                endIndentation
            );

            if (expected.equals(actual)) return;

            if (expected.replace(" ", "").replace("\n", "")
                .equals(actual.replace(" ", "").replace("\n", ""))) {
                withoutWhitespaceEqual = true;
                closestExpected = expected;
            }

            if (!withoutWhitespaceEqual) {
                closestExpected = expected;
            }
        }

        context = contextBuilder()
            .add("input", closestExpected)
            .add("indentation", indentation)
            .subject("JSONObjectNode#write(BufferedWriter, int)")
            .build();

        String message = "Methode did not write the correct String to the BufferedWriter.";

        if (withoutWhitespaceEqual) {
            message += " The contents of the expected and actual json element are equal but the whitespaces do not match.";
        }

        message += "\nNote that the order of the object entries does not matter";

        String finalMessage = message;
        assertEquals(createJSONString(closestExpected), createJSONString(actual), context, TR -> finalMessage);
    }

    private static <T> List<List<T>> getAllPermutations(List<T> input) {
        if (input.size() <= 1) return new ArrayList<>(List.of(input));

        List<List<T>> result = new ArrayList<>();
        for (T entry : input) {
            List<T> copy = new ArrayList<>(input);
            copy.remove(entry);
            List<List<T>> permutations = getAllPermutations(copy);
            for (List<T> permutation : permutations) {
                permutation.add(0, entry);
                result.add(permutation);
            }
        }
        return result;
    }

}


