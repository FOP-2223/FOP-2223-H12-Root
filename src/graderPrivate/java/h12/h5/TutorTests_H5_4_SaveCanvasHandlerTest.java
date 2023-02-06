package h12.h5;

import h12.exceptions.JSONWriteException;
import h12.gui.components.SaveCanvasHandler;
import h12.gui.shapes.ColorHelper;
import h12.gui.shapes.MyRectangle;
import h12.gui.shapes.MyShape;
import h12.ioFactory.FileSystemIOFactory;
import h12.ioFactory.IOFactory;
import h12.json.JSON;
import h12.json.JSONElement;
import h12.json.JSONObject;
import h12.json.implementation.node.JSONArrayNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
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
public class TutorTests_H5_4_SaveCanvasHandlerTest {

    private ArgumentCaptor<String> systemPropertyArgumentCaptor;
    private ArgumentCaptor<String> checkFileNameArgumentCaptor;
    private ArgumentCaptor<IOFactory> ioFactoryArgumentCaptor;
    private ArgumentCaptor<String> writeFileNameArgumentCaptor;
    private ArgumentCaptor<JSONObject> writeJsonObjectArgumentCaptor;
    private ArgumentCaptor<String> showSuccessDialogFileNameArgumentCaptor;
    private ArgumentCaptor<String> showErrorDialogFileNameArgumentCaptor;

    @ParameterizedTest
    @CsvSource("#FF0000")
    public void testCanvasToJSONObject(String colorCode) throws NoSuchFieldException, IllegalAccessException {
        Context context = contextBuilder()
            .subject("SaveCanvasHandler#canvasTOJSONObject()")
            .build();

        SaveCanvasHandler saveCanvasHandler = mock(SaveCanvasHandler.class);

        when(saveCanvasHandler.canvasToJSONObject()).thenCallRealMethod();

        JSONElement resultElement = JSONObject.of();
        MyShape shape = mock(MyRectangle.class);
        when(shape.toJSON()).thenReturn(resultElement);
        List<MyShape> contents = List.of(shape);

        Field contentsField = SaveCanvasHandler.class.getDeclaredField("contents");
        contentsField.setAccessible(true);
        contentsField.set(saveCanvasHandler, contents);

        Color background = Color.decode(colorCode);

        Field backgroundField = SaveCanvasHandler.class.getDeclaredField("background");
        backgroundField.setAccessible(true);
        backgroundField.set(saveCanvasHandler, background);

        JSONObject actual = saveCanvasHandler.canvasToJSONObject();

        assertEquals(2, actual.getObjectEntries().size(), context,
            TR -> "The JSONObject returned by the method does not contain the correct amount of entries");

        assertTrue(actual.getObjectEntries().contains(JSONObjectEntry.of("background", null)), context,
            TR -> "The JSONObject returned by the method does not contain the key background");
        assertEquals(ColorHelper.toJSON(background), actual.getValueOf("background"), context,
            TR -> "The JSONObject returned by the method does not contain the correct value for the key background");

        assertTrue(actual.getObjectEntries().contains(JSONObjectEntry.of("shapes", null)), context,
            TR -> "The JSONObject returned by the method does not contain the key shapes");
        assertEquals(new JSONArrayNode(List.of(resultElement)), actual.getValueOf("shapes"), context,
            TR -> "The JSONObject returned by the method does not contain the correct value for the key shapes");
    }

    @ParameterizedTest
    @CsvSource("test.json")
    public void testSaveSuccess(String fileName) {
        Context context = new BasicContext.Builder.Factory().builder()
            .subject("SaveCanvasHandler#save()")
            .build();

        JSON json = mock(JSON.class);

        JSONObject jsonObject = JSONObject.of();
        SaveCanvasHandler saveCanvasHandler = mock(SaveCanvasHandler.class);
        doCallRealMethod().when(saveCanvasHandler).save();
        doCallRealMethod().when(saveCanvasHandler).setJson(any());

        InOrder inOrder = inOrder(saveCanvasHandler, json);

        prepareArgumentCaptor(saveCanvasHandler, json, fileName, jsonObject, true);

        saveCanvasHandler.setJson(json);
        saveCanvasHandler.save();

        inOrder.verify(saveCanvasHandler, times(1)).selectFile(any());
        assertTrue(systemPropertyArgumentCaptor.getValue().equals(System.getProperty("user.dir")), context,
            TR -> "The method did not call the method selectFile(String) with the correct value");

        inOrder.verify(saveCanvasHandler, times(1)).checkFileName(any());
        assertEquals(fileName, checkFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method checkFileName(String) with the correct value");

        inOrder.verify(json, times(1)).setIOFactory(any());
        assertTrue(ioFactoryArgumentCaptor.getValue() instanceof FileSystemIOFactory, context,
            TR -> "The method did not call the method json.setIOFactory(IOFactory) with a correct value");

        inOrder.verify(json, times(1)).write(any(), any());
        assertEquals(fileName, writeFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method json.write(String, JSONObject) with the correct value for the first parameter");
        assertEquals(jsonObject, writeJsonObjectArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method json.write(String, JSONObject) with the correct value for the second parameter");

        inOrder.verify(saveCanvasHandler, times(1)).showSuccessDialog(any());
        assertEquals(fileName, showSuccessDialogFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method showSuccessDialog(String) with the correct value");

        verify(saveCanvasHandler, never()).showErrorDialog(any());
    }

    @ParameterizedTest
    @CsvSource("test.txt")
    public void testSaveInvalidFileName(String invalidFileName) {
        Context context = new BasicContext.Builder.Factory().builder()
            .subject("SaveCanvasHandler#save()")
            .build();

        SaveCanvasHandler saveCanvasHandler = mock(SaveCanvasHandler.class);
        doCallRealMethod().when(saveCanvasHandler).save();
        doCallRealMethod().when(saveCanvasHandler).setJson(any());

        JSON json = mock(JSON.class);
        saveCanvasHandler.setJson(json);

        InOrder inOrder = inOrder(saveCanvasHandler, json);

        prepareArgumentCaptor(saveCanvasHandler, json, invalidFileName, null, false);

        saveCanvasHandler.save();

        inOrder.verify(saveCanvasHandler, times(1)).selectFile(any());
        assertTrue(systemPropertyArgumentCaptor.getValue().equals(System.getProperty("user.dir")), context,
            TR -> "The method did not call the method selectFile(String) with the correct value");

        inOrder.verify(saveCanvasHandler, times(1)).checkFileName(any());
        assertEquals(invalidFileName, checkFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method checkFileName(String) with the correct value");

        verify(json, never()).write(any(), any());
        verify(saveCanvasHandler, never()).showSuccessDialog(any());
        verify(saveCanvasHandler, never()).showErrorDialog(any());
    }

    @ParameterizedTest
    @CsvSource("test.json, An exception was thrown")
    public void testSaveException(String fileName, String exceptionMessage) {
        Context context = new BasicContext.Builder.Factory().builder()
            .subject("SaveCanvasHandler#save()")
            .build();

        JSON json = mock(JSON.class);
        JSONObject jsonObject = JSONObject.of();
        SaveCanvasHandler saveCanvasHandler = mock(SaveCanvasHandler.class);
        doCallRealMethod().when(saveCanvasHandler).save();
        doCallRealMethod().when(saveCanvasHandler).setJson(any());

        InOrder inOrder = inOrder(saveCanvasHandler, json);

        prepareArgumentCaptor(saveCanvasHandler, json, fileName, jsonObject, true);

        doThrow(new JSONWriteException(exceptionMessage)).when(json).write(writeFileNameArgumentCaptor.capture(), writeJsonObjectArgumentCaptor.capture());

        saveCanvasHandler.setJson(json);
        saveCanvasHandler.save();

        inOrder.verify(saveCanvasHandler, times(1)).selectFile(any());
        assertTrue(systemPropertyArgumentCaptor.getValue().equals(System.getProperty("user.dir")), context,
            TR -> "The method did not call the method selectFile(String) with the correct value");

        inOrder.verify(saveCanvasHandler, times(1)).checkFileName(any());
        assertEquals(fileName, checkFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method checkFileName(String) with the correct value");

        inOrder.verify(json, times(1)).setIOFactory(any());
        assertTrue(ioFactoryArgumentCaptor.getValue() instanceof FileSystemIOFactory, context,
            TR -> "The method did not call the method json.setIOFactory(IOFactory) with a correct value");

        inOrder.verify(json, times(1)).write(any(), any());
        assertEquals(fileName, writeFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method json.write(String, JSONObject) with the correct value for the first parameter");
        assertEquals(jsonObject, writeJsonObjectArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method json.write(String, JSONObject) with the correct value for the second parameter");

        inOrder.verify(saveCanvasHandler, times(1)).showErrorDialog(any());
        assertEquals("An exception occurred while trying to write to a JSON file. " + exceptionMessage, showErrorDialogFileNameArgumentCaptor.getValue(), context,
            TR -> "The method did not call the method showSuccessDialog(String) with the correct value");

        verify(saveCanvasHandler, never()).showSuccessDialog(any());
    }

    private void prepareArgumentCaptor(SaveCanvasHandler saveCanvasHandler, JSON json, String fileName, JSONObject jsonObject, boolean checkFileName) {
        systemPropertyArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(saveCanvasHandler.selectFile(systemPropertyArgumentCaptor.capture())).thenReturn(fileName);

        checkFileNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(saveCanvasHandler.checkFileName(checkFileNameArgumentCaptor.capture())).thenReturn(checkFileName);

        ioFactoryArgumentCaptor = ArgumentCaptor.forClass(IOFactory.class);
        doNothing().when(json).setIOFactory(ioFactoryArgumentCaptor.capture());

        when(saveCanvasHandler.canvasToJSONObject()).thenReturn(jsonObject);

        writeFileNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        writeJsonObjectArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        doNothing().when(json).write(writeFileNameArgumentCaptor.capture(), writeJsonObjectArgumentCaptor.capture());

        showSuccessDialogFileNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(saveCanvasHandler).showSuccessDialog(showSuccessDialogFileNameArgumentCaptor.capture());

        showErrorDialogFileNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(saveCanvasHandler).showErrorDialog(showErrorDialogFileNameArgumentCaptor.capture());
    }
}
