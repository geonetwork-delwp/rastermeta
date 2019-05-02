package au.gov.vic.delwp;

public class Dataset {

	public int ID;
	public String ANZLICID;
	public String FileIdentifier;
	public IDnText ProjectTopic;
  public String hostNameForLinks;

  public String getMetadataRecordUrl() {
    return hostNameForLinks + "catalog.search?uuid=" + FileIdentifier;
  }
}
