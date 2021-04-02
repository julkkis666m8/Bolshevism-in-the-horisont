package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GuiMain extends Application {
    public static Main main;

    //public static Main main;

    @Override
    public void start(Stage nationGuiStage) throws Exception {


        new Thread("MainMainThread"){
            public void run(){
                try {
                    main = new main.Main();
                    main.main();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        try {
            Parent nationGuiRoot =
                    FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("nationGui.fxml")));
            Scene nationGuiScene = new Scene(nationGuiRoot);
            //Stage nationGuiStage = new Stage();
            nationGuiStage.setScene(nationGuiScene);
            //nationGuiScene.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("Stylesheet.css")));
            nationGuiStage.setTitle("NationGui");
            nationGuiStage.setResizable(false);
            nationGuiStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
