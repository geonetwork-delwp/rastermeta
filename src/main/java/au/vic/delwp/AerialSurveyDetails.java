package au.gov.vic.delwp;

import org.apache.commons.lang3.StringUtils;

public class AerialSurveyDetails {

	public int ID;
	public String Runs;
	public String SwathWidth;
	public String ForwardOverlap;
	public String SideOverlap;
	public String FlightHeight;
	public IDnText FlyingHeightUnit;
	public IDnText RunOrientation;

  public boolean hasRuns() {
    return !StringUtils.isBlank(Runs);
  }

  public boolean hasSwathWidth() {
    return !StringUtils.isBlank(SwathWidth);
  }

  public boolean hasForwardOverlap() {
    return !StringUtils.isBlank(ForwardOverlap);
  }

  public boolean hasSideOverlap() {
    return !StringUtils.isBlank(SideOverlap);
  }

  public boolean hasFlightHeight() {
    return !StringUtils.isBlank(FlightHeight);
  }

  public boolean hasFlyingHeightUnit() {
    return (FlyingHeightUnit != null) && !StringUtils.isBlank(FlyingHeightUnit.Text);
  }

  public boolean hasRunOrientation() {
    return (RunOrientation != null) && !StringUtils.isBlank(RunOrientation.Text);
  }

  public String getRunOrientation() {
    return RunOrientation.Text;
  }

  public String getFlyingHeightUnit() {
    return FlyingHeightUnit.Text;
  }
}
