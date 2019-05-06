package au.gov.vic.delwp;

import org.apache.commons.lang3.StringUtils;

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

  public boolean hasEllipsoidProvided() {
    return !(StringUtils.isBlank(EllipsoidProvided) || EllipsoidProvided.trim().equals("0"));
  }

  public boolean hasGeoidVerticalDatum() {
    return !(StringUtils.isBlank(GeoidVerticalDatum) || GeoidVerticalDatum.trim().equals("0"));
  }

  public String getEllipsoidProvided() {
    if (StringUtils.isBlank(EllipsoidProvided) || EllipsoidProvided.trim().equals("0")) {
      return "0";
    } else {
      return "1";
    }
  }

  public String getEllipsoidVerticalDatumCode() {
    return EllipsoidVerticalDatum;
  }

  public String getEllipsoidFormatCode() {
    return EllipsoidFormat;
  }

  public String getGeoidVerticalDatumCode() {
    return GeoidVerticalDatum;
  }

}
