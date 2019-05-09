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
	public String ANZLICID;
	public String UUID;
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
    return "dataset "+Title;
  }

  public Individual getMetadataPOC() {
    return Individual.getDefault();
  }

  public Individual getResourcePOC() {
    return Individual.getDefault();
  }

  public boolean hasRefSystem() {
    return (EPSG != null && !StringUtils.isBlank(EPSG.Text));
  }

  public String getSpatialRepresentationType() {
    return (String)SpatialRepresentationTypeCodes.get(DatasetType.Text);
  }

  public boolean hasHorizontalAccuracy() {
    return HorizontalAccuracy != null;
  }

  public boolean hasVerticalAccuracy() {
    return VerticalAccuracy != null;
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
    return Tilesize.Text;
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
    return StringUtils.replace(StringUtils.replace(UUID, "{", ""), "}", "");
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
