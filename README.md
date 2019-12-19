shex_rdf4j
========

Example ShEx client using Java/RDF4_J

This repository contains a simple command line Java program that runs the ShEx validator using RDf4J

The code uses [Maven](https://maven.apache.org/) to manage the project.

# Run the tests

```
mvn test
``` 



# Create a standalone Jar

```
mvn clean compile package
```

# Validating example

The following line validates a turtle file against a ShEx schema using a shape map. 

```
java -jar target/shexjava.jar -d examples/issue.ttl -s examples/issue.shex -m examples/issue.shapeMap
```

It runs ShEx and validates the nodes according to the shapeMap.

The output is a result shape map (in Json format):

```
Result:[
  {
    "node" : "<http://example.org/x>",
    "shape" : "<http://example.org/IssueShape>",
    "status" : "conformant",
    "appInfo" : "Shaclex",
    "reason" : ":unassigned == :unassigned"
  },
  {
    "node" : "<http://example.org/john>",
    "shape" : "<http://example.org/UserShape>",
    "status" : "conformant",
    "appInfo" : "Shaclex",
    "reason" : "John Smith has datatype xsd:string\n<mailto:john@example.org> is an IRI"
  }
]
```

It is also possible to validate data in other formats, like JSON-LD:

```
java -jar target/shexjava.jar -d examples/wot.lsonld -df JSON-LD -s examples/wot.shex -m examples/wot.shapeMap
```

It is also possible to show the schema in different formats with the option `showSchema`. The following example 
 validates and shows the ShEx schema in Turtle format:
 
```
java -jar target/shex_rdf4j.jar -d examples/issue.ttl -s examples/issue.shex -m examples/issue.shapeMap --showSchema --outSchemaFormat Turtle
```
