package au.gov.vic.delwp;

public class AerialPhoto {

	public int ID;
	public IDnText PhotoType;
	public IDnText Lens;

  public String getPhotoType() {
    return PhotoType.Text;
  }

  public String getLens() {
    return Lens.Text;
  }
}
