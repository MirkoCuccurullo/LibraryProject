package com.example.libraryproject.controllers;



import com.example.libraryproject.enumerations.IsItemAvailable;
import com.example.libraryproject.database.Database;
import com.example.libraryproject.structure.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddItemController {
    private final Database db;
    private final MainController mainController;
    @FXML
    private TextField tfAuthor;
    @FXML
    private Label lblAddItem;
    @FXML
    private TextField tfTitle;

    public AddItemController(Database db, MainController controller) {
        this.db = db;
        this.mainController = controller;

    }

    @FXML
    public void onButtonAddClick() {

        if (tfTitle.getText().equals("") || tfAuthor.getText().equals("")) {
            lblAddItem.setText("All field need to be filled");
            return;
        }

        Item lastItemInList = db.items.get(db.items.size() - 1);
        int newIndex = lastItemInList.getItemCode() + 1;
        String title = tfTitle.getText();
        String author = tfAuthor.getText();
        Item item = new Item(newIndex, null, IsItemAvailable.Yes, title, author);
        db.items.add(item);

        mainController.populateItemTableView();

        onButtonCancelClick();

    }

    @FXML
    public void onButtonCancelClick() {
        Stage currentStage = (Stage) tfAuthor.getScene().getWindow();
        currentStage.close();
    }
}
