package h12.h5;

import h12.gui.components.ControlPanel;
import h12.gui.components.FileOperationHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertFalse;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;

@TestForSubmission()
public class TutorTests_H5_3_FileOperationHandlerTest {

    @ParameterizedTest
    @CsvSource("test.json")
    public void testCheckFileNameSuccess(String filename) {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("fileName", filename)
            .subject("FileOperationHandler#checkFileName(String)")
            .build();

        assertTrue(new FileOperationHandlerImpl(null).checkFileName(filename), context,
            TR -> "The method did not return the correct value when given a valid input");
    }

    @Test
    public void testCheckFileNameExceptionNull() {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("fileName", "null")
            .subject("FileOperationHandler#checkFileName(String)")
            .build();

        FileOperationHandler fileOperationHandler = spy(new FileOperationHandlerImpl(null));
        doNothing().when(fileOperationHandler).showErrorDialog(anyString());

        assertFalse(fileOperationHandler.checkFileName(null), context,
            TR -> "The method did not return the correct value when given null");
    }

    @ParameterizedTest
    @CsvSource("test.txt")
    public void testCheckFileNameExceptionInvalidFileType(String filename) {
        Context context = new BasicContext.Builder.Factory().builder()
            .property("fileName", filename)
            .subject("FileOperationHandler#checkFileName(String)")
            .build();

        FileOperationHandler fileOperationHandler = spy(new FileOperationHandlerImpl(null));
        doNothing().when(fileOperationHandler).showErrorDialog(anyString());

        assertFalse(fileOperationHandler.checkFileName(filename), context,
            TR -> "The method did not return the correct value when given an invalid input");
    }

    private static class FileOperationHandlerImpl extends FileOperationHandler {
        public FileOperationHandlerImpl(ControlPanel controlPanel) {
            super(controlPanel);
        }
    }
}
