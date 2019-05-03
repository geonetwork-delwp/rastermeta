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

import org.jibx.runtime.JiBXException;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class FileXMLWriter {


    public static void main( String args[] ){

    String hostNameForLinks = "http://localhost:8080/geonetwork/srv/eng/";
		
		Session src = new Configuration( ).configure("SourceDB.cfg.xml").buildSessionFactory( ).openSession( );

    Options options = new Options();
    options.addOption("h", true, "Specify host name for linkages to metadata records. If not specified then http://localhost:8080/geonetwork/srv/eng/ will be used.");
    options.addOption("D", false, "Process rastermeta datasets (mutually exclusive with -P.");
    options.addOption("P", false, "Process rastermeta projects (mutually exclusive with -D.");
    options.addOption("q", true, "Specify a query condition eg. ANZLIC_ID = 'ANZVI0803002511' for debugging purposes.");
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

    String HQL = null;	
    if (cmd.hasOption("P")) {	
		  /* Fetch list of (or iterator over?) projects from Oracle DB */
		  HQL = "FROM Project WHERE ( PROJECTID IS NOT NULL )"; // Build a HQL query string from command line arguments plus some default
		  if( cmd.hasOption("q")) {
        String query = cmd.getOptionValue("q");
        if (query.matches(".*\\S+.*")) HQL += " AND " + query;
      }
      System.out.println("Requesting rastermeta records using:\n" + HQL);
  
		  ArrayList projects = (ArrayList) src.createQuery( HQL ).list( );
	  	
      System.out.println("Found "+projects.size()+" records");
		  try {
        for( int i = 0; i < projects.size(); ++i ){
          Project d = (Project) projects.get( i );
          d.hostNameForLinks = hostNameForLinks;
				  System.out.println("Processing Project '" + d.Name + "'");
				  d.UUID = d.generateUUID( ); // generate new UUID for dataset
          jibxit(d, cmd, path, d.UUID, src);
        }
  
		  } catch( org.hibernate.HibernateException e ) {
			  System.out.println( "Hibernate exception occurred" );
			  logThrowableMsgStack( e, "N/A" );
			  System.exit( 1 );
		  } finally {
			  src.close( );
			  //dest.close( );
		  }
    } else if (cmd.hasOption("D")) {

		  /* Fetch list of (or iterator over?) projects from Oracle DB */
		  HQL = "FROM Dataset WHERE ( DATASETID IS NOT NULL )"; // Build a HQL query string from command line arguments plus some default
		  if( cmd.hasOption("q")) {
        String query = cmd.getOptionValue("q");
        if (query.matches(".*\\S+.*")) HQL += " AND " + query;
      }
      System.out.println("Requesting rastermeta records using:\n" + HQL);
  
		  ArrayList datasets = (ArrayList) src.createQuery( HQL ).list( );
	  	
      System.out.println("Found "+datasets.size()+" records");
		  try {
        for( int i = 0; i < datasets.size(); ++i ){
          Dataset d = (Dataset) datasets.get( i );
				  System.out.println("Processing Dataset '" + d.Title + "'");
          jibxit(d, cmd, path, d.UUID, src);
        }

		  } catch( org.hibernate.HibernateException e ) {
			  System.out.println( "Hibernate exception occurred" );
			  logThrowableMsgStack( e, "N/A" );
			  System.exit( 1 );
	  	} finally {
			  src.close( );
			  //dest.close( );
		  }
    }
  }
	
  private static void jibxit(Object d, CommandLine cmd, String path, String UUID, Session src) {
				
		IMarshallingContext mctx = getMarshallingContext( );
				/* Transform Project instance to XML */
				try {
					StringWriter sw = new StringWriter( );
	        mctx.marshalDocument( d, "utf-8", Boolean.FALSE, sw );
					
					String XML = sw.toString( );
          boolean XMLIsValid = false, ContentIsValid = false;
          if (cmd.hasOption("s")) {
					  System.out.println("Validation is skipped.");
            XMLIsValid = ContentIsValid = true;
          } else {
					  System.out.println("Validating against http://schemas.isotc211.org/19115/-3/mdb/2.0 :" );
					  XMLIsValid = ISO19115Validator.isValid( XML );
					  System.out.println();
					  System.out.println("Validating against ANZLIC Metadata Schematron:");
					  ContentIsValid = ANZLICSchematronValidator.isValid( XML );
          }

					/* Write XML out to file */
          String outputFile = path;
          if (!XMLIsValid) {
            outputFile += "invalid" + File.separator;
          } 
          outputFile += UUID + ".xml";
           
          
					FileWriter fw = new FileWriter( outputFile );
					fw.write( XML );
					fw.close( );

          // Now finally run the created XML through a postprocessing XSLT which does
          // things like add the GML polygons (if anzlic_id matches)
          /*
          Element mdXml = Xml.loadFile(outputFile);
          Map<String,String> xsltparams = new HashMap<String,String>();
          xsltparams.put("anzlicid", d.ANZLIC_ID);
			    System.out.println( "Transforming "+d.UUID );
          Element result = Xml.transform(mdXml, "data" + File.separator + "insert-gml.xsl",  xsltparams);
          //System.out.println("Result was \n"+Xml.getString(result));
          XMLOutputter out = new XMLOutputter();
          Format f = Format.getPrettyFormat();  
          f.setEncoding("UTF-8");
          out.setFormat(f);  
          FileOutputStream fo = new FileOutputStream(outputFile);
          out.output(result, fo);
          fo.close();
          */

					}
				catch( JiBXException e ){
					/* This usually due to data problems such as unexpected nulls or
					 * referential integrity failures. Once a JiBXException occurs, JiBX's
					 * state is corrupted, hence it must be reinitialised */
          e.printStackTrace();
					logThrowableMsgStack( e.getRootCause( ), UUID );
					mctx = getMarshallingContext( );					
					}
				catch( Exception e ){
					/* Write exception info to console, then continue processing next dataset record */
					logThrowableMsgStack( e, UUID );
					}
				finally {
					// Have finished with these elements, so purge them from the session
					//dest.evict( m );
					src.evict( d );
				}
  } 			
	
	private static void logThrowableMsgStack( Throwable t, String objName ){

		java.io.PrintStream ps = System.out;
		
		ps.println( "Problem whilst processing dataset '" + objName + "'" );
		t.printStackTrace(ps);
		do ps.println( "Cause: " + t.getMessage( ) + " - " + t.getClass( ).getName( ) );
		while( ( t = t.getCause( ) ) != null );
		ps.println( "Processing of '" + objName + "' aborted" );
		}

		
	private static IMarshallingContext getMarshallingContext( ){
		//Following lines of code reads Jibx binding information and creates context from file binding.xml
		IMarshallingContext mctx;
		try {
			IBindingFactory bfact = BindingDirectory.getFactory( Project.class );
			try {
				mctx = bfact.createMarshallingContext( );
				}
			catch( JiBXException e ){
				throw new RuntimeException("Problem instantiating JiBX Marshalling Context", e );
				}
			}
		catch( JiBXException e ){
			throw new RuntimeException("Problem instantiating JiBX Binding Factory", e );
			}
		mctx.setIndent( 1 );
		return mctx;
		}
	}
