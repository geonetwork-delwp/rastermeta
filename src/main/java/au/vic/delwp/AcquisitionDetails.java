package au.gov.vic.delwp;

import org.apache.commons.lang3.StringUtils;

public class AcquisitionDetails {

	public int ID;
	public String Program;
	public String ContractNumber;
	public String ScheduleNumber;
	public String Component;
	public String AcquisitionYear;
	public Individual Supplier;
	public IDnText AcquisitionMethod;
  public Project project;

  public String getBeginningDate() {
    System.out.println(toString());
    return StringUtils.substringBefore(AcquisitionYear,"-");
  }

  public String getEndingDate() {
    String prefix = StringUtils.substringBefore(AcquisitionYear,"-");
    String after = StringUtils.substringAfter(AcquisitionYear,"-");
    return StringUtils.left(prefix, prefix.length() - after.length()) + after;
  }

  public boolean hasSupplier() {
    return (Supplier != null);
  }

  public String getAcquisitionMethod() {
    return AcquisitionMethod.Text;
  }

  @Override
  public String toString() {
    StringBuilder stuff = new StringBuilder();
    stuff.append("ID: "+ID);
    stuff.append("Program: "+Program);
    stuff.append("AcquisitionYear: "+AcquisitionYear);
    return stuff.toString();
  }

}
