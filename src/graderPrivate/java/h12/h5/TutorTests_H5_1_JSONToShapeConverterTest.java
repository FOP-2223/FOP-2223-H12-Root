package h12.h5;

import h12.exceptions.JSONParseException;
import h12.gui.shapes.*;
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
import java.lang.reflect.Field;

import static h12.json.JSONObject.JSONObjectEntry;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission()
public class TutorTests_H5_1_JSONToShapeConverterTest {

    @ParameterizedTest
    @CsvSource("1, 2, 3, 4, #FF0000, #00FF00, 1")
    public void testFromJSONSuccess(Integer ax, Integer ay, Integer bx, Integer by, String fillColorCode, String borderColorCode, Integer edges) throws NoSuchFieldException, IllegalAccessException {
        Context context = contextBuilder()
            .add("x", "[%d, %d]".formatted(ax, bx))
            .add("y", "[%d, %d]".formatted(ay, by))
            .add("fillColor", fillColorCode)
            .add("borderColor", borderColorCode)
            .add("edges", edges)
            .subject("MyPolygon#polygonFromJSON(JSONElement)")
            .build();

        Color fillColor = Color.decode(fillColorCode);
        Color borderColor = Color.decode(borderColorCode);

        JSONObject input = JSONObject.of(
            JSONObjectEntry.of("name", new JSONStringNode(ShapeType.POLYGON.getSpelling())),
            JSONObjectEntry.of("edges", new JSONNumberNode(edges)),
            JSONObjectEntry.of("x", JSONArray.of(JSONNumber.of(ax), JSONNumber.of(bx))),
            JSONObjectEntry.of("y", JSONArray.of(JSONNumber.of(ay), JSONNumber.of(by))),
            JSONObjectEntry.of("fillColor", ColorHelper.toJSON(fillColor)),
            JSONObjectEntry.of("borderColor", ColorHelper.toJSON(borderColor))
        );

        MyPolygon actual = callObject(() -> new JSONToShapeConverter().polygonFromJSON(input), context,
            TR -> "Unexpected exception was thrown");

        Field xField = MyPolygon.class.getDeclaredField("x");
        xField.setAccessible(true);
        assertEquals(java.util.List.of(ax, bx), xField.get(actual), context, TR -> "The method did not return the correct x values");

        Field yField = MyPolygon.class.getDeclaredField("y");
        yField.setAccessible(true);
        assertEquals(java.util.List.of(ay, by), yField.get(actual), context, TR -> "The method did not return the correct y values");

        Field fillColorField = MyShape.class.getDeclaredField("fillColor");
        fillColorField.setAccessible(true);
        assertEquals(fillColor, fillColorField.get(actual), context, TR -> "The method did not return the correct fillColor");

        Field borderColorField = MyShape.class.getDeclaredField("borderColor");
        borderColorField.setAccessible(true);
        assertEquals(borderColor, borderColorField.get(actual), context, TR -> "The method did not return the correct borderColor");

        Field edgesField = MyPolygon.class.getDeclaredField("edges");
        edgesField.setAccessible(true);
        assertEquals(edges, edgesField.get(actual), context, TR -> "The method did not return the correct edges values");
    }

    @Test
    public void testFromJSONInvalidFormat() {
        Context contextEmpty = contextBuilder()
            .add("values", "empty")
            .subject("MyPolygon#polygonFromJSON(JSONElement)")
            .build();

        JSONObject emptyInput = JSONObject.of();

        assertThrows(JSONParseException.class, () -> new JSONToShapeConverter().polygonFromJSON(emptyInput), contextEmpty,
            TR -> "The method did not throw the correct exception when the input is invalid");

        try {
            new JSONToShapeConverter().polygonFromJSON(emptyInput);
        } catch (JSONParseException exc) {
            assertEquals("An exception occurred while trying to parse a JSON file. Invalid MyShape format!", exc.getMessage(),
                contextEmpty, TR -> "The thrown exception does not contain the correct message");
        }

        Context contextInvalidType = contextBuilder()
            .add("values", "invalid Type")
            .subject("MyPolygon#polygonFromJSON(JSONElement)")
            .build();

        JSONNumberNode invalidTypeInput = new JSONNumberNode(1);

        assertThrows(JSONParseException.class, () -> new JSONToShapeConverter().polygonFromJSON(invalidTypeInput), contextInvalidType,
            TR -> "The method did not throw the correct exception when the input is invalid");

        try {
            new JSONToShapeConverter().polygonFromJSON(emptyInput);
        } catch (JSONParseException exc) {
            assertEquals("An exception occurred while trying to parse a JSON file. Invalid MyShape format!", exc.getMessage(),
                contextInvalidType, TR -> "The thrown exception does not contain the correct message");
        }
    }

}
