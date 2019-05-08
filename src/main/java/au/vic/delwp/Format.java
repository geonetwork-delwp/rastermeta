package au.gov.vic.delwp;

public class Format {

	public String format;
	public String storage;
  public Individual formatParty;

  public Individual getMetadataPOC() {
    return Individual.getDefault();
  }
}
