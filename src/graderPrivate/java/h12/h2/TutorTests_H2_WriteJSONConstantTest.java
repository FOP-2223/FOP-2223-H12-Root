package h12.h2;

import h12.json.JSONConstant;
import h12.json.JSONConstants;
import h12.json.implementation.node.JSONConstantNode;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

@TestForSubmission()
public class TutorTests_H2_WriteJSONConstantTest extends TutorTests_WriteJSONTest {

    @Test
    public void testWriteJSONConstant() throws Throwable {
        for (JSONConstants constant : JSONConstants.values()) {
            JSONConstant jsonConstant = new JSONConstantNode(constant);
            String expected = constant.getSpelling();

            testWriteJSONNode(jsonConstant, expected, 1, null);
        }
    }

}
