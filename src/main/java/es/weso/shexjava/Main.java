package es.weso.shexjava;

import java.util.logging.Logger;
import es.weso.rdf.RDFBuilder;
import es.weso.rdf.nodes.IRI;
import es.weso.rdf.rdf4j.RDFAsRDF4jModel;
import es.weso.shex.Schema;
import org.joda.time.Instant;
import org.joda.time.Period;


import org.joda.time.format.PeriodFormat;
import scala.Option;
import scala.util.Either;


public class Main {
	static boolean isVerbose = false;

	public static void main(String... args) {
		
		Logger log = Logger.getLogger(Main.class.getName());
		Instant start = Instant.now();
		ArgsParser options = new ArgsParser(args);
		Option<IRI> noneIri = Option.empty();


		isVerbose = options.verbose;
		Validate validator = new Validate();

		verbose("Data: " + options.data +
                ". Schema: " + options.schema +
                ". ShapeMap: " + options.shapeMap
        );

		Either<String,Result> eitherResult = validator.validate(options.data, options.dataFormat,
                   options.schema, options.schemaFormat,
                   options.shapeMap, options.shapeMapFormat);

		String msg = eitherResult.fold(err -> "Error: " + err,
          (Result result) -> {
		    if (options.showSchema) {
		    	RDFBuilder builder = RDFAsRDF4jModel.apply().unsafeRunSync();
		        String outSchema = Schema.serialize(result.getSchema(), options.outputSchemaFormat, noneIri, builder).fold(
		                e -> "Error serializing schema: " + e,
                        str -> str
                );
		        System.out.println(outSchema);
            }
		    return result.getResultShapeMap().toJson().spaces2();
		});
		System.out.println(msg);
		Instant end = Instant.now();
		if (options.printTime) printTime(start,end);
	}

	static void verbose(String msg) {
	  if (isVerbose) {
		System.out.println(msg);
	  }
	}
	
	static void printTime(Instant start,Instant end) {
		Period timeElapsed = new Period(start,end);
     	System.out.println("Time elapsed " + 
   			PeriodFormat.getDefault().print(timeElapsed));
	}

}
