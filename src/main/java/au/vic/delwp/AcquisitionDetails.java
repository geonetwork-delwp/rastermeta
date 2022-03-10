package au.gov.vic.delwp;

import org.apache.commons.lang3.StringUtils;

public class AcquisitionDetails {

	public int ID;
	public String Program;
	public String ContractNumber;
	public String ScheduleNumber;
	public String Component;
	public String AcquisitionYear;
	public Organisation Supplier;
	public IDnText AcquisitionMethod;
  public Project project;

  public boolean hasAcquisitionYear() {
    return (AcquisitionYear != null);
  }
  public String getBeginningDate() {
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

  public Organisation getSupplier() {
    return Supplier;
  }

  public boolean hasProgram() {
    return (Program != null);
  }

  public boolean hasScheduleNumber() {
    return (ScheduleNumber != null);
  }

  public boolean hasContractNumber() {
    return (ContractNumber != null);
  }

  public String getAcquisitionMethod() {
    return AcquisitionMethod.Text;
  }

  public boolean acquisitionStatusIsUnknown() {
    return (project == null || project.Availability == null || project.Availability.ID == 0);
  }
      
  public boolean acquisitionStatusIsKnown() {
    return !acquisitionStatusIsUnknown();
  }
      
  public String getAcquisitionStatus() {
    // every other availability means that the acquisition is complete
    return "completed";
  }

  @Override
  public String toString() {
    StringBuilder stuff = new StringBuilder();
    stuff.append("ID: "+ID+"\n");
    stuff.append("Program: "+Program+"\n");
    stuff.append("AcquisitionYear: "+AcquisitionYear+"\n");
    stuff.append("Component: "+Component+"\n");
    stuff.append("ContractNumber: "+ContractNumber+"\n");
    stuff.append("ScheduleNumber: "+ScheduleNumber+"\n");
    stuff.append("AcquisitionMethod: "+AcquisitionMethod.Text+"\n");
    return stuff.toString();
  }

}
