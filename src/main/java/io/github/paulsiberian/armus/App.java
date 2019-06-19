package io.github.paulsiberian.armus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public final static String WS_PATH = "/home/paulsiberian/Dev/Armus_Project/test_work_folder";

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        setMinSize(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        setMinSize(scene);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static void setMinSize(Scene scene) {
        var stage = (Stage) scene.getWindow();
        stage.setMinWidth(scene.getRoot().minWidth(0));
        stage.setMinHeight(scene.getRoot().minHeight(0));
    }

    public static void main(String[] args) {
        launch();
    }

}