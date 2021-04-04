package main;

import com.sun.javafx.stage.EmbeddedWindow;
import controller.BattleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.BattleGuiController;
import world.Army;
import world.Nation;
import world.Regiment;
import world.World;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class BattleTesterMain {

    private static Stage battleGuiStage;

    public static void main(String[] args) {




        World world = new World();

        Nation germany = new Nation("Germany", "German",world);
        Nation poland = new Nation("Poland", "Polish",world);


        Army armyGer = new Army(germany);
        Army armyPol = new Army(poland);

        armyGer.addRegiment(new Regiment("Artillery",6, 1, 1.2,0.5, true));
        armyGer.addRegiment(new Regiment("Artillery",6, 1, 1.2,0.5,true));
        armyGer.addRegiment(new Regiment("Artillery",6, 1, 1.2,0.5,true));
        armyGer.addRegiment(new Regiment("Artillery",6, 1, 1.2,0.5,true));
        armyGer.addRegiment(new Regiment("Guards",1,1,1.1,1.2,false));
        armyGer.addRegiment(new Regiment("Guards",1,1,1.1,1.2,false));
        armyGer.addRegiment(new Regiment("Guards",1,1,1.1,1.2,false));
        armyGer.addRegiment(new Regiment("Guards",1,1,1.1,1.2,false));
        for(int i = 0; i < 10 ; i++){
            armyGer.addRegiment(new Regiment("Regiment",1,1,1,1.1,false));
            armyPol.addRegiment(new Regiment("Regiment",1,1,1,1.1,false));
        }
        armyPol.addRegiment(new Regiment("Regiment",1,1,1,1.1,false));
        armyPol.addRegiment(new Regiment("Regiment",1,1,1,1.1,false));
        armyPol.addRegiment(new Regiment("Regiment",1,1,1,1.1,false));
        armyPol.addRegiment(new Regiment("Regiment",1,1,1,1.1,false));
        armyPol.addRegiment(new Regiment("Hussar",4,2,1.1,0.9,false));
        armyPol.addRegiment(new Regiment("Hussar",4,2,1.1,0.9,false));
        armyPol.addRegiment(new Regiment("Hussar",4,2,1.1,0.9,false));
        armyPol.addRegiment(new Regiment("Hussar",4,2,1.1,0.9,false));
        BattleController bc = new BattleController();

        bc.startBattle(armyGer,armyPol, null);

        //startBattleGui(bc);  //prob have to do this in an other manner


        String myString = "";
        while(bc.ongoing()){
            bc.tick();
            myString = "";
            myString += armyGer.toString()+"\n";
            myString += armyPol.toString()+"\n";
            JOptionPane.showMessageDialog(null, myString);
        }

    }

    private static void startBattleGui(BattleController bc) {

        new Thread("Battle GUI"){
            public void run(){
                try {

                    try {
                        Parent battleGuiRoot =
                                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("battleGui.fxml")));
                        Scene battleGuiScene = new Scene(battleGuiRoot);
                        //Stage nationGuiStage = new Stage();
                        battleGuiStage.setScene(battleGuiScene);
                        //nationGuiScene.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("Stylesheet.css")));
                        battleGuiStage.setTitle("battleGui");
                        battleGuiStage.setResizable(false);
                        battleGuiStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();    }


}
