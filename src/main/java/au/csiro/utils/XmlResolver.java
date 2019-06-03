package au.csiro.utils;

import jeeves.JeevesJCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.util.XMLCatalogResolver;
import org.jdom.Element;
import org.w3c.dom.ls.LSInput;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* Resolves system and public ids as well as URIs using oasis catalog
   as per XMLCatalogResolver, but goes further and retrieves any
	 external references since we need to use config'd proxy details on 
	 any http connection we make and Xerces doesn't do this (why?)
	 hence this extension.  */
	 
public class XmlResolver extends XMLCatalogResolver {
   
	public static final String XMLRESOLVER_JCS = "XmlResolver";

  private final Logger logger = LogManager.getLogger("resolver");

    
	//--------------------------------------------------------------------------
  /**
    * <p>Constructs a catalog resolver with the given
    * list of entry files.</p>
    * 
    * @param catalogs an ordered array list of absolute URIs
    */
   public XmlResolver (String [] catalogs) {
   	super(catalogs, true);
   }
    
	//--------------------------------------------------------------------------
  /**
    * <p>Resolves any public and system ids as well as URIs
    * from the catalog - also retrieves any external references
    * using Jeeves XmlRequest so that config'd proxy details can 
		* be used on the http connection.</p>
    * 
    * @param type the type of the resource being resolved (usually XML schema)
    * @param namespaceURI the namespace of the resource being resolved, 
		* or <code>null</code> if none was supplied
		* @param publicId the public identifier of the resource being resolved,
    * or <code>null</code> if none was supplied
    * @param systemId the system identifier of the resource being resolved,
    * or <code>null</code> if none was supplied
    * @param baseURI the absolute base URI of the resource being parsed, 
    * or <code>null</code> if there is no base URI
    */
   public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {

     logger.debug("Before resolution: Type: "+type+" NamespaceURI :"+namespaceURI+" PublicId :"+publicId+" SystemId :"+systemId+" BaseURI:"+baseURI);

		LSInput result = super.resolveResource(type, namespaceURI, publicId, systemId, baseURI);

		if (result != null) { // some changes made so update
			publicId = result.getPublicId();
			systemId = result.getSystemId();
			baseURI  = result.getBaseURI();
		}

    logger.debug("After resolution: PublicId :"+publicId+" SystemId :"+systemId+" BaseURI:"+baseURI);

		URL externalRef = null;
		try {

			if (publicId != null && publicId.startsWith("http")) {
				externalRef = new URL(publicId);
			} else if (systemId != null && systemId.startsWith("http")) {
				externalRef = new URL(systemId);
			} else if (systemId != null && baseURI != null) {
				if (baseURI.startsWith("http")) {
					URL ref = new URL(baseURI);
					String thePath = new File(ref.getPath()).getParent().replace('\\','/');
					externalRef = new URI(ref.getProtocol(), null, ref.getHost(), ref.getPort(), thePath + "/" + systemId, null, null).toURL();


				}
			}
		} catch (MalformedURLException e) { // leave this to someone else?
			e.printStackTrace();
			return result; 
		} catch (URISyntaxException e) { // leave this to someone else?
			e.printStackTrace();
			return result; 
		}

		if (externalRef != null) { 

			Element elResult = null;
			
			try {
				elResult = isXmlInCache(externalRef.toString());
			} catch (CacheException e) {
				//System.err.println("Request to cache for "+externalRef+" failed.");
				e.printStackTrace();
			}

			if (elResult == null) { // use XMLRequest to get the XML
				XmlRequest xml = new XmlRequest(externalRef);
				elResult = null;
				try {
					elResult = xml.execute();
					addXmlToCache(externalRef.toString(), elResult);
          logger.debug("Retrieved: \n"+Xml.getString(elResult));
				} catch (Exception e) {
					logger.error("Request on "+externalRef+" failed.");
					e.printStackTrace();
				}

			}

			if (result == null) {
        logger.error("Will try and get "+systemId);
				result = new DOMInputImpl(publicId, systemId, baseURI);
        logger.error("done");
			}
			if (elResult != null) {
				result.setStringData(Xml.getString(elResult));
			}
   	}
    return result;
	}

		
	//--------------------------------------------------------------------------
  /**
    * Clear the cache.
    */
	public void clearCache() throws CacheException {
		JeevesJCS.getInstance(XMLRESOLVER_JCS).clear();
	}

	//--------------------------------------------------------------------------
  /**
    * Add an XLink to the cache.
    */
	public void addXmlToCache(String uri, Element xml) throws CacheException {
		JeevesJCS xmlCache = JeevesJCS.getInstance(XMLRESOLVER_JCS);
		Element cachedXml = (Element) xmlCache.get(uri.toLowerCase());
		if (cachedXml == null) {
			logger.debug("Caching "+uri.toLowerCase());
			xmlCache.put(uri.toLowerCase(), xml);
		}
	}

	//--------------------------------------------------------------------------
	/** Resolves an xlink */
	public Element isXmlInCache(String uri) throws CacheException {
		JeevesJCS xmlCache = JeevesJCS.getInstance(XMLRESOLVER_JCS);
		Element xml = (Element) xmlCache.get(uri.toLowerCase());

		if (xml == null) {
			logger.error("cache MISS on "+uri.toLowerCase());
		} else {
			logger.error("cache HIT on "+uri.toLowerCase());
		}
		return xml;
	}

}
