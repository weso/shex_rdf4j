package es.weso.shexjava;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.*;

public class ArgsParser {

	public String schema = "";
    static final String schemaAbbr = "s";
    static final String schemaLong = "schema";
	public String data = "";
    static final String dataAbbr = "d";
    static final String dataLong = "data";
    public String shapeMap = "";
    static final String shapeMapAbbr = "m";
    static final String shapeMapLong = "shapeMap";

    public String schemaFormat = "SHEXC";
	public String dataFormat = "Turtle";
    public String shapeMapFormat = "Compact";
    public String outputSchemaFormat = "SHEXC" ;

	public Boolean printTime = false;
	public Boolean verbose = false;
	public Boolean showSchema = false;

	private static final Logger log = Logger.getLogger(ArgsParser.class.getName());
	
	private Options options = new Options();

	private void createOptions() {
		Boolean WithArg = true;
		Boolean NoArg = false;

		Option schemaOpt = new Option(schemaAbbr, schemaLong, WithArg, "Schema file");
		schemaOpt.setRequired(true);
		options.addOption(schemaOpt);

		Option dataOpt = new Option(dataAbbr, dataLong, WithArg, "Data file");
		dataOpt.setRequired(true);
		options.addOption(dataOpt);

		Option shapeMapOpt = new Option(shapeMapAbbr, shapeMapLong, WithArg, "ShapeMap file");
		shapeMapOpt.setRequired(true);
		options.addOption(shapeMapOpt);


		options.addOption("h", "help", NoArg,"show help.");
		options.addOption("v", "verbose", NoArg,"Verbose mode");
		options.addOption("sf", "schemaFormat", WithArg, "Schema Format: (SHEXC by default)");
		options.addOption("ss", "showSchema", NoArg, "Show Schema");
		options.addOption("osf", "outSchemaFormat", WithArg, "Output Schema format");
		options.addOption("df", "dataFormat", WithArg, "Data Format: (Turtle by default)");
		options.addOption("mf", "shapeMapFormat", WithArg, "Shape map format: (Compact by default)");

		options.addOption("t", "time", NoArg,"Print processing time at the end");
	}
	
	public ArgsParser(String...args) {
		createOptions();
		try {
			parse(args);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			help(options);
		}
	}

	String showArgs(String ...args) {
	    StringBuilder sb = new StringBuilder();
	    for(int i=0;i<args.length;i++) { sb.append(" " + args[i]);}
	    return sb.toString();
    }

	public void parse(String... args) {
		log.info("Parsing args " + showArgs(args));

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);

			if (cmd.hasOption("h"))
				help(options);

			if (cmd.hasOption(schemaAbbr)) {
				schema = cmd.getOptionValue(schemaAbbr);
			}

			if (cmd.hasOption(dataAbbr)) {
				data = cmd.getOptionValue(dataAbbr);
			}

            if (cmd.hasOption(shapeMapAbbr)) {
                shapeMap = cmd.getOptionValue(shapeMapAbbr);
            }

			if (cmd.hasOption("sf")) {
				schemaFormat = cmd.getOptionValue("sf");
			}

            if (cmd.hasOption("df")) {
                dataFormat = cmd.getOptionValue("df");
            }

            if (cmd.hasOption("ss")) {
                showSchema = true;
            }

            if (cmd.hasOption("osf")) {
                outputSchemaFormat = cmd.getOptionValue("osf");
            }

            if (cmd.hasOption("mf")) {
                shapeMapFormat = cmd.getOptionValue("mf");
            }

			if (cmd.hasOption("v")) {
				verbose = true;
			}
			
			if (cmd.hasOption("t")) {
				printTime = true;
			}
			
		} catch (ParseException e) {
            log.log(Level.SEVERE, e.getMessage());
            help(options);
        }
		
	}

	private void help(Options options) {
		// This prints out some help
		HelpFormatter formater = new HelpFormatter();

		formater.printHelp("Main", options);
		System.exit(0);
	}
}