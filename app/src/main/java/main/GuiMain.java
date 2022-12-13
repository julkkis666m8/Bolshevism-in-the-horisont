package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GuiMain extends Application {
    public static Main main;

    @Override
    public void start(Stage nationGuiStage) {

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
            nationGuiStage.setScene(nationGuiScene);
            nationGuiStage.setTitle("NationGui");
            nationGuiStage.setResizable(false);
            nationGuiStage.show();

            final Stage popGuiStage = new Stage(); //stage.setScene(popGuiScene); stage.show();
            Parent popGuiRoot =
                    FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("gui for thing.fxml")));
            Scene popGuiScene = new Scene(popGuiRoot);
            popGuiStage.setScene(popGuiScene);
            popGuiStage.setTitle("PopGui");
            popGuiStage.setResizable(false);
            popGuiStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
