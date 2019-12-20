package es.weso.shexrdf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import cats.effect.IO;
import es.weso.rdf.RDFReader;
import es.weso.rdf.nodes.IRI;
import es.weso.rdf.rdf4j.RDFAsRDF4jModel$;
import es.weso.shapeMaps.*;
import es.weso.shex.Schema;
import es.weso.shex.validator.Validator$;
// import org.apache.jena.rdf.model.Model;
import es.weso.utils.FileUtils;
import org.eclipse.rdf4j.model.Model;
import scala.Option;

import es.weso.rdf.rdf4j.RDFAsRDF4jModel;
import scala.util.Either;
import scala.util.Left;

public class Validator {

    Logger log = Logger.getLogger(Validator.class.getName());

    private static final Option<IRI> noneIri = Option.empty();
    private static final Option<RDFReader> noneReader = Option.empty();
    private static final ShapeMap$ shapeMapObj = ShapeMap$.MODULE$ ;
    private static final Validator$ validatorObj = Validator$.MODULE$ ;
    private static final ResultShapeMap emptyResult = ResultShapeMap$.MODULE$.empty();
    private static final RDFAsRDF4jModel emptyRdf = RDFAsRDF4jModel$.MODULE$.empty().unsafeRunSync();
    private static final Status conformant = Conformant$.MODULE$;
    private static final Status nonConformant = NonConformant$.MODULE$;


    public Either<String,ResultShapeMap> validate(String dataFile,
                         String dataFormat,
                         String schemaFile,
                         String schemaFormat,
                         String shapeMapFile,
                         String shapeMapFormat) throws IOException {
     String strRdf = Files.readString(Paths.get(dataFile));
     String strShEx = Files.readString(Paths.get(schemaFile));
     String strShapeMap = Files.readString(Paths.get(shapeMapFile));
     return emptyRdf.fromString(strRdf,dataFormat,noneIri).flatMap(rdf ->
                Schema.fromString(strShEx, schemaFormat,noneIri, noneReader).flatMap(shex ->
                        ShapeMap.fromString(strShapeMap,shapeMapFormat,noneIri,rdf.getPrefixMap(),shex.prefixMap()).flatMap(shapeMap ->
                                shapeMapObj.fixShapeMap(shapeMap,rdf, rdf.getPrefixMap(),shex.prefixMap())).flatMap(fiexdShapeMap ->
                                 validatorObj.validate(shex,fiexdShapeMap,rdf)
                                )));
    }


    public ResultShapeMap validateModel(Model model, String shEx, String shapeMapStr) throws Exception {
        RDFAsRDF4jModel rdf = new RDFAsRDF4jModel(model,noneIri);
        Either<String,ResultShapeMap> eitherResult = Schema.fromString(shEx,"ShExC",noneIri,noneReader).flatMap(schema ->
                shapeMapObj.fromCompact(shapeMapStr,noneIri,rdf.getPrefixMap(),schema.prefixMap()).flatMap(shapeMap ->
                        shapeMapObj.fixShapeMap(shapeMap,rdf,rdf.getPrefixMap(),schema.prefixMap()).flatMap(fixedShapeMap ->
                                validatorObj.validate(schema,fixedShapeMap,rdf)
                        )));
        // System.out.println(eitherResult);
        return eitherResult.getOrElse(() -> emptyResult);
    }



}