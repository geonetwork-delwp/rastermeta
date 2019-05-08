package au.gov.vic.delwp;

public class Satellite {

	public int ID;
	public String Nadir;
	public String ControlPoints;
	public IDnText RawImageFormat;
	public String RawImageStorage;
	public IDnText RectifiedImageFormat;
	public String RectifiedImageStorage;
	public IDnText UnrectifiedImageFormat;
	public String UnrectifiedImageStorage;
	public IDnText ProcessingLevel;
	public IDnText AtmosphericCorrection;

  public boolean hasNadir() {
    return Nadir != null;
  }

  public boolean hasControlPoints() {
    return ControlPoints != null;
  }

  public boolean hasRawImageFormat() {
    return RawImageFormat != null;
  }

  public Format getRawImageFormat() {
    Format f = new Format();
    f.format = RawImageFormat.Text;
    f.storage = RawImageStorage;
    return f;
  }

  public boolean hasRectifiedImageFormat() {
    return RectifiedImageFormat != null;
  }

  public Format getRectifiedImageFormat() {
    Format f = new Format();
    f.format = RectifiedImageFormat.Text;
    f.storage = RectifiedImageStorage;
    return f;
  }

  public boolean hasUnrectifiedImageFormat() {
    return UnrectifiedImageFormat != null;
  }

  public Format getUnrectifiedImageFormat() {
    Format f = new Format();
    f.format = UnrectifiedImageFormat.Text;
    f.storage = UnrectifiedImageStorage;
    return f;
  }

  public boolean hasProcessingLevel() {
    return ProcessingLevel != null;
  }

  public String getProcessingLevel() {
    return ProcessingLevel.Text;
  }

  public boolean hasAtmosphericCorrection() {
    return AtmosphericCorrection != null;
  }

  public String getAtmosphericCorrection() {
    return AtmosphericCorrection.Text;
  }

}
