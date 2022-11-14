package com.example.libraryproject.controllers;

import com.example.libraryproject.structure.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditItemController implements Initializable {

    private final Item item;
    private final MainController mainController;
    @FXML
    private TextField tfAuthor;
    @FXML
    private TextField tfTitle;
    @FXML
    private Button btnEdit;
    @FXML
    private Label lblEditItem;


    public EditItemController(Item item, MainController mainController) {
        this.item = item;
        this.mainController = mainController;


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfAuthor.setText(item.getAuthor());
        tfTitle.setText(item.getTitle());
    }

    @FXML
    public void onButtonCancelClick() {
        Stage currentStage = (Stage) tfTitle.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void onButtonEditClick() {
        if (tfAuthor.getText().equals("") || tfTitle.getText().equals("")) {
            lblEditItem.setText("All field need to be filled");
            return;
        }
        String newAuthor = tfAuthor.getText();
        String newTitle = tfTitle.getText();

        item.setAuthor(newAuthor);
        item.setTitle(newTitle);

        mainController.populateItemTableView();

        onButtonCancelClick();
    }
}
