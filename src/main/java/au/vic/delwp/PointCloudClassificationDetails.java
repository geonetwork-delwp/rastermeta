package au.gov.vic.delwp;

import org.apache.commons.lang3.StringUtils;

public class PointCloudClassificationDetails {

	public int ID;
	public String ClassLevel;
	public String ClassAccuracy;
	public String OtherClass;
	public String Class0;
	public String Class1;
	public String Class2;
	public String Class3;
	public String Class4;
	public String Class5;
	public String Class6;
	public String Class7;
	public String Class8;
	public String Class9;
	public String Class10;
	public String Class12;

  public boolean hasClassLevel() {
    return !StringUtils.isBlank(ClassLevel);
  }

  public boolean hasClassAccuracy() {
    return !StringUtils.isBlank(ClassAccuracy);
  }

  public boolean hasOtherClass() {
    return !StringUtils.isBlank(OtherClass);
  }
}
