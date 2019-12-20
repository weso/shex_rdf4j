package es.weso.shexjava;

import es.weso.rdf.RDFReader;
import es.weso.rdf.rdf4j.RDFAsRDF4jModel$;
import es.weso.rdf.nodes.IRI;
import es.weso.shapeMaps.ResultShapeMap;
import es.weso.shapeMaps.ShapeMap$;
import es.weso.shex.Schema;
import es.weso.shex.validator.Validator$;
import io.circe.Json;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.junit.Assert;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;


import es.weso.rdf.PrefixMap;
import es.weso.rdf.rdf4j.RDFAsRDF4jModel;
import es.weso.shapeMaps.ShapeMap;
import scala.Option;
import scala.collection.immutable.Map;
import scala.util.Either;
import scala.util.Try;

public class ValidateTest {


    private static final String SHACLEX = "SHACLex";
    private static final Option<String> OPTION_NONE = Option.apply(null);
    private static final String TRIGGER_MODE = "TargetDecls";
    private static final Option<IRI> noneIri = Option.empty();
    private static final Option<RDFReader> noneReader = Option.empty();
    private static final ShapeMap$ shapeMapObj = ShapeMap$.MODULE$ ;
    private static final Validator$ validatorObj = Validator$.MODULE$ ;


/*	private es.weso.schema.Result validate(RDFAsJenaModel resourceAsRDFReader, RDFAsJenaModel shapeAsRDFReader) {
	        Schema schema = null;
	        Either<String,Schema> schemaTry = Schemas.fromRDF(shapeAsRDFReader, SHACLEX);
	        if (schemaTry.isRight()) {
	            schema = schemaTry.right().get();
	        }
	        return validate(resourceAsRDFReader, schema);
	    }
	
	 private es.weso.schema.Result validate(final RDFAsJenaModel rdf, final Schema schema) {
	        PrefixMap nodeMap = rdf.getPrefixMap();
	        PrefixMap shapesMap = schema.pm();
	        String shapeMap = "";
	        return schema.validate(rdf, TRIGGER_MODE, shapeMap, OPTION_NONE, OPTION_NONE, nodeMap,
	                shapesMap);
	    } */

    @Test
    public void ValidateSimple() {
        try {
            ModelBuilder builder = new ModelBuilder();
            builder.setNamespace("ex", "http://example.org/").setNamespace(FOAF.NS);
            builder.defaultGraph()
					.subject("ex:john")
                    .add(FOAF.NAME, "John")
                    .add(FOAF.AGE, 42)
                    .add(FOAF.MBOX, "john@example.org");
            Model model = builder.build();
            RDFAsRDF4jModel rdfAsRDF4jModel = new RDFAsRDF4jModel(model,noneIri);

            String schemaStr = "prefix : <http://example.org>\n"
                    + "prefix foaf: <http://xmlns.com/foaf/0.1/>\n"
                    + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>"
                    + ":User {\n"
                    + " foaf:name xsd:string ; \n"
                    + " foaf:age xsd:int\n"
                    + "}";

            String shapeMapStr = "ex:john@:User";

            Either<String, Json> eitherResult =
                    Schema.fromString(schemaStr,"ShExC",noneIri,noneReader).flatMap(schema ->
                    shapeMapObj.fromCompact(shapeMapStr,noneIri,rdfAsRDF4jModel.getPrefixMap(),schema.prefixMap()).flatMap(shapeMap ->
                    shapeMapObj.fixShapeMap(shapeMap,rdfAsRDF4jModel,rdfAsRDF4jModel.getPrefixMap(),schema.prefixMap()).flatMap(fixedShapeMap ->
                    validatorObj.validate(schema,fixedShapeMap,rdfAsRDF4jModel).map(result -> result.toJson())
                    )));
            System.out.println(eitherResult);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception thrown in validateSimple");
        }
    }

}