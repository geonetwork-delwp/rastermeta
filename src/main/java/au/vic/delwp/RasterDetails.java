package au.gov.vic.delwp;

public class RasterDetails {

	public int ID;
	public String RasterID;
	public IDnText RasterType;
	public IDnText Seamlessness;
	public IDnText RectificationType;
	public String XPixelResolution;
	public String YPixelResolution;
	public String NumberBands;
	public String NumberColumns;
	public String NumberRows;
	public IDnText BandList;
	public IDnText ResamplingKernel;
	public IDnText PlatformName;
	public String RectificationNotes;
	public AerialPhoto AerialPhotoDetails;

  public String getRasterTypeCode() {
    return RasterType.Text;
  }

  public String getSeamlessness() {
    return Seamlessness.Text;
  }

  public String getRectificationTypeCode() {
    return RectificationType.Text;
  }

  public boolean hasXPixelResolution() {
    return XPixelResolution != null;
  }

  public boolean doesNotHaveNumberRows() {
    return NumberRows == null;
  }

  public boolean hasNumberRows() {
    return NumberRows != null;
  }

  public boolean hasYPixelResolution() {
    return YPixelResolution != null;
  }

  public boolean doesNotHaveNumberColumns() {
    return NumberColumns == null;
  }

  public boolean hasNumberColumns() {
    return NumberColumns != null;
  }

  public String getBandListCode() {
    return BandList.Text;
  }

  public String getResamplingKernelCode() {
    return ResamplingKernel.Text;
  }

  public String getPlatformCode() {
    return PlatformName.Text;
  }

  public boolean hasRectificationNotes() {
    return RectificationNotes != null;
  }

  public String getRectificationNotes() {
    return RectificationNotes;
  }

  public boolean hasAerialPhoto() {
    return AerialPhotoDetails != null;
  }

  public AerialPhoto getAerialPhoto() {
    return AerialPhotoDetails;
  }

}
