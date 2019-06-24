package au.gov.vic.delwp;
  
public class XlinkedIndividual {

  private final Individual ind;

  public XlinkedIndividual(Individual ind) { this.ind = ind; }

  public String getIndividualXlink(){
    return "local://srv/api/registries/entries/"+Utils.generateIndividualUUID(ind.ID+"");
  }

}
