package au.gov.vic.delwp;

public class SensorDetails {

	public int ID;
	public IDnText Platform;
	public IDnText SensorName;
	public IDnText SensorType;

  public String getSensorName() {
    return SensorName.Text;
  }

  public String getSensorType() {
    return SensorType.Text;
  }

  public String getPlatformType() {
    return Platform.Text;
  }

}
