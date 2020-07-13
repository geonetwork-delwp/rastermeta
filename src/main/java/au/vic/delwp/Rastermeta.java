package au.gov.vic.delwp;

public class Rastermeta {
  private static Rastermeta instance = new Rastermeta();

  private final String rastermetaBaseUUID = "3a82a7f9-c19e-45d1-bdb1-e1a94bf7af00";

  public static Rastermeta getInstance() {
    return instance;
  }

  public String getBaseUUID() { return rastermetaBaseUUID; }
  
}
