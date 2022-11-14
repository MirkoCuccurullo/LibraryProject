package com.example.libraryproject.controllers;

import com.example.libraryproject.LibraryApplication;
import com.example.libraryproject.database.Database;
import com.example.libraryproject.structure.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.example.libraryproject.controllers.MainController.STYLE_SHEET;

public class LoginController {

    private final Database db;
    @FXML
    private Button buttonLogin;
    @FXML
    private PasswordField fieldPassword;
    @FXML
    private TextField fieldUsername;
    @FXML
    private Label labelError;


    public LoginController(Database db) {
        this.db = db;
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException {

        for (User user : db.users) {
            if (isValidLogin(user)) {

                MainController mainController = new MainController(user, db);
                loadScene(mainController, "main-view.fxml");
            } else {
                labelError.setText("wrong username or password");
            }
        }
    }

    public void loadScene(Object controller, String fxmlFileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource(fxmlFileName));
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 650, 450);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLE_SHEET)).toExternalForm());
        Stage loginStage = (Stage) buttonLogin.getScene().getWindow();
        loginStage.setTitle("Main");
        loginStage.setScene(scene);

    }

    private boolean isValidLogin(User user) {
        return user.getUsername().equals(fieldUsername.getText()) && user.getPassword().equals(fieldPassword.getText());
    }
}