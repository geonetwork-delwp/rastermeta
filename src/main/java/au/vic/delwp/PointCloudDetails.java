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
	public IDnText EllipsoidFormat;
	public IDnText EllipsoidVerticalDatum;
	public IDnText GeoidVerticalDatum;
	public String AdditionalAdjustments;
	public String TidalConditions;
	public String EnvironmentalConditions;
	public PointCloudClassificationDetails PointCloudClassification;

  public boolean hasPulseMode() {
    return PulseMode != null;
  }

  public boolean hasReturnType() {
    return ReturnType != null;
  }

  public boolean hasScanAngle() {
    return ScanAngle != null;
  }

  public boolean hasScanRate() {
    return !Utils.isBlank(ScanRate);
  }

  public boolean doesNotHaveScanRate() {
    return Utils.isBlank(ScanRate);
  }

  public boolean hasScanFrequency() {
    return !Utils.isBlank(ScanFrequency);
  }

  public boolean doesNotHaveScanFrequency() {
    return Utils.isBlank(ScanFrequency);
  }

  public boolean hasFootprintSize() {
    return !Utils.isBlank(FootprintSize);
  }

  public boolean doesNotHaveFootprintSize() {
    return Utils.isBlank(FootprintSize);
  }

  public boolean hasPointDensityActual() {
    return !Utils.isBlank(PointDensityActual);
  }

  public boolean doesNotHavePointDensityActual() {
    return Utils.isBlank(PointDensityActual);
  }

  public boolean hasPointSpacingActual() {
    return !Utils.isBlank(PointSpacingActual);
  }

  public boolean doesNotHavePointSpacingActual() {
    return Utils.isBlank(PointSpacingActual);
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
    return GeoidVerticalDatum != null;
  }

  public boolean hasEllipsoidVerticalDatum() {
    return EllipsoidVerticalDatum != null;
  }

  public boolean hasEllipsoidFormat() {
    return EllipsoidFormat != null;
  }

  public boolean hasPointCloudClassificationDetails() {
    return PointCloudClassification != null;
  }

  public String getEllipsoidProvided() {
    if (Utils.isBlank(EllipsoidProvided) || EllipsoidProvided.trim().equals("0")) {
      return "0";
    } else {
      return "1";
    }
  }

  public String getEllipsoidVerticalDatumCode() {
    return EllipsoidVerticalDatum.Text;
  }

  public String getEllipsoidFormatCode() {
    return EllipsoidFormat.Text;
  }

  public String getGeoidVerticalDatumCode() {
    return GeoidVerticalDatum.Text;
  }

}
