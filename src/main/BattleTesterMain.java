package main;

import controller.BattleController;
import world.Army;
import world.Regiment;

import javax.swing.*;

public class BattleTesterMain {

    public static void main(String[] args) {
        Army armyGer = new Army();
        Army armyPol = new Army();

        armyGer.addRegiment(new Regiment("German Artillery",6, 1, 1.2,0.5, true));
        armyGer.addRegiment(new Regiment("German Guards",1,1,1.1,1.2,false));
        armyGer.addRegiment(new Regiment("German Artillery",6, 1, 1.2,0.5,true));
        armyGer.addRegiment(new Regiment("German Guards",1,1,1.1,1.2,false));
        for(int i = 0; i < 10 ; i++){
            armyGer.addRegiment(new Regiment("German Regiment",1,1,1,1,false));
            armyPol.addRegiment(new Regiment("Polish Regiment",1,1,1,1,false));
        }
        armyPol.addRegiment(new Regiment("Polish Hussar",4,2,1.1,1,false));
        armyPol.addRegiment(new Regiment("Polish Hussar",4,2,1.1,1,false));
        armyPol.addRegiment(new Regiment("Polish Hussar",4,2,1.1,1,false));
        armyPol.addRegiment(new Regiment("Polish Hussar",4,2,1.1,1,false));
        BattleController bc = new BattleController();

        bc.startBattle(armyGer,armyPol, null);
        String myString = "";
        while(bc.ongoing()){
            bc.tick();
            myString = "";
            myString += armyGer.toString()+"\n";
            myString += armyPol.toString()+"\n";
            JOptionPane.showMessageDialog(null, myString);
        }

    }
}
