package au.gov.vic.delwp;

public class AssociatedResource {

	public String ANZLICID;
	public String FileIdentifier;
	public String hostNameForLinks;

  public String getMetadataRecordUrl() {
    return hostNameForLinks + "catalog.search?uuid=" + FileIdentifier;
  }

}
