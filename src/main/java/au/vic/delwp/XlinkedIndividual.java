package au.gov.vic.delwp;
  
public class XlinkedIndividual {

  private final Individual ind;

  public XlinkedIndividual(Individual ind) { this.ind = ind; }

  public String getIndividualXlink(){
    return "local://xml.metadata.get?uuid="+Utils.generateIndividualUUID(ind.ID+"");
  }

}
