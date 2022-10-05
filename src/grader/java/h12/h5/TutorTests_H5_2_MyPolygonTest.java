package h12.h5;

import h12.gui.shapes.ColorHelper;
import h12.gui.shapes.MyPolygon;
import h12.json.JSONElement;
import h12.json.JSONObject;
import h12.json.implementation.node.JSONArrayNode;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.implementation.node.JSONStringNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import java.awt.*;
import java.util.List;

import static h12.json.JSONObject.JSONObjectEntry;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;

@SuppressWarnings("Duplicates")
@TestForSubmission()
public class TutorTests_H5_2_MyPolygonTest {

    @ParameterizedTest
    @CsvSource("1, 2, 3, 4, #FF0000, #00FF00, 1")
    public void testToJSON(Integer ax, Integer ay, Integer bx, Integer by, String fillColorCode, String borderColorCode, Integer edges) {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("values", "%d, %d, %d, %d, %s, %s, %d".formatted(ax, ay, bx, by, fillColorCode, borderColorCode, edges))
            .subject("MyPolygon#toJSON()")
            .build();

        Color fillColor = Color.decode(fillColorCode);
        Color borderColor = Color.decode(borderColorCode);

        MyPolygon circle = new MyPolygon(List.of(ax, ay), List.of(bx, by), fillColor, borderColor, edges);
        JSONObject actual = circle.toJSON();

        String[] keys = new String[]{"name", "x", "y", "fillColor", "borderColor", "edges"};
        JSONElement[] values = new JSONElement[]{
            new JSONStringNode("polygon"),
            new JSONArrayNode(List.of(new JSONNumberNode(ax), new JSONNumberNode(ay))),
            new JSONArrayNode(List.of(new JSONNumberNode(bx), new JSONNumberNode(by))),
            ColorHelper.toJSON(fillColor),
            ColorHelper.toJSON(borderColor),
            new JSONNumberNode(edges)
        };

        assertEquals(keys.length, actual.getObjectEntries().size(), context,
            TR -> "The JSONObject returned by the method does not contain the correct amount of entries");

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            Object value = values[i];

            assertTrue(actual.getObjectEntries().contains(JSONObjectEntry.of(key, null)),
                context, TR -> "The JSONObject returned by the method does not contain the key %s".formatted(key));

            assertEquals(value, actual.getValueOf(key), context,
                TR -> "The JSONObject returned by the method does not contain the correct value for the key %s".formatted(key));
        }
    }

}
