package au.gov.vic.delwp;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import java.io.StringWriter;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import au.csiro.utils.Xml;
import au.csiro.utils.XmlResolver;

import org.jibx.runtime.JiBXException;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import jeeves.JeevesJCS;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileXMLWriter {

  private static final Logger logger = LogManager.getLogger("main");
  private static final String iwsLayersFilename = "iws-layers.xml";


  public static void main( String args[] ){

    final String iwsWMS = "http://images.land.vic.gov.au/erdas-iws/ogc/wms?request=getcapabilities&service=wms";
    String oasisCatalogFile = "schemas/iso19115-3/src/main/plugin/iso19115-3/oasis-catalog.xml";

    try {
      JeevesJCS.setConfigFilename("src/main/config/cache.ccf");
      JeevesJCS.getInstance(XmlResolver.XMLRESOLVER_JCS);
    } catch (Exception ce) {
      logger.error("Failed to create cache for schema files");
    }

    String hostNameForLinks = "https://dev-metashare.maps.vic.gov.au/geonetwork/srv/eng/";
		
		Session src = new Configuration( ).configure("SourceDB.cfg.xml").buildSessionFactory( ).openSession( );

    Options options = new Options();
    options.addOption("h", true, "Specify host name for linkages to metadata records. If not specified then "+hostNameForLinks+" will be used.");
    options.addOption("D", false, "Process rastermeta datasets (mutually exclusive with -P and -c.");
    options.addOption("P", false, "Process rastermeta projects (mutually exclusive with -D and -c.");
    options.addOption("c", false, "Process rastermeta contacts (mutually exclusive with -D and -P");
    options.addOption("q", true, "Specify a query condition eg. ANZLICID = 'ANZVI0803002511' for debugging purposes.");
    options.addOption("s", false, "Skip validation. Useful for debugging because reading the schemas takes some time.");
    options.addRequiredOption("d", "directory", true, "Specify the directory name for output xml files.");

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = null;

    String header = "Convert rastermeta metadata from database to XML\n\n";
 
    HelpFormatter formatter = new HelpFormatter();

    try {
      cmd = parser.parse(options, args);
    } catch (Exception e) {
      formatter.printHelp("rastermetatool", header, options, null, true);
      System.exit(1);
    }

    // Process the hostname if specified
    if (cmd.hasOption("h")) {
      hostNameForLinks = cmd.getOptionValue("h");
    }
    
		/* Process output directory if specifed, otherwise use current directory as default */
		String path = cmd.getOptionValue("d");
		/* Append final separator character if not already suppled */
		if(!path.endsWith(File.separator)) path += File.separator;
    File op = new File(path + "invalid");
    op.mkdirs(); // creates both output directory and invalid directory if they don't exist

    System.setProperty("XML_CATALOG_FILES", oasisCatalogFile);
    Xml.resetResolver();

    String HQL = null;	
    if (cmd.hasOption("P")) {	
		  /* Fetch list of (or iterator over?) projects from Oracle DB */
		  HQL = "FROM Project"; // Build a HQL query string from command line arguments plus some default
		  if( cmd.hasOption("q")) {
        String query = cmd.getOptionValue("q");
        if (query.matches(".*\\S+.*")) HQL += " WHERE " + query;
      }
      logger.info("Requesting rastermeta project records using:\n" + HQL);
  
		  ArrayList projects = (ArrayList) src.createQuery( HQL ).list( );
	  	
		  try {
        for( int i = 0; i < projects.size(); ++i ){
          Project d = (Project) projects.get( i );
          d.hostNameForLinks = hostNameForLinks;
				  d.UUID = d.generateUUID( ); // generate new UUID for dataset
				  logger.info("Processing Project '" + d.Title + "' with ID "+d.ID+" uuid "+d.UUID);
          jibxit(d, cmd, path, d.UUID, null, src, false);
        }
  
		  } catch( org.hibernate.HibernateException e ) {
			  logger.error( "Hibernate exception occurred" );
			  logThrowableMsgStack( e, "N/A" );
			  System.exit( 1 );
		  } finally {
			  src.close( );
			  //dest.close( );
		  }
    } else if (cmd.hasOption("D")) {

      /* First run the iws-wms.xsl to get the layer names from the iws wms in delwp */
      logger.info("Requesting layer names from wms..");
      Map<String,String> xsltparams = new HashMap<String,String>();
      xsltparams.put("wms", iwsWMS);
      String iwsLayersFile = "data" + File.separator + iwsLayersFilename;
      try {
        Element iwslayers = Xml.transform(new Element("nothing"), "data" + File.separator + "iws-wms.xsl", xsltparams);				
        XMLOutputter out = new XMLOutputter();
        Format f = Format.getPrettyFormat();
        f.setEncoding("UTF-8");
        out.setFormat(f);
        FileOutputStream fo = new FileOutputStream(iwsLayersFile);
        out.output(iwslayers, fo);
        fo.close(); 
      } catch (Exception ex) {
			  logger.error( "Cannot get list of layer names from wms server "+iwsWMS );
        ex.printStackTrace();
      }

		  /* Fetch list of (or iterator over?) datasets from Oracle DB */
		  HQL = "FROM Dataset"; // Build a HQL query string from command line arguments plus some default
		  if( cmd.hasOption("q")) {
        String query = cmd.getOptionValue("q");
        if (query.matches(".*\\S+.*")) HQL += " WHERE " + query;
      }
      logger.info("Requesting rastermeta dataset records using:\n" + HQL);
  
		  ArrayList datasets = (ArrayList) src.createQuery( HQL ).list( );
	  	
		  try {
        for( int i = 0; i < datasets.size(); ++i ){
          Dataset d = (Dataset) datasets.get( i );
          d.UUID = d.getUUID();
				  logger.info("Processing Dataset '" + d.Title + "' with ID "+d.ID+" uuid "+d.UUID);
          jibxit(d, cmd, path, d.UUID, d.ANZLICID, src, false);
        }

		  } catch( org.hibernate.HibernateException e ) {
			  logger.error( "Hibernate exception occurred" );
			  logThrowableMsgStack( e, "N/A" );
			  System.exit( 1 );
	  	} finally {
			  src.close( );
			  //dest.close( );
		  }
    } else if (cmd.hasOption("c")) {
      /* Fetch list of (or iterator over?) inidviduals from Oracle DB */
      HQL = "FROM Individual";
      logger.info("Requesting rastermeta individual records using:\n" + HQL);
  
		  ArrayList individuals = (ArrayList) src.createQuery( HQL ).list( );

      individuals.add(Individual.getDefault());
	  	
		  try {
        for( int i = 0; i < individuals.size(); ++i ){
          Individual ind = (Individual) individuals.get( i );
				  logger.info("Processing Individual '" + ind.ID +"'");
          jibxit(ind, cmd, path, ind.ID+"", null, src, true);
        }

		  } catch( org.hibernate.HibernateException e ) {
			  logger.error( "Hibernate exception occurred" );
			  logThrowableMsgStack( e, "N/A" );
			  System.exit( 1 );
	  	} finally {
			  src.close( );
			  //dest.close( );
		  }
    }

  }
	
  private static void jibxit(Object d, CommandLine cmd, String path, String UUID, String ANZLICID, Session src, boolean forceSkip) {
				
		IMarshallingContext mctx = getMarshallingContext( );
				/* Transform Project instance to XML */
				try {
					StringWriter sw = new StringWriter( );
	        mctx.marshalDocument( d, "utf-8", Boolean.FALSE, sw );

          // Now run the created XML through a postprocessing XSLT which does
          // things like add the GML polygons (if anzlic_id matches)
          Element mdXml = Xml.loadString(sw.toString(), false);
          if (ANZLICID != null) {
            Map<String,String> xsltparams = new HashMap<String,String>();
            xsltparams.put("anzlicid", ANZLICID);
            xsltparams.put("iwslayersfile", iwsLayersFilename);
            logger.debug("Transforming "+UUID );
            mdXml = Xml.transform(mdXml, "data" + File.separator + "insert-gml.xsl",  xsltparams);				
          }

	
          boolean xmlIsValid = true;
          if (!(cmd.hasOption("s") || forceSkip)) {
            try {
              Xml.validate(mdXml);
            } catch (Exception e) {
              logger.error("Validation of '"+UUID+"' against http://schemas.isotc211.org/19115/-3/mdb/2.0 FAILED:" );
              logger.error("\n"+e.getMessage());
              xmlIsValid = false;
            }
          }

					/* Write XML out to file */
          String outputFile = path;
          if (!xmlIsValid) {
            outputFile += "invalid" + File.separator;
          } 
          outputFile += UUID + ".xml";
           
         
          XMLOutputter out = new XMLOutputter();
          Format f = Format.getPrettyFormat();
          f.setEncoding("UTF-8");
          out.setFormat(f);
          FileOutputStream fo = new FileOutputStream(outputFile);
          out.output(mdXml, fo);
          fo.close(); 

				} catch( JiBXException e ) {
					/* This usually due to data problems such as unexpected nulls or
					 * referential integrity failures. Once a JiBXException occurs, JiBX's
					 * state is corrupted, hence it must be reinitialised */
					logThrowableMsgStack( e.getRootCause( ), UUID );
					mctx = getMarshallingContext( );					
				} catch( Exception e ) {
					// Write exception info to console, then continue processing next
					logThrowableMsgStack( e, UUID );
				} finally {
					// Have finished with these elements, so purge them from the session
					//dest.evict( m );
					src.evict( d );
				}
  } 			
	
	private static void logThrowableMsgStack( Throwable t, String objName ){

	  StringBuffer ps = new StringBuffer();
	
		ps.append( "Problem whilst processing dataset '" + objName + "'" );
		do ps.append( "Cause: " + t.getMessage( ) + " - " + t.getClass( ).getName( ) );
		while( ( t = t.getCause( ) ) != null );
		ps.append( "Processing of '" + objName + "' aborted" );
    
    logger.error(ps.toString());
	}

		
	private static IMarshallingContext getMarshallingContext( ){
		//Following lines of code reads Jibx binding information and creates context from file binding.xml
		IMarshallingContext mctx;
		try {
			IBindingFactory bfact = BindingDirectory.getFactory( Project.class );
			try {
				mctx = bfact.createMarshallingContext( );
			} catch( JiBXException e ){
				throw new RuntimeException("Problem instantiating JiBX Marshalling Context", e );
			}
		} catch( JiBXException e ){
			throw new RuntimeException("Problem instantiating JiBX Binding Factory", e );
		}
		mctx.setIndent( 1 );
		return mctx;
	}
}
