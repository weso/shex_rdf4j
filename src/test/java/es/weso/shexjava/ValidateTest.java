package es.weso.shexjava;

import es.weso.rdf.jena.RDFAsJenaModel$;
import es.weso.rdf.nodes.IRI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Assert;
import org.junit.Test;

import es.weso.rdf.PrefixMap;
import es.weso.rdf.jena.RDFAsJenaModel;
import es.weso.schema.Schema;
import es.weso.schema.Schemas;
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


	private es.weso.schema.Result validate(RDFAsJenaModel resourceAsRDFReader, RDFAsJenaModel shapeAsRDFReader) {
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
	    }

    @Test
    public void ValidateSimple() {
        try {
        	Model rdfModel = ModelFactory.createDefaultModel();
			rdfModel.read("data1.ttl");
			RDFAsJenaModel rdf = new RDFAsJenaModel(rdfModel,noneIri,noneIri);
			// RDFAsJenaModel rdf = RDFAsJenaModel.apply(rdfModel,noneIri,noneIri);

        	Model shaclModel = ModelFactory.createDefaultModel();
        	shaclModel.read("schema1.ttl");
        	RDFAsJenaModel shacl = new RDFAsJenaModel(shaclModel,noneIri,noneIri);
        	es.weso.schema.Result result = validate(rdf,shacl);
        	Assert.assertEquals(result.isValid(),true);
		} catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception thrown in validateSimple");
        }
    }
    
    @Test
    public void ValidateSimpleBad() {
        try {
        	Model rdfModel = ModelFactory.createDefaultModel();
        	rdfModel.read("data2.ttl");
        	RDFAsJenaModel rdf =  new RDFAsJenaModel(rdfModel,noneIri,noneIri);

        	Model shaclModel = ModelFactory.createDefaultModel();
        	shaclModel.read("schema1.ttl");
        	RDFAsJenaModel shacl =  new RDFAsJenaModel(shaclModel,noneIri,noneIri);
        	es.weso.schema.Result result = validate(rdf,shacl);
        	Assert.assertEquals(result.isValid(),false);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception thrown in validateSimple");
        }
    }
}