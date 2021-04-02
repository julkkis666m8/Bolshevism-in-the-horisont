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
    @Override
    public String toString(){
        String string = "";
        for(Regiment regiment : regimentList){
            string += "|"+regiment.getDistFront()+","+regiment.getOrganization();
        }
        return string;
    }
}
