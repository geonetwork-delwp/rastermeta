package au.gov.vic.delwp;

public class PointCloudDetails {

	public int ID;
	public String PointCloudID;
	public String PulseMode;
	public String ReturnType;
	public String ScanRate;
	public String ScanFrequency;
	public String ScanAngle;
	public String FootprintSize;
	public String PointDensityTarget;
	public String PointDensityActual;
	public String PointSpacingTarget;
	public String PointSpacingActual;
	public String EllipsoidProvided;
	public String EllipsoidFormat;
	public String EllipsoidVerticalDatum;
	public String GeoidVerticalDatum;
	public String AdditionalAdjustments;
	public String TidalConditions;
	public String EnvironmentalConditions;

  public boolean hasPulseMode() {
    return PulseMode != null;
  }

  public boolean hasReturnType() {
    return ReturnType != null;
  }

  public boolean hasScanAngle() {
    return ScanAngle != null;
  }

  public boolean hasPointDensityTarget() {
    return PointDensityTarget != null;
  }

  public boolean hasPointSpacingTarget() {
    return PointSpacingTarget != null;
  }

  public boolean hasAdditionalAdjustments() {
    return AdditionalAdjustments != null;
  }

  public boolean hasEnvironmentalConditions() {
    return EnvironmentalConditions != null;
  }

  public boolean hasTidalConditions() {
    return TidalConditions != null;
  }
}
