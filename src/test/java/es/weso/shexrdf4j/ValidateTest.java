package es.weso.shexrdf4j;

import es.weso.rdf.RDFReader;
import es.weso.rdf.nodes.IRI;
import es.weso.shapeMaps.*;
import es.weso.shex.validator.Validator$;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.junit.Assert;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import org.eclipse.rdf4j.model.vocabulary.FOAF;


import scala.Option;

import java.net.URI;
import java.net.URISyntaxException;

public class ValidateTest {


    private static final String SHACLEX = "SHACLex";
    private static final Option<String> OPTION_NONE = Option.apply(null);
    private static final String TRIGGER_MODE = "TargetDecls";
    private static final Option<IRI> noneIri = Option.empty();
    private static final Option<RDFReader> noneReader = Option.empty();
    private static final ShapeMap$ shapeMapObj = ShapeMap$.MODULE$ ;
    private static final Validator$ validatorObj = Validator$.MODULE$ ;
    private static final ResultShapeMap emptyResult = ResultShapeMap$.MODULE$.empty();
    private static final Status conformant = Conformant$.MODULE$;
    private final Validator validator = new Validator();

    private Boolean isConformant(String node, String shape, ResultShapeMap result) throws URISyntaxException {
        Info info = result.getInfo(new IRI(new URI(node)), new IRILabel(new IRI(new URI(shape))));
        return info.status().equals(conformant);
    }

    @Test
    public void ValidateSimple() {
        try {
            ModelBuilder builder = new ModelBuilder();
            builder.setNamespace("", "http://example.org/").setNamespace(FOAF.NS);
            builder.defaultGraph()
					.subject(":alice")
                    .add(FOAF.NAME, "Alice")
                    .add(FOAF.AGE, 42)
                    .add(FOAF.MBOX, "alice@example.org")
                    .subject(":bob")
                    .add(FOAF.NAME, "Bob")
                    .add(FOAF.AGE, "Unknown")
                    .add(FOAF.MBOX, "bob@example.org");
            Model model = builder.build();

            String schemaStr = "prefix : <http://example.org/>\n"
                    + "prefix foaf: <http://xmlns.com/foaf/0.1/>\n"
                    + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                    + ":User {\n"
                    + " foaf:name xsd:string ; \n"
                    + " foaf:age xsd:int\n"
                    + "}";

            String shapeMapStr = ":alice@:User,:bob@:User";

            ResultShapeMap result = validator.validateModel(model, schemaStr, shapeMapStr);
            // System.out.println("ResultShapeMap: " + result);

            assertThat(isConformant("http://example.org/alice","http://example.org/User", result), is(true));
            assertThat(isConformant("http://example.org/bob","http://example.org/User", result), is(false));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception thrown in validateSimple");
        }
    }

}