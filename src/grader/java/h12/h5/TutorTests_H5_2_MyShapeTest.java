package h12.h5;

import h12.exceptions.JSONParseException;
import h12.gui.shapes.*;
import h12.json.JSONObject;
import h12.json.JSONString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import static h12.json.JSONObject.JSONObjectEntry;
import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertThrows;

@TestForSubmission()
public class TutorTests_H5_2_MyShapeTest {

    @ParameterizedTest
    @CsvSource({"rectangle", "circle", "custom_line", "polygon"})
    public void testFromJSONSuccess(String name) {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("name", name)
            .subject("MyShape.fromJSON()")
            .build();

        JSONToShapeConverter jsonToShapeConverter = mock(JSONToShapeConverter.class);
        MyShape.setJsonToShapeConverter(jsonToShapeConverter);

        JSONObject input = JSONObject.of(JSONObjectEntry.of("name", JSONString.of(name)));
        MyShape shape = null;

        switch (name) {
            case "rectangle" -> {
                shape = mock(MyRectangle.class);
                doReturn(shape).when(jsonToShapeConverter).rectangleFromJSON(any());
            }
            case "circle" -> {
                shape = mock(MyCircle.class);
                doReturn(shape).when(jsonToShapeConverter).circleFromJSON(any());
            }
            case "custom_line" -> {
                shape = mock(CustomLine.class);
                doReturn(shape).when(jsonToShapeConverter).customLineFromJSON(any());
            }
            case "polygon" -> {
                shape = mock(MyPolygon.class);
                doReturn(shape).when(jsonToShapeConverter).polygonFromJSON(any());
            }
        }

        MyShape actual = MyShape.fromJSON(input);

        assertEquals(shape, actual, context,
            TR -> "The method did not return the correct value");

        if (!name.equals("rectangle")) {
            verify(jsonToShapeConverter, never()).rectangleFromJSON(any());
        }
        if (!name.equals("circle")) {
            verify(jsonToShapeConverter, never()).circleFromJSON(any());
        }
        if (!name.equals("custom_line")) {
            verify(jsonToShapeConverter, never()).customLineFromJSON(any());
        }
        if (!name.equals("polygon")) {
            verify(jsonToShapeConverter, never()).polygonFromJSON(any());
        }
    }

    @ParameterizedTest
    @CsvSource({"invalidType", "triangle", "straight_line"})
    public void testFromJSONInvalidShapeType(String invalidType) {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("name", invalidType)
            .subject("MyShape.fromJSON(JSONElement)")
            .build();

        JSONObject input = JSONObject.of(JSONObjectEntry.of("name", JSONString.of(invalidType)));

        assertThrows(JSONParseException.class, () -> MyShape.fromJSON(input), context,
            TR -> "The method fromJSON(JSONElement) did not throw the correct exception when given an invalid shapeType");

        try {
            MyShape.fromJSON(input);
        } catch (JSONParseException exc) {
            assertEquals("An exception occurred while trying to parse a JSON file. Invalid shape type: %s!".formatted(invalidType),
                exc.getMessage(), context, TR -> "The thrown exception does not contain the correct message");
        }
    }

    @Test
    public void testFromJSONInvalidFormat() {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("input", "invalid format")
            .subject("MyShape.fromJSON(JSONElement)")
            .build();

        JSONObject input = JSONObject.of();

        assertThrows(JSONParseException.class, () -> MyShape.fromJSON(input), context,
            TR -> "The method fromJSON(JSONElement) did not throw the correct exception when given an input with an invalid format");

        try {
            MyShape.fromJSON(input);
        } catch (JSONParseException exc) {
            assertEquals("An exception occurred while trying to parse a JSON file. Invalid MyShape format!",
                exc.getMessage(), context, TR -> "The thrown exception does not contain the correct message");
        }

        JSONToShapeConverter jsonToShapeConverter = mock(JSONToShapeConverter.class);
        MyShape.setJsonToShapeConverter(jsonToShapeConverter);

        JSONObject input2 = JSONObject.of(JSONObjectEntry.of("name", JSONString.of("circle")));

        doThrow(new JSONParseException("Invalid MyShape format!")).when(jsonToShapeConverter).circleFromJSON(any());

        assertThrows(JSONParseException.class, () -> MyShape.fromJSON(input2), context,
            TR -> "The method fromJSON(JSONElement) did not throw the correct exception when given an input with an invalid format");

        try {
            MyShape.fromJSON(input2);
        } catch (JSONParseException exc) {
            assertEquals("An exception occurred while trying to parse a JSON file. Invalid MyShape format!",
                exc.getMessage(), context, TR -> "The thrown exception does not contain the correct message");
        }
    }

}
