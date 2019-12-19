package es.weso.shexjava;

import es.weso.rdf.RDFReader;
import es.weso.shapeMaps.ResultShapeMap;
import es.weso.shapeMaps.ShapeMap;
import es.weso.shex.Schema;
import io.circe.Json;

public class Result {
    public Result(Schema schema,
                  RDFReader rdf,
                  ShapeMap shapeMap,
                  ResultShapeMap resultShapeMap) {
        this.schema = schema ;
        this.rdf = rdf;
        this.shapeMap = shapeMap;
        this.resultShapeMap = resultShapeMap;
    }

    Schema          schema;
    RDFReader       rdf;
    ResultShapeMap  resultShapeMap;
    ShapeMap        shapeMap;

    Schema getSchema() { return schema; }
    ResultShapeMap getResultShapeMap() { return resultShapeMap; }

}
