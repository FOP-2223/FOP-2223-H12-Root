package h12.h3;

import h12.TutorResourceIOFactory;
import h12.ioFactory.ResourceIOFactory;
import h12.json.*;
import h12.json.parser.implementation.node.JSONElementNodeParser;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicContext;

import java.io.IOException;

import static h12.json.JSONObject.JSONObjectEntry;

@TestForSubmission()
public class TutorTests_H3_3_JSONParseComplexTest {

    @Test
    public void testParseJSONComplex() throws IOException {
        Context context = new BasicContext.Builder.Factory().builder().property("input", "complex, nested json file")
            .subject("JSONElementNodeParser#parse()").build();

        String resourceName = "h12/h3/JSONParserComplexTest.json";

        try (LookaheadReader reader = new LookaheadReader(new TutorResourceIOFactory().createReader(resourceName))) {
            JSONElementNodeParser elementNodeParser = new JSONElementNodeParser(reader);

            JSONElement actual = elementNodeParser.parse();
            JSONElement expected = getExpected();

            Assertions2.assertEquals(expected, actual, context,
                TR -> "The method did not return the expected value given a complex input");
        }
    }

    private JSONElement getExpected() {
        return JSONObject.of(
            JSONObjectEntry.of("Modulname", JSONString.of("Funktionale und objektorientierte Programmierkonzepte")),
            JSONObjectEntry.of("Dozent", JSONString.of("Karsten Weihe")),
            JSONObjectEntry.of("CP", JSONNumber.of(10.0)),
            JSONObjectEntry.of("WS", JSONConstant.TRUE),
            JSONObjectEntry.of("Students", JSONArray.of(
                JSONObject.of(
                    JSONObjectEntry.of("Vorname", JSONString.of("Max")),
                    JSONObjectEntry.of("Nachname", JSONString.of("Mustermann"))
                ),
                JSONObject.of(
                    JSONObjectEntry.of("Vorname", JSONString.of("Erika")),
                    JSONObjectEntry.of("Nachname", JSONString.of("Musterfrau"))
                )
            )),
            JSONObjectEntry.of("Noten", JSONConstant.NULL)
        );
    }
}
