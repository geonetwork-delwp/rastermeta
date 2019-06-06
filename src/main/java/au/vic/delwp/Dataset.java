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

public class Dataset {

	public int ID;
	public String UUID;
	public String ANZLICID;
	public String Title;
	public String CompressionRatio;
	public String VerticalAccuracy;
	public String HorizontalAccuracy;
	public String ProcessingLineage;
	public String DatasetCompleteness;
	public String LogicalConsistency;
  public IDnText DatasetType;
  public IDnText DatasetAssembly;
  public IDnText EPSG;
  public IDnText DatasetFormat;
  public IDnText Tilesize;
	public IDnText ProjectTopic;
  public IDnText QALevel;
  public IDnText Availability;
  public IDnText Platform;
  public PointCloudDetails PointCloud;
  public RasterDetails Raster;
  public ContourDetails Contour;
  public SurveyDetails Survey;
  
	static protected HashMap ClassificationCodes = new HashMap();
	static protected HashMap SpatialRepresentationTypeCodes = new HashMap();

  static protected Date now = new Date(System.currentTimeMillis());
	static protected SimpleDateFormat IS08601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH );


	static {
		MapUtils.Populate( "availabilityToMD_ClassificationCode.txt", ClassificationCodes);
		MapUtils.Populate( "datasetTypeToMD_SpatialRepresentationTypeCode.txt", SpatialRepresentationTypeCodes);
  }

  public String getDatasetName() {
    return "dataset "+Title;
  }

  /* could be more descriptive or if not then use gco:nilReason on abstract */
  public String getAbstract() {
    return "Dataset: "+Title+"\nAssembly: "+getAssembly();
  }

  public XlinkedIndividual getMetadataPOC() {
    return new XlinkedIndividual(Individual.getDefault());
  }

  public XlinkedIndividual getResourcePOC() {
    return new XlinkedIndividual(Individual.getDefault());
  }

  public XlinkedIndividual getDefaultDELWP() {
    return new XlinkedIndividual(Individual.getDefault());
  }

  public boolean hasRefSystem() {
    return (EPSG != null && !StringUtils.isBlank(EPSG.Text));
  }

  public String getSpatialRepresentationType() {
    return (String)SpatialRepresentationTypeCodes.get(DatasetType.Text);
  }

  public boolean hasHorizontalAccuracy() {
    return !Utils.isBlank(HorizontalAccuracy);
  }

  public String getHorizontalAccuracy() {
    if (HorizontalAccuracy.trim().equals("+/- 10cm")) return "0.1";
    else if (HorizontalAccuracy.trim().endsWith("m")) return HorizontalAccuracy.substring(0,HorizontalAccuracy.length()-1);
    else return HorizontalAccuracy;
  }

  public boolean hasVerticalAccuracy() {
    return !Utils.isBlank(VerticalAccuracy);
  }

  public String getVerticalAccuracy() {
    if (VerticalAccuracy.trim().endsWith("m")) return VerticalAccuracy.substring(0,VerticalAccuracy.length()-1);
    else return VerticalAccuracy;
  }

  public boolean hasSurvey() {
    return Survey != null;
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

  public ISODateBlock getDateStamp() throws ParseException {
    ISODateBlock isodb = new ISODateBlock();
    isodb.date = IS08601DateFormat.format(now);
    isodb.dateType = "revision";
    return isodb;
  }

  public boolean hasProcessingLineage() {
    return ProcessingLineage != null;
  }

  public boolean hasDataQualityInfo() {
    return hasLogicalConsistency() || hasDatasetCompleteness();
  }

  public boolean hasLogicalConsistency() {
    return LogicalConsistency != null;
  }

  public boolean hasDatasetCompleteness() {
    return DatasetCompleteness != null;
  }

  public String getDatasetType() {
    return DatasetType.Text;
  }

  public boolean hasDatasetFormat() {
    return DatasetFormat != null;
  }

  public String getDatasetFormat() {
    return DatasetFormat.Text;
  }

  public String getPlatformType() {
    return Platform.Text;
  }

  public String getAssembly() {
    return DatasetAssembly.Text;
  }

  public String getTilesize() {
    if (Tilesize.Text.equals("1:1million mapsheet")) return "1million mapsheet";
    else return Tilesize.Text;
  }

  public String getProjectTopic() {
    return StringUtils.uncapitalize(ProjectTopic.Text);
  }

  public String getEPSGCode() {
    return EPSG.Text;
  }

  public boolean isPointCloud() {
    return (PointCloud != null);
  }

  public boolean isRaster() {
    return (Raster != null);
  }

  public boolean isContour() {
    return (Contour != null);
  }

  public String getUUID() {
    return DigestUtils.sha1Hex(ANZLICID);
  }

  public boolean acquisitionStatusIsUnknown() {
    return (Availability == null || Availability.ID == 0);
  }
      
  public boolean acquisitionStatusIsKnown() {
    return !acquisitionStatusIsUnknown();
  }
      
  public String getAcquisitionStatus() {
    // every other availability means that the acquisition is complete
    return "completed";
  }

}
