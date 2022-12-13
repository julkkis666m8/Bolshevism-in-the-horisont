package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class PopWindowMain extends Application {
    public static Main main;

    @Override
    public void start(Stage nationGuiStage) {

        try {
            Parent popGuiRoot =
                    FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("gui for thing.fxml")));
            Scene popGuiScene = new Scene(popGuiRoot);
            nationGuiStage.setScene(popGuiScene);
            nationGuiStage.setTitle("PopGui");
            nationGuiStage.setResizable(false);
            nationGuiStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
