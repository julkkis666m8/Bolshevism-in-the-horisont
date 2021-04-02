package world;

import java.util.ArrayList;
import java.util.List;

public class Army {
    private List<Regiment> regimentList = new ArrayList<>();

    public List<Regiment> getRegiments() {
        return regimentList;
    }
    public void addRegiment(Regiment regiment){
        regimentList.add(regiment);
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

    @Override
    public String toString(){
        String string = "";
        string += "Org: "+getTotalOrganization()+"\n";
        string += "Man: "+getTotalManpower()+"\n";
        for(Regiment regiment : regimentList){
            string += "|"+regiment.numTarg+","+(regiment.distTEST-regiment.getRange());
        }
        return string;
    }
}
