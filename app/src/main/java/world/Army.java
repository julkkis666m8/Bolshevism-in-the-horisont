package world;

import java.util.ArrayList;
import java.util.List;

public class Army {
    private List<Regiment> regimentList = new ArrayList<>();
    private Nation nation;

    public Army(Nation nation) {
        this.nation = nation;
    }

    public void addRegiment(Regiment regiment){
        regimentList.add(regiment);
        regiment.setName(nation.getNameADJ()+" "+regiment.getName());
    }

    @Override
    public String toString(){
        String string = "";
        string += "Org: "+getTotalOrganization()+"\n";
        string += "Man: "+getTotalManpower()+"\n";
        for(Regiment regiment : regimentList){
            string += "|"+regiment.numTarg+","+regiment.getShortName()+","+(regiment.distTEST-regiment.getRange());
        }
        return string;
    }

    public List<Regiment> getRegiments() {
        return regimentList;
    }

    public double getTotalManpower(){
        double total = 0;
        for (Regiment regiment : getRegiments()){
            total +=regiment.getManpower();
        }
        return total;
    }
    public double getTotalOrganization(){
        double total = 0;
        for (Regiment regiment : getRegiments()){
            total += regiment.getOrganization();
        }
        return total;
    }
}
