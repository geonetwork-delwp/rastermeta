package au.gov.vic.delwp;

import java.util.ArrayList; 
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.HashMap; 
import java.util.List; 
import java.util.Map;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.regex.*;
import java.text.ParseException;
import java.util.StringTokenizer;

import com.baeldung.uuid.UUIDGenerator;

import org.apache.commons.lang3.StringUtils;

public class Project {

	public int ID;
	public String UUID;
	public String Title;
	public String Name;
	public String Abstract;
	public String Purpose;
	public String Location;
	public IDnText Availability;
  public Individual Owner;
  public Individual Custodian;
  public Individual MetadataAuthor;
  public Date LastUpdated;
  public String ANZLIC_ID = "dunno";
  public AcquisitionDetails ProjectAcquisitionDetails;
  public Set DatasetDetails = new HashSet();
	
	public String hostNameForLinks;

	static protected HashMap ClassificationCodes = new HashMap( );
	static protected SimpleDateFormat IS08601DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH );

	static {
		MapUtils.Populate( "availabilityToMD_ClassificationCode.txt", ClassificationCodes);
		}

	public String generateUUID( ) throws Exception {	
    //return DigestUtils.sha1Hex("urn:au.gov.vic.delwp:rastermeta:project:" + ID);
    Rastermeta rm = Rastermeta.getInstance();
    return UUIDGenerator.generateType5UUID(rm.getBaseUUID(), "urn:au.gov.vic.delwp:rastermeta:project:" + ID).toString();
		}

  public String getAvailability() {
    String result = "Unknown";
    if (Availability != null) {
      result = Availability.Text;
    }
    return result;
	}

  public String getResourceConstraint() {
    String result = (String)ClassificationCodes.get(getAvailability());
    if (result == null) {
      result = "unknown";
    }
    return result;
  }

  public String getProjectName() {
    return "project "+Title;
	}

  public String getName() {
    if (Utils.isBlank(Name)) return Title; 
    else return Name;
	}

  public String getAbstract() {
    if (Utils.isBlank(Abstract)) { return Title; } else { return Abstract; }
	}

  public boolean hasPurpose() {
    return !Utils.isBlank(Purpose);
  }

  public ISODateBlock getDateStamp( ) throws ParseException {
    ISODateBlock db = new ISODateBlock();
    db.date = IS08601DateFormat.format( LastUpdated );
    db.dateType = "revision";
    return db;
  }

  public boolean hasAcquisitionInfo() {
    return (ProjectAcquisitionDetails != null);
  }

  public boolean hasOwner() {
    return (Owner != null);
  }

  public XlinkedIndividual getOwner() {
    return new XlinkedIndividual(Owner);
  }

  public boolean hasCustodian() {
    return (Custodian != null);
  }

  public XlinkedIndividual getCustodian() {
    return new XlinkedIndividual(Custodian);
  }

  public boolean hasMetadataAuthor() {
    if (MetadataAuthor != null) {
      if (!StringUtils.isEmpty(MetadataAuthor.getName())) {
        if (!MetadataAuthor.getName().trim().equals("Raster Metadata Not Entered")) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean hasLocation() {
    return Location != null;
  }

  public XlinkedIndividual getResourcePOC() {
    return new XlinkedIndividual(Individual.getDefault());
  }

  public XlinkedIndividual getDefaultDELWP() {
    return new XlinkedIndividual(Individual.getDefault());
  }

  public XlinkedIndividual getMetadataPOC() {
    return new XlinkedIndividual(Individual.getDefault());
  }

  public XlinkedIndividual getMetadataAuthor() {
    return new XlinkedIndividual(MetadataAuthor);
  }

  public Set getAssociatedResources() throws Exception {
    Set asrs = new HashSet();
    for (Object o : DatasetDetails) { 
      Dataset ds = (Dataset)o;
      AssociatedResource asr = new AssociatedResource();
      asr.ANZLICID = ds.ANZLICID;
      asr.Title = ds.Title;
      asr.UUID = ds.getUUID(); 
      asr.hostNameForLinks = hostNameForLinks; 
      asrs.add(asr);
    }
    return asrs;
  }

  @Override
  public String toString() {
    StringBuilder stuff = new StringBuilder();
    stuff.append("Acq Info: "+ProjectAcquisitionDetails+"\n");
    stuff.append("UUID: "+UUID+"\n");
    stuff.append("Title: "+Title+"\n");
    stuff.append("Name: "+Name+"\n");
    stuff.append("Abstract: "+Abstract+"\n");
    stuff.append("Purpose: "+Purpose+"\n");
    stuff.append("Location: "+Location+"\n");
    String availability = (Availability != null) ? Availability.Text : null;
    stuff.append("Availability: "+availability+"\n");
    stuff.append("LastUpdated: "+LastUpdated+"\n");
/*
public String UUID;
  public String Title;
  public String Name;
  public String Abstract;
  public String Purpose;
  public String Location;
  public IDnText Availability;
  public Individual Owner;
  public Individual Custodian;
  public Individual MetadataAuthor;
  public Date LastUpdated;
  public String ANZLIC_ID = "dunno";
  public AcquisitionDetails ProjectAcquisitionDetails;
  public Set DatasetDetails = new HashSet();
*/
    return stuff.toString();
  }

}
