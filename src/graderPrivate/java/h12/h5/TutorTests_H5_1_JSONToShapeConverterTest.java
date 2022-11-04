package h12.h5;

import h12.exceptions.JSONParseException;
import h12.gui.shapes.ColorHelper;
import h12.gui.shapes.JSONToShapeConverter;
import h12.gui.shapes.MyPolygon;
import h12.gui.shapes.ShapeType;
import h12.json.JSONArray;
import h12.json.JSONNumber;
import h12.json.JSONObject;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.implementation.node.JSONStringNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import java.awt.*;

import static h12.json.JSONObject.JSONObjectEntry;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertThrows;

@TestForSubmission()
public class TutorTests_H5_1_JSONToShapeConverterTest {

    @ParameterizedTest
    @CsvSource("1, 2, 3, 4, #FF0000, #00FF00, 1")
    public void testFromJSONSuccess(Integer ax, Integer ay, Integer bx, Integer by, String fillColorCode, String borderColorCode, Integer edges) {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("values", "%d, %d, %d, %d, %s, %s, %d".formatted(ax, ay, bx, by, fillColorCode, borderColorCode, edges))
            .subject("MyPolygon#polygonFromJSON(JSONElement)")
            .build();

        Color fillColor = Color.decode(fillColorCode);
        Color borderColor = Color.decode(borderColorCode);
        MyPolygon expected = new MyPolygon(java.util.List.of(ax, bx), java.util.List.of(ay, by), fillColor, borderColor, edges);

        JSONObject input = JSONObject.of(
            JSONObjectEntry.of("name", new JSONStringNode(ShapeType.POLYGON.getSpelling())),
            JSONObjectEntry.of("edges", new JSONNumberNode(edges)),
            JSONObjectEntry.of("x", JSONArray.of(JSONNumber.of(ax), JSONNumber.of(bx))),
            JSONObjectEntry.of("y", JSONArray.of(JSONNumber.of(ay), JSONNumber.of(by))),
            JSONObjectEntry.of("fillColor", ColorHelper.toJSON(fillColor)),
            JSONObjectEntry.of("borderColor", ColorHelper.toJSON(borderColor))
        );

        MyPolygon actual = new JSONToShapeConverter().polygonFromJSON(input);

        assertEquals(expected, actual, context, TR -> "The method did not return the correct value");
    }

    @Test
    public void testFromJSONInvalidFormat() {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("values", "invalid Format")
            .subject("MyPolygon#polygonFromJSON(JSONElement)")
            .build();

        JSONObject emptyInput = JSONObject.of();

        assertThrows(JSONParseException.class, () -> new JSONToShapeConverter().polygonFromJSON(emptyInput), context,
            TR -> "The method did not throw the correct exception when the input is invalid");

        try {
            new JSONToShapeConverter().polygonFromJSON(emptyInput);
        } catch (JSONParseException exc) {
            assertEquals("An exception occurred while trying to parse a JSON file. Invalid MyShape format!", exc.getMessage(),
                context, TR -> "The thrown exception does not contain the correct message");
        }

        JSONNumberNode invalidTypeInput = new JSONNumberNode(1);

        assertThrows(JSONParseException.class, () -> new JSONToShapeConverter().polygonFromJSON(invalidTypeInput), context,
            TR -> "The method did not throw the correct exception when the input is invalid");

        try {
            new JSONToShapeConverter().polygonFromJSON(emptyInput);
        } catch (JSONParseException exc) {
            assertEquals("An exception occurred while trying to parse a JSON file. Invalid MyShape format!", exc.getMessage(),
                context, TR -> "The thrown exception does not contain the correct message");
        }
    }

}
