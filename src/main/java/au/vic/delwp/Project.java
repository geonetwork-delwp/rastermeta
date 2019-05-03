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

import org.apache.commons.codec.digest.DigestUtils;
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
	static protected HashMap MaintenanceFrequencies = new HashMap( );
	static protected HashMap ProgressCodes = new HashMap( );
	static protected HashMap EquivalentScales = new HashMap( );
	static protected HashMap IndeterminateDates = new HashMap( );
	static protected HashMap CRSCodes = new HashMap( );
	static protected HashMap DataTypes = new HashMap( );
	static protected HashMap ScopeCodes = new HashMap( );
	static protected HashMap Products = new HashMap( );
	static protected HashMap Objects = new HashMap( );
	static protected SimpleDateFormat DBDateFormat = new SimpleDateFormat("ddMMMyyyy",Locale.ENGLISH );
	static protected SimpleDateFormat IS08601DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH );

	
	static private final String DEFAULT_DATA_FORMAT = "Most popular formats";
	
	static {
		MapUtils.Populate( "availabilityToMD_ClassificationCode.txt", ClassificationCodes);
		MapUtils.Populate( "reftab__ms2ap_distance.txt", EquivalentScales, ".+\\|\\d+" );
		MapUtils.Populate( "reftab__ms2ap_progress.txt", ProgressCodes);
		MapUtils.Populate( "reftab__ms2ap_maintenance_frequency.txt", MaintenanceFrequencies);
		MapUtils.Populate( "reftab__ms2ap_temporal.txt", IndeterminateDates );
		MapUtils.Populate( "reftab__ms2ap_crscodes.txt", CRSCodes, ".+\\|\\d+" );
		MapUtils.Populate( "reftab__ms2ap_spatial_rep_type.txt", DataTypes, "\\d+\\|.+" );
		MapUtils.PopulateScopeCodeMap( "reftab__ms2ap_scope_with_series.txt", ScopeCodes );
    MapUtils.PopulateMulti( "product_object.csv", Products );
    MapUtils.Populate( "object.csv", Objects );
		}

	public String generateUUID( ){	
    return "urn:au.gov.vic.delwp:rastermeta:project:" + ID;
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
    System.out.println(this.toString());
    return "project "+Title;
	}

  public String getAbstract() {
    if (Abstract == null) { return Title; } else { return Abstract; }
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

  public Individual getOwner() {
    return Owner;
  }

  public boolean hasCustodian() {
    return (Custodian != null);
  }

  public Individual getCustodian() {
    return Custodian;
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

  public Individual getResourcePOC() {
    return Individual.getDefault();
  }

  public Individual getMetadataPOC() {
    return Individual.getDefault();
  }

  public Individual getMetadataAuthor() {
    return MetadataAuthor;
  }

  public boolean hasPurpose() {
    return !StringUtils.isBlank(Purpose);
  }

  public Set getDatasets() {
    System.out.println(toString());
    for (Object o : DatasetDetails) { 
      Dataset ds = (Dataset)o;
      ds.hostNameForLinks = hostNameForLinks; 
    }
    return DatasetDetails;
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
