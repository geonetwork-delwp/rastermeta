package au.gov.vic.delwp;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

public class SurveyDetails {

	public int ID;
	public AerialSurveyDetails AerialSurvey;
	public String CloudCoverPercentage;
	public SensorDetails Sensor;
	public Date StartDate;
	public Date EndDate;

	static protected SimpleDateFormat IS08601DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH );

  public boolean hasStartOrEndDate() {
    return (StartDate != null) || (EndDate != null);
  }

  public boolean hasCloudCoverPercentage() {
    return !StringUtils.isBlank(CloudCoverPercentage);
  }

  // this is an extension point as there may be other survey details apart from those
  // relating to platform type aerial
  public boolean hasExtraSurveyDetails() {
    return hasAerialSurveyDetails(); 
  }

  public boolean hasAerialSurveyDetails() {
    return AerialSurvey != null;
  }

  public boolean hasSensorDetails() {
    return Sensor != null;
  }

  public String getBeginningDate() {
    return IS08601DateFormat.format( StartDate );
  }

  public String getEndingDate() {
    return IS08601DateFormat.format( EndDate );
  }
}
