package au.gov.vic.delwp;

public class PointCloudDetails {

	public int ID;
	public String PointCloudID;
	public String PulseMode;
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

}
