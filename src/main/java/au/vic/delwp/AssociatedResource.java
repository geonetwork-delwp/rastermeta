package au.gov.vic.delwp;

public class AssociatedResource {

	public String ANZLICID;
	public String Title;
	public String UUID;
	public String hostNameForLinks;

  public String getMetadataRecordUrl() {
    return hostNameForLinks + "catalog.search#/metadata/" + UUID;
  }

}
