package com.example.libraryproject;

import com.example.libraryproject.controllers.LoginController;
import com.example.libraryproject.database.Database;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LibraryApplication extends javafx.application.Application {
    private final Database db = new Database();
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource("login-view.fxml"));
        LoginController controller = new LoginController(db);
        fxmlLoader.setController(controller);

        Scene scene = new Scene(fxmlLoader.load(), 320, 350);
        stage.setTitle("Login");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("controllers/style.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void stop() {
        db.streamDBToFile();
    }

}