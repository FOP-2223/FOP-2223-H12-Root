package h12.h5;

import h12.gui.components.ControlPanel;
import h12.gui.components.FileOperationHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission()
public class TutorTests_H5_3_FileOperationHandlerTest {

    ArgumentCaptor<String> errorDialogCaptor;

    @ParameterizedTest
    @CsvSource({"test.json", ".json", "test.txt.json"})
    public void testCheckFileNameSuccess(String filename) {
        Context context = contextBuilder()
            .add("fileName", filename)
            .subject("FileOperationHandler#checkFileName(String)")
            .build();

        FileOperationHandler fileOperationHandler = spy(new FileOperationHandlerImpl(null));

        setupArgumentCaptor(fileOperationHandler);

        verify(fileOperationHandler, times(0)).showSuccessDialog(anyString());
        verify(fileOperationHandler, times(0)).showErrorDialog(anyString());

        assertTrue(fileOperationHandler.checkFileName(filename), context,
            TR -> "The method did not return the correct value when given a valid input");
    }

    @Test
    public void testCheckFileNameExceptionNull() {
        Context context = new BasicContext.Builder.Factory().builder()
            .add("fileName", "null")
            .subject("FileOperationHandler#checkFileName(String)")
            .build();

        FileOperationHandler fileOperationHandler = spy(new FileOperationHandlerImpl(null));

        setupArgumentCaptor(fileOperationHandler);

        assertFalse(fileOperationHandler.checkFileName(null), context,
            TR -> "The method did not return the correct value when given null");

        verify(fileOperationHandler, times(0)).showSuccessDialog(anyString());
        verify(fileOperationHandler, times(1)).showErrorDialog(anyString());

        assertEquals("No file selected!", errorDialogCaptor.getValue(), context,
            TR -> "The method showErrorDialog(String) wasn't invoked with the correct value");
    }

    @ParameterizedTest
    @CsvSource({"test.txt", "test", "test.", "test.jso", "test.json.txt"})
    public void testCheckFileNameExceptionInvalidFileType(String filename) {
        Context context = new BasicContext.Builder.Factory().builder()
            .add("fileName", filename)
            .subject("FileOperationHandler#checkFileName(String)")
            .build();

        FileOperationHandler fileOperationHandler = spy(new FileOperationHandlerImpl(null));

        setupArgumentCaptor(fileOperationHandler);

        assertFalse(fileOperationHandler.checkFileName(filename), context,
            TR -> "The method did not return the correct value when given an invalid input");

        verify(fileOperationHandler, times(0)).showSuccessDialog(anyString());
        verify(fileOperationHandler, times(1)).showErrorDialog(anyString());

        assertEquals("Invalid file type!", errorDialogCaptor.getValue(), context,
            TR -> "The method showErrorDialog(String) wasn't invoked with the correct value");
    }

    private void setupArgumentCaptor(FileOperationHandler fileOperationHandler) {
        errorDialogCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(fileOperationHandler).showErrorDialog(errorDialogCaptor.capture());
    }

    private static class FileOperationHandlerImpl extends FileOperationHandler {
        public FileOperationHandlerImpl(ControlPanel controlPanel) {
            super(controlPanel);
        }
    }
}
