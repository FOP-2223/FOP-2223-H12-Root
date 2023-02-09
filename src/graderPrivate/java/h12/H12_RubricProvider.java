package h12;

import h12.h1.FileSystemIOFactoryTransformer;
import h12.h1.TutorTests_H1_1_FileSystemIOFactoryTest;
import h12.h1.TutorTests_H1_2_LookaheadReaderTest;
import h12.h2.*;
import h12.h3.*;
import h12.h4.TutorTests_H4_1_JSONParserTest;
import h12.h4.TutorTests_H4_2_JSONTest;
import h12.h5.*;
import org.sourcegrade.jagr.api.rubric.*;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

public class H12_RubricProvider implements RubricProvider {

    private static final BiFunction<String, Callable<Method>, Criterion> DEFAULT_CRITERION = (shortDescription, callable) ->
        Criterion.builder()
            .shortDescription(shortDescription)
            .grader(Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(callable))
                .pointsFailedMin()
                .pointsPassedMax()
                .build())
            .build();

    private static final Criterion H1_1 = Criterion.builder()
        .shortDescription("Die Klasse FileSystemIOFactory funktioniert vollständig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H1_1_FileSystemIOFactoryTest.class.getDeclaredMethod("testFileSystemIOFactoryReader")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H1_1_FileSystemIOFactoryTest.class.getDeclaredMethod("testFileSystemIOFactoryWriter")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H1_2_1 = Criterion.builder()
        .shortDescription("Die Klasse LookaheadReader funktioniert vollständig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H1_2_LookaheadReaderTest.class.getDeclaredMethod("testLookaheadReader", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H1_2_LookaheadReaderTest.class.getDeclaredMethod("testEmptyLookaheadReader")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H1_2 = Criterion.builder()
        .shortDescription("H1.2 | LookaheadReader")
        .addChildCriteria(H1_2_1)
        .build();

    private static final Criterion H1 = Criterion.builder()
        .shortDescription("H1 | Vorbereitungen")
        .addChildCriteria(H1_1, H1_2)
        .build();

    private static final Criterion H2_1 = Criterion.builder()
        .shortDescription("Die Methode write(BufferedWriter, int) der Klassen JSONNumberNode, JSONConstantNode und JSONStringNode funktionieren vollständig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H2_WriteJSONNumberTest.class.getDeclaredMethod("testWriteJSONInteger", Integer.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H2_WriteJSONNumberTest.class.getDeclaredMethod("testWriteJSONDouble", Double.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H2_WriteJSONStringTest.class.getDeclaredMethod("testWriteJSONString", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H2_WriteJSONConstantTest.class.getDeclaredMethod("testWriteJSONConstant")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H2_2 = DEFAULT_CRITERION.apply("Die Methode write(BufferedWriter, int) der Klasse JSONObjectEntryNode funktioniert vollständig korrekt.",
        () -> TutorTests_H2_WriteJSONObjectEntryTest.class.getDeclaredMethod("testWriteJSONObjectEntry", String.class, Integer.class));

    private static final Criterion H2_3 = Criterion.builder()
        .shortDescription("Die Methode write(BufferedWriter, int) der Klasse JSONObjectNode funktioniert ohne Einrückung vollständig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H2_WriteJSONObjectTest.class.getDeclaredMethod("testWriteJSONObjectNoIndent", String.class, int.class, String.class, int.class, String.class, int.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .maxPoints(1)
        .build();

    private static final Criterion H2_4 = Criterion.builder()
        .shortDescription("Die Methode write(BufferedWriter, int) der Klasse JSONObjectNode funktioniert inklusive Einrückung vollständig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H2_WriteJSONObjectTest.class.getDeclaredMethod("testWriteJSONObject", String.class, int.class, String.class, int.class, String.class, int.class, int.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .maxPoints(1)
        .build();

    private static final Criterion H2_5 = Criterion.builder()
        .shortDescription("Die Methode write(BufferedWriter, int) der Klasse JSONArrayNode funktioniert ohne Einrückung vollständig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H2_WriteJSONArrayTest.class.getDeclaredMethod("testWriteJSONArrayNoIndent", int.class, int.class, int.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .maxPoints(1)
        .build();

    private static final Criterion H2_6 = Criterion.builder()
        .shortDescription("Die Methode write(BufferedWriter, int) der Klasse JSONArrayNode funktioniert inklusive Einrückung vollständig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H2_WriteJSONArrayTest.class.getDeclaredMethod("testWriteJSONArray", int.class, int.class, int.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .maxPoints(1)
        .build();

    private static final Criterion H2 = Criterion.builder()
        .shortDescription("H2 | Herausschreiben in JSON Dateien")
        .addChildCriteria(H2_1, H2_2, H2_3, H2_4, H2_5, H2_6)
        .build();

    private static final Criterion H3_1_1 = Criterion.builder()
        .shortDescription("Die Methoden skipIndentation() und peek() der Klasse JSONElementNodeParser funktionieren vollständig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testSkipIndentation", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testSkipIndentationEmpty")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testPeek", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testPeekEmpty")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H3_1_2 = Criterion.builder()
        .shortDescription("Die Methoden acceptIt() und accept(char) der Klasse JSONElementNodeParser funktionieren vollständig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testAcceptIt", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testAcceptItEmpty")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testAcceptSuccess", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testAcceptException", String.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H3_1_3 = Criterion.builder()
        .shortDescription("Die Methode checkEndOfFile() der Klasse JSONElementNodeParser funktioniert vollständigändig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testCheckEndOfFileSuccess")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testCheckEndOfFileException")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H3_1_4 = Criterion.builder()
        .shortDescription("Die Methode readUntil() der Klasse JSONElementNodeParser funktioniert vollständigändig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testReadUntil", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testReadUntilException")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H3_1 = Criterion.builder()
        .shortDescription("H3.1 | Hilfsmethoden")
        .addChildCriteria(H3_1_1, H3_1_2, H3_1_3, H3_1_4)
        .build();

    private static final Criterion H3_2_1 = Criterion.builder()
        .shortDescription("Die Methode parse() der Klasse JSONElementNodeParser funktioniert vollständigändig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testParseArray", Integer.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testParseObject", String.class, Integer.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testParseConstant", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testParseNumber", Integer.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testParseString", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_1_H3_2_JSONElementNodeParserTest.class.getDeclaredMethod("testParseEmpty")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H3_2 = Criterion.builder()
        .shortDescription("H3.2 | Erkennen der JSON Elemente")
        .addChildCriteria(H3_2_1)
        .build();

    private static final Criterion H3_3_1 = Criterion.builder()
        .shortDescription("Die Methode parse() der Klasse JSONNumberNodeParser funktioniert vollständigändig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_3_JSONNumberNodeParserTest.class.getDeclaredMethod("testParseSuccess", String.class, String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_3_JSONNumberNodeParserTest.class.getDeclaredMethod("testParseException", String.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H3_3_2 = Criterion.builder()
        .shortDescription("Die Methode parse() der Klasse JSONConstantNodeParser funktioniert vollständigändig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_3_JSONConstantNodeParserTest.class.getDeclaredMethod("testParseSuccess", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_3_JSONConstantNodeParserTest.class.getDeclaredMethod("testParseException", String.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H3_3_3 = Criterion.builder()
        .shortDescription("Die Methode parse() der Klasse JSONObjectEntryNodeParser funktioniert vollständigändig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_3_JSONObjectEntryParserTest.class.getDeclaredMethod("testParseSuccess", String.class, Integer.class, String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_3_JSONObjectEntryParserTest.class.getDeclaredMethod("testParseException", String.class, Integer.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H3_3_4 = DEFAULT_CRITERION.apply("Die Methode parse() der Klasse JSONObjectNodeParser funktioniert für korrekte Eingaben.",
        () -> TutorTests_H3_3_JSONObjectNodeParserTest.class.getDeclaredMethod("testParseSuccess", String.class, Integer.class, String.class, Integer.class, String.class, Integer.class, String.class));

    private static final Criterion H3_3_5 = Criterion.builder()
        .shortDescription("Die Methode parse() der Klasse JSONObjectNodeParser funktioniert vollständigändig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_3_JSONObjectNodeParserTest.class.getDeclaredMethod("testParseSuccess", String.class, Integer.class, String.class, Integer.class, String.class, Integer.class, String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_3_JSONObjectNodeParserTest.class.getDeclaredMethod("testParseException", String.class, Integer.class, String.class, Integer.class, String.class, Integer.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H3_3_6 = DEFAULT_CRITERION.apply("Die Methode parse() der Klasse JSONArrayNodeParser funktioniert für korrekte Eingaben.",
        () -> TutorTests_H3_3_JSONArrayNodeParserTest.class.getDeclaredMethod("testParseSuccess", Integer.class, Integer.class, Integer.class, String.class));

    private static final Criterion H3_3_7 = Criterion.builder()
        .shortDescription("Die Methode parse() der Klasse JSONArrayNodeParser funktioniert vollständigändig korrekt.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_3_JSONArrayNodeParserTest.class.getDeclaredMethod("testParseSuccess", Integer.class, Integer.class, Integer.class, String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H3_3_JSONArrayNodeParserTest.class.getDeclaredMethod("testParseException", Integer.class, Integer.class, Integer.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H3_3 = Criterion.builder()
        .shortDescription("H3.3 | Die eigentlichen Parser")
        .addChildCriteria(H3_3_1, H3_3_2, H3_3_3, H3_3_4, H3_3_5, H3_3_6, H3_3_7)
        .build();

    private static final Criterion H3 = Criterion.builder()
        .shortDescription("H3 | Einlesen von JSON Dateien")
        .addChildCriteria(H3_1, H3_2, H3_3)
        .build();

    private static final Criterion H4_1_1 = DEFAULT_CRITERION.apply("Die Methode parse() der Klasse JSONParser funktioniert für korrekte Eingaben",
        () -> TutorTests_H4_1_JSONParserTest.class.getDeclaredMethod("testParseSuccess"));

    private static final Criterion H4_1_2 = Criterion.builder()
        .shortDescription("Die Methode parse() der Klasse JSONParser funktioniert vollständig korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H4_1_JSONParserTest.class.getDeclaredMethod("testParseSuccess")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H4_1_JSONParserTest.class.getDeclaredMethod("testParseIOException")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H4_1 = Criterion.builder()
        .shortDescription("H4.1 | JSON Parser")
        .addChildCriteria(H4_1_1, H4_1_2)
        .build();

    private static final Criterion H4_2_1 = DEFAULT_CRITERION.apply("Die Methode write(String, JSONElement) der Klasse JSON funktioniert für korrekte Eingaben",
        () -> TutorTests_H4_2_JSONTest.class.getDeclaredMethod("testWriteSuccess"));

    private static final Criterion H4_2_2 = Criterion.builder()
        .shortDescription("Die Methode write(String, JSONElement) der Klasse JSON funktioniert vollständig korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H4_2_JSONTest.class.getDeclaredMethod("testWriteSuccess")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H4_2_JSONTest.class.getDeclaredMethod("testWriteExceptionUnsupportedWriting")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H4_2_JSONTest.class.getDeclaredMethod("testWriteIOException")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H4_2_3 = DEFAULT_CRITERION.apply("Die Methode parse(String) der Klasse JSON funktioniert für korrekte Eingaben",
        () -> TutorTests_H4_2_JSONTest.class.getDeclaredMethod("testParseSuccess"));

    private static final Criterion H4_2_4 = Criterion.builder()
        .shortDescription("Die Methode parse(String) der Klasse JSON funktioniert vollständig korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H4_2_JSONTest.class.getDeclaredMethod("testParseSuccess")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H4_2_JSONTest.class.getDeclaredMethod("testParseExceptionUnsupportedReading")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H4_2_JSONTest.class.getDeclaredMethod("testParseIOException")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H4_2 = Criterion.builder()
        .shortDescription("H4.2 | JSON")
        .addChildCriteria(H4_2_1, H4_2_2, H4_2_3, H4_2_4)
        .build();

    private static final Criterion H4 = Criterion.builder()
        .shortDescription("H4 | JSON Handler")
        .addChildCriteria(H4_1, H4_2)
        .build();

    private static final Criterion H5_1_1 = DEFAULT_CRITERION.apply("Die Methode toJSON() der Klasse MyPolygon funktioniert vollständig korrekt",
        () -> TutorTests_H5_2_MyPolygonTest.class.getDeclaredMethod("testToJSON", Integer.class, Integer.class, Integer.class, Integer.class, String.class, String.class, Integer.class));

    private static final Criterion H5_1 = Criterion.builder()
        .shortDescription("H5.1 | Formen als JSON Dateien darstellen")
        .addChildCriteria(H5_1_1)
        .build();

    private static final Criterion H5_2_1 = DEFAULT_CRITERION.apply("Die Methode fromJSON(JSONElement) der Klasse MyShape funktioniert für korrekte Eingaben",
        () -> TutorTests_H5_2_MyShapeTest.class.getDeclaredMethod("testFromJSONSuccess", String.class));

    private static final Criterion H5_2_2 = Criterion.builder()
        .shortDescription("Die Methode fromJSON(JSONElement) der Klasse MyShape funktioniert vollständig korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_2_MyShapeTest.class.getDeclaredMethod("testFromJSONSuccess", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_2_MyShapeTest.class.getDeclaredMethod("testFromJSONInvalidFormat")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_2_MyShapeTest.class.getDeclaredMethod("testFromJSONInvalidShapeType", String.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H5_2_3 = Criterion.builder()
        .shortDescription("Die Methode polygonFromJSON(JSONElement) der Klasse JSONToShapeConverter funktioniert vollständig korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_1_JSONToShapeConverterTest.class.getDeclaredMethod("testFromJSONSuccess", Integer.class, Integer.class, Integer.class, Integer.class, String.class, String.class, Integer.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_1_JSONToShapeConverterTest.class.getDeclaredMethod("testFromJSONInvalidFormat")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H5_2 = Criterion.builder()
        .shortDescription("H5.2 | Formen aus JSON Dateien einlesen")
        .addChildCriteria(H5_2_1, H5_2_2, H5_2_3)
        .build();

    private static final Criterion H5_3_1 = Criterion.builder()
        .shortDescription("Die Methode checkFileName(String) der Klasse FileOperationHandler funktioniert vollständig korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_3_FileOperationHandlerTest.class.getDeclaredMethod("testCheckFileNameSuccess", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_3_FileOperationHandlerTest.class.getDeclaredMethod("testCheckFileNameExceptionNull")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_3_FileOperationHandlerTest.class.getDeclaredMethod("testCheckFileNameExceptionInvalidFileType", String.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H5_3 = Criterion.builder()
        .shortDescription("H5.3 | Validieren von ausgewählten Dateien")
        .addChildCriteria(H5_3_1)
        .build();

    private static final Criterion H5_4_1 = DEFAULT_CRITERION.apply("Die Methode canvasToJSONObject() der Klasse SaveCanvasHandler funktioniert vollständig korrekt",
        () -> TutorTests_H5_4_SaveCanvasHandlerTest.class.getDeclaredMethod("testCanvasToJSONObject", String.class));

    private static final Criterion H5_4_2 = DEFAULT_CRITERION.apply("Die Methode save() der Klasse SaveCanvasHandler funktioniert für korrekte Eingaben",
        () -> TutorTests_H5_4_SaveCanvasHandlerTest.class.getDeclaredMethod("testSaveSuccess", String.class));

    private static final Criterion H5_4_3 = Criterion.builder()
        .shortDescription("Die Methode save() der Klasse SaveCanvasHandler funktioniert vollständig korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_4_SaveCanvasHandlerTest.class.getDeclaredMethod("testSaveSuccess", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_4_SaveCanvasHandlerTest.class.getDeclaredMethod("testSaveException", String.class, String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_4_SaveCanvasHandlerTest.class.getDeclaredMethod("testSaveInvalidFileName", String.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H5_4 = Criterion.builder()
        .shortDescription("H5.4 | Speichern von Zeichnungen in JSON Dateien")
        .addChildCriteria(H5_4_1, H5_4_2, H5_4_3)
        .build();

    private static final Criterion H5_5_1 = DEFAULT_CRITERION.apply("Die Methode canvasFromJSONObject(JSONElement) der Klasse LoadCanvasHandler funktioniert für korrekte Eingaben",
        () -> TutorTests_H5_5_LoadCanvasHandlerTest.class.getDeclaredMethod("testCanvasFromJSONObjectSuccess", String.class, Integer.class, Integer.class, Integer.class, String.class, String.class));

    private static final Criterion H5_5_2 = Criterion.builder()
        .shortDescription("Die Methode canvasFromJSONObject(JSONElement) der Klasse LoadCanvasHandler funktioniert vollständig korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_5_LoadCanvasHandlerTest.class.getDeclaredMethod("testCanvasFromJSONObjectSuccess", String.class, Integer.class, Integer.class, Integer.class, String.class, String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_5_LoadCanvasHandlerTest.class.getDeclaredMethod("testCanvasFromJSONElementNull")))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_5_LoadCanvasHandlerTest.class.getDeclaredMethod("testCanvasFromJSONElementInvalidFormat")))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H5_5_3 = DEFAULT_CRITERION.apply("Die Methode load() der Klasse LoadCanvasHandler funktioniert für korrekte Eingaben",
        () -> TutorTests_H5_5_LoadCanvasHandlerTest.class.getDeclaredMethod("testLoadSuccess", String.class));

    private static final Criterion H5_5_4 = Criterion.builder()
        .shortDescription("Die Methode load() der Klasse LoadCanvasHandler funktioniert vollständig korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_5_LoadCanvasHandlerTest.class.getDeclaredMethod("testLoadSuccess", String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_5_LoadCanvasHandlerTest.class.getDeclaredMethod("testLoadException", String.class, String.class)))
            .requirePass(JUnitTestRef.ofMethod(() -> TutorTests_H5_5_LoadCanvasHandlerTest.class.getDeclaredMethod("testLoadInvalidFileName", String.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H5_5 = Criterion.builder()
        .shortDescription("H5.5 | Laden von Zeichnungen aus JSON Dateien")
        .addChildCriteria(H5_5_1, H5_5_2, H5_5_3, H5_5_4)
        .build();

    private static final Criterion H5 = Criterion.builder()
        .shortDescription("H5 | Speichern und Einlesen von Zeichnungen")
        .addChildCriteria(H5_1, H5_2, H5_3, H5_4, H5_5)
        .build();

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H12")
        .addChildCriteria(H1, H2, H3, H4, H5)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(new FileSystemIOFactoryTransformer());
    }
}
