package h12.h5;

import h12.exceptions.JSONParseException;
import h12.gui.components.LoadCanvasHandler;
import h12.gui.shapes.ColorHelper;
import h12.gui.shapes.MyCircle;
import h12.gui.shapes.MyShape;
import h12.ioFactory.FileSystemIOFactory;
import h12.ioFactory.IOFactory;
import h12.json.JSON;
import h12.json.JSONArray;
import h12.json.JSONElement;
import h12.json.JSONObject;
import h12.json.implementation.node.JSONNumberNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.MockedStatic;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

import static h12.json.JSONObject.JSONObjectEntry;
import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@SuppressWarnings("Duplicates")
@TestForSubmission()
public class TutorTests_H5_5_LoadCanvasHandlerTest {

    private ArgumentCaptor<String> systemPropertyArgumentCaptor;
    private ArgumentCaptor<String> checkFileNameArgumentCaptor;
    private ArgumentCaptor<IOFactory> ioFactoryArgumentCaptor;
    private ArgumentCaptor<String> parseFileNameArgumentCaptor;
    private ArgumentCaptor<JSONObject> canvasFromJSONElementArgumentCaptor;
    private ArgumentCaptor<String> showErrorDialogFileNameArgumentCaptor;

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @CsvSource("#FF0000, 1, 2, 3, #00FF00, #0000FF")
    public void testCanvasFromJSONObjectSuccess(String colorCode, Integer x, Integer y, Integer radius, String fillColor, String borderColor) throws NoSuchFieldException, IllegalAccessException {
        Context context = contextBuilder()
            .subject("LoadCanvasHandler#canvasFromJSONElement(JSONElement)")
            .build();

        Color background = Color.decode(colorCode);
        MyShape shape = new MyCircle(x, y, radius, Color.decode(fillColor), Color.decode(borderColor));

        JSONObject input = JSONObject.of(
            JSONObjectEntry.of("shapes", JSONArray.of(shape.toJSON())),
            JSONObjectEntry.of("background", ColorHelper.toJSON(background))
        );

        try (MockedStatic<MyShape> mockUtils = mockStatic(MyShape.class, CALLS_REAL_METHODS)) {

            ArgumentCaptor<JSONElement> inputCapture = ArgumentCaptor.forClass(JSONElement.class);

            mockUtils.when(() -> MyShape.fromJSON(inputCapture.capture())).thenReturn(shape);

            LoadCanvasHandler loadCanvasHandler = mock(LoadCanvasHandler.class);
            doCallRealMethod().when(loadCanvasHandler).canvasFromJSONElement(any());

            loadCanvasHandler.canvasFromJSONElement(input);

            Field shapesField = LoadCanvasHandler.class.getDeclaredField("shapes");
            shapesField.setAccessible(true);
            List<MyShape> actualShapes = (List<MyShape>) shapesField.get(loadCanvasHandler);

            Field backgroundColorField = LoadCanvasHandler.class.getDeclaredField("backgroundColor");
            backgroundColorField.setAccessible(true);
            Color actualBackgroundColor = (Color) backgroundColorField.get(loadCanvasHandler);

            assertEquals(List.of(shape), actualShapes, context,
                TR -> "The method did not set the attribute shapes to the correct value");

            assertEquals(background, actualBackgroundColor, context,
                TR -> "The method did not set the field backgroundColor to the correct value");

            assertEquals(input.getValueOf("shapes").getArray()[0], inputCapture.getValue(), context,
                TR -> "The method MyShape.fromJSON(JSONElement) wasn't called with the correct JSONElement");
        }


    }

    @Test
    public void testCanvasFromJSONElementNull() {
        Context context = contextBuilder()
            .add("input", null)
            .subject("LoadCanvasHandler#canvasFromJSONElement(JSONElement)")
            .build();

        LoadCanvasHandler loadCanvasHandler = mock(LoadCanvasHandler.class);
        doCallRealMethod().when(loadCanvasHandler).canvasFromJSONElement(any());

        assertThrows(JSONParseException.class, () -> loadCanvasHandler.canvasFromJSONElement(null), context,
            TR -> "The method did not throw the correct exception when the input is null");

        try {
            loadCanvasHandler.canvasFromJSONElement(null);
        } catch (JSONParseException exc) {
            String expected1 = "An exception occurred while trying to parse a JSON file. The given file is empty!";
            String expected2 = "An exception occurred while trying to parse a JSON file. The given File is empty!"; //typo from sheet

            if (!expected1.equals(exc.getMessage()) && !expected2.equals(exc.getMessage())) {
                assertEquals(expected1, exc.getMessage(),
                    context, TR -> "The thrown exception does not contain the correct message");
            }
        }
    }

    @Test
    public void testCanvasFromJSONElementInvalidFormat() {
        Context contextEmpty = contextBuilder()
            .add("input", "empty")
            .subject("LoadCanvasHandler#canvasFromJSONElement(JSONElement)")
            .build();

        LoadCanvasHandler loadCanvasHandler = mock(LoadCanvasHandler.class);
        doCallRealMethod().when(loadCanvasHandler).canvasFromJSONElement(any());

        JSONObject emptyInput = JSONObject.of();

        assertThrows(JSONParseException.class, () -> loadCanvasHandler.canvasFromJSONElement(emptyInput), contextEmpty,
            TR -> "The method did not throw the correct exception when the input is empty");

        try {
            loadCanvasHandler.canvasFromJSONElement(emptyInput);
        } catch (JSONParseException exc) {
            assertEquals("An exception occurred while trying to parse a JSON file. Invalid MyShape format!", exc.getMessage(),
                contextEmpty, TR -> "The thrown exception does not contain the correct message");
        }

        Context contextInvalid = contextBuilder()
            .add("input", "invalid format")
            .subject("LoadCanvasHandler#canvasFromJSONElement(JSONElement)")
            .build();

        JSONNumberNode invalidTypeInput = new JSONNumberNode(1);

        assertThrows(JSONParseException.class, () -> loadCanvasHandler.canvasFromJSONElement(invalidTypeInput), contextInvalid,
            TR -> "The method canvasFromJSONElement(JSONElement) did not throw the correct exception when the input is invalid");

        try {
            loadCanvasHandler.canvasFromJSONElement(invalidTypeInput);
        } catch (JSONParseException exc) {
            assertEquals("An exception occurred while trying to parse a JSON file. Invalid MyShape format!", exc.getMessage(),
                contextInvalid, TR -> "The thrown exception does not contain the correct message");
        }
    }

    @ParameterizedTest
    @CsvSource("test.json")
    public void testLoadSuccess(String fileName) {
        Context context = new BasicContext.Builder.Factory().builder()
            .subject("LoadCanvasHandler#load()")
            .build();

        JSON json = mock(JSON.class);

        JSONObject jsonObject = JSONObject.of();
        LoadCanvasHandler loadCanvasHandler = mock(LoadCanvasHandler.class);
        doCallRealMethod().when(loadCanvasHandler).load();
        doCallRealMethod().when(loadCanvasHandler).setJson(any());

        InOrder inOrder = inOrder(loadCanvasHandler, json);

        prepareArgumentCaptor(loadCanvasHandler, json, fileName, jsonObject, true);

        loadCanvasHandler.setJson(json);
        loadCanvasHandler.load();

        inOrder.verify(loadCanvasHandler, times(1)).selectFile(any());
        assertTrue(systemPropertyArgumentCaptor.getValue().equals(System.getProperty("user.dir")), context,
            TR -> "The method did not call the method selectFile(String) with the correct value");

        inOrder.verify(loadCanvasHandler, times(1)).checkFileName(any());
        assertEquals(fileName, checkFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method checkFileName(String) with the correct value");

        inOrder.verify(json, times(1)).setIOFactory(any());
        assertTrue(ioFactoryArgumentCaptor.getValue() instanceof FileSystemIOFactory, context,
            TR -> "The method did not call the method json.setIOFactory(IOFactory) with a correct value");

        inOrder.verify(json, times(1)).parse(any());
        assertEquals(fileName, parseFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method json.parse(String) with the correct value");

        inOrder.verify(loadCanvasHandler, times(1)).canvasFromJSONElement(any());
        assertEquals(jsonObject, canvasFromJSONElementArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method canvasFromJSONElement with the correct value for the second parameter");

        inOrder.verify(loadCanvasHandler, times(1)).setupNewFrame();

        verify(loadCanvasHandler, never()).showErrorDialog(any());
        verify(loadCanvasHandler, never()).showSuccessDialog(any());
    }

    @ParameterizedTest
    @CsvSource("test.txt")
    public void testLoadInvalidFileName(String invalidFileName) {
        Context context = new BasicContext.Builder.Factory().builder()
            .subject("LoadCanvasHandler#load()")
            .build();

        LoadCanvasHandler loadCanvasHandler = mock(LoadCanvasHandler.class);
        doCallRealMethod().when(loadCanvasHandler).load();
        doCallRealMethod().when(loadCanvasHandler).setJson(any());

        JSON json = mock(JSON.class);
        loadCanvasHandler.setJson(json);

        InOrder inOrder = inOrder(loadCanvasHandler, json);

        prepareArgumentCaptor(loadCanvasHandler, json, invalidFileName, null, false);

        loadCanvasHandler.load();

        inOrder.verify(loadCanvasHandler, times(1)).selectFile(any());
        assertTrue(systemPropertyArgumentCaptor.getValue().equals(System.getProperty("user.dir")), context,
            TR -> "The method did not call the method selectFile(String) with the correct value");

        inOrder.verify(loadCanvasHandler, times(1)).checkFileName(any());
        assertEquals(invalidFileName, checkFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method checkFileName(String) with the correct value");

        verify(json, never()).write(any(), any());
        verify(loadCanvasHandler, never()).showSuccessDialog(any());
        verify(loadCanvasHandler, never()).setupNewFrame();
        verify(loadCanvasHandler, never()).showErrorDialog(any());
    }

    @ParameterizedTest
    @CsvSource("test.json, An exception was thrown")
    public void testLoadException(String fileName, String exceptionMessage) {
        Context context = new BasicContext.Builder.Factory().builder()
            .subject("LoadCanvasHandler#load()")
            .build();

        JSON json = mock(JSON.class);
        JSONObject jsonObject = JSONObject.of();
        LoadCanvasHandler loadCanvasHandler = mock(LoadCanvasHandler.class);
        doCallRealMethod().when(loadCanvasHandler).load();
        doCallRealMethod().when(loadCanvasHandler).setJson(any());

        InOrder inOrder = inOrder(loadCanvasHandler, json);

        prepareArgumentCaptor(loadCanvasHandler, json, fileName, jsonObject, true);

        doThrow(new JSONParseException(exceptionMessage)).when(json).parse(parseFileNameArgumentCaptor.capture());

        loadCanvasHandler.setJson(json);
        loadCanvasHandler.load();

        inOrder.verify(loadCanvasHandler, times(1)).selectFile(any());
        assertTrue(systemPropertyArgumentCaptor.getValue().equals(System.getProperty("user.dir")), context,
            TR -> "The method did not call the method selectFile(String) with the correct value");

        inOrder.verify(loadCanvasHandler, times(1)).checkFileName(any());
        assertEquals(fileName, checkFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method checkFileName(String) with the correct value");

        inOrder.verify(json, times(1)).setIOFactory(any());
        assertTrue(ioFactoryArgumentCaptor.getValue() instanceof FileSystemIOFactory, context,
            TR -> "The method did not call the method json.setIOFactory(IOFactory) with a correct value");

        inOrder.verify(json, times(1)).parse(any());
        assertEquals(fileName, parseFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method json.parse(String) with the correct value");

        inOrder.verify(loadCanvasHandler, times(1)).showErrorDialog(any());
        assertEquals("An exception occurred while trying to parse a JSON file. " + exceptionMessage, showErrorDialogFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method showSuccessDialog(String) with the correct value");

        verify(loadCanvasHandler, never()).showSuccessDialog(any());
        verify(loadCanvasHandler, never()).setupNewFrame();
    }

    private void prepareArgumentCaptor(LoadCanvasHandler loadCanvasHandler, JSON json, String fileName, JSONObject jsonObject, boolean checkFileName) {
        systemPropertyArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(loadCanvasHandler.selectFile(systemPropertyArgumentCaptor.capture())).thenReturn(fileName);

        checkFileNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(loadCanvasHandler.checkFileName(checkFileNameArgumentCaptor.capture())).thenReturn(checkFileName);

        ioFactoryArgumentCaptor = ArgumentCaptor.forClass(IOFactory.class);
        doNothing().when(json).setIOFactory(ioFactoryArgumentCaptor.capture());

        canvasFromJSONElementArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        doNothing().when(loadCanvasHandler).canvasFromJSONElement(canvasFromJSONElementArgumentCaptor.capture());

        parseFileNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        doReturn(jsonObject).when(json).parse(parseFileNameArgumentCaptor.capture());

        showErrorDialogFileNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(loadCanvasHandler).showErrorDialog(showErrorDialogFileNameArgumentCaptor.capture());
    }
}
