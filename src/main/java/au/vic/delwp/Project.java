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
	public IDnText DataAvailability;
  public Individual Owner;
  public Individual Custodian;
  public Individual MetadataAuthor;
  public Date LastUpdated;
  public String ANZLIC_ID = "dunno";
	
	public String hostNameForLinks;

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
		return java.util.UUID.randomUUID().toString();
    //return DigestUtils.sha1Hex(ANZLIC_ID);
		}

  public String getProjectName() {
    return "project "+Title;
	}

  public ISODateBlock getDateStamp( ) throws ParseException {
    System.out.println("Date is "+LastUpdated);
    ISODateBlock db = new ISODateBlock();
    db.date = IS08601DateFormat.format( LastUpdated );
    db.dateType = "revision";
    return db;
  }

  public boolean hasOwner() {
    System.out.println("Owner is "+Owner);
    return (Owner != null);
  }

  public Individual getOwner() {
    return Owner;
  }

  public boolean hasCustodian() {
    System.out.println("Custodian is "+Custodian);
    return (Custodian != null);
  }

  public Individual getCustodian() {
    return Custodian;
  }

  public boolean hasPurpose() {
    System.out.println("Purpose is "+Purpose);
    return !StringUtils.isBlank(Purpose);
  }

}
