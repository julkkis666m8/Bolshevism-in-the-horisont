package main;

import controller.BattleController;
import world.Army;
import world.Regiment;

import javax.swing.*;

public class BattleTesterMain {

    public static void main(String[] args) {
        Army armyGer = new Army();
        Army armyPol = new Army();

        for(int i = 0; i < 10 ; i++){
            armyGer.addRegiment(new Regiment("German Regiment"));
            armyPol.addRegiment(new Regiment("Polish Regiment"));
        }
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
