package com.example.libraryproject.controllers;


import com.example.libraryproject.LibraryApplication;
import com.example.libraryproject.enumerations.IsItemAvailable;
import com.example.libraryproject.database.Database;
import com.example.libraryproject.structure.Item;
import com.example.libraryproject.structure.Member;
import com.example.libraryproject.structure.User;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.RecordComponent;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable {

    private final Database db;
    private final User currentUser;
    public static final String STYLE_SHEET = "style.css";

    @FXML
    private TextField fieldItemCode;
    @FXML
    private Button btnPayFine;
    @FXML
    private Button buttonReceiveItem;
    @FXML
    private Label lblTotalFine;
    @FXML
    private Label lblLateDays;
    @FXML
    private TextField fieldMemberIdentifier;
    @FXML
    private Label labelLendingPanel;
    @FXML
    private TableView<Item> tvItems;
    @FXML
    private Label labelWelcome;
    @FXML
    private Label labelReceivePanel;
    @FXML
    private TextField fieldReceivedItem;
    @FXML
    private TableView<Member> tvMembers;


    public MainController(User user, Database db) {
        this.currentUser = user;
        this.db = db;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLabelWelcome(currentUser.toString());
        populateItemTableView();
        populateMemberTableView();
    }

    public void populateItemTableView() {
        tvItems.getItems().clear();
        ObservableList<Item> items = FXCollections.observableArrayList(db.items);
        tvItems.setItems(items);
    }

    public void populateMemberTableView() {
        tvMembers.getItems().clear();
        ObservableList<Member> members = FXCollections.observableArrayList(db.members);
        tvMembers.setItems(members);
    }

    @FXML
    public void onButtonRemoveItemClick(ActionEvent action) {
        Item item = tvItems.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + item.getTitle() + " ?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            db.items.remove(item);
            populateItemTableView();
        } else {
            alert.close();
        }

    }

    @FXML
    public void onButtonRemoveMemberClick(ActionEvent action) {

        Member member = tvMembers.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + member.getFirstName() + " ?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            db.members.remove(member);
            populateMemberTableView();
        } else {
            alert.close();
        }
    }

    @FXML
    public void onButtonReceiveClick(ActionEvent event) {

        lblTotalFine.setVisible(false);
        lblLateDays.setVisible(false);
        btnPayFine.setVisible(false);

        if (fieldReceivedItem.getText().equals("")) {
            labelReceivePanel.setTextFill(Color.RED);
            labelReceivePanel.setText("field can not be empty");
            return;
        }

        int itemCode;

        try {
            itemCode = Integer.parseInt(fieldReceivedItem.getText());
        } catch (NumberFormatException pe) {
            itemCode = 0;
        }


        receiveItem(itemCode);
    }
    @FXML
    public void onFieldTextChange(StringProperty observable, String oldString, String newString){

        lblTotalFine.setVisible(false);
        lblLateDays.setVisible(false);
        btnPayFine.setVisible(false);
        buttonReceiveItem.setDisable(false);

        int itemId;
        try{
            itemId = Integer.parseInt(newString);
        }catch (Exception pe){
            itemId = 0;
        }

        Item item = null;

        for (Item i : db.items){
            if (i.getItemCode() == itemId){
                item = i;
            }
        }

        if (item != null) {
            if (item.getLendingDate() != null) {

                long weeks = ChronoUnit.WEEKS.between(item.getLendingDate(), LocalDate.now());
                if (weeks >= 3) {
                    Period period = Period.between(item.getLendingDate(), LocalDate.now());
                    lblTotalFine.setVisible(true);
                    lblLateDays.setVisible(true);
                    btnPayFine.setVisible(true);
                    buttonReceiveItem.setDisable(true);
                    String lblLateDaysText = "Item is late by: ";
                    lblLateDays.setText(lblLateDaysText + period.getDays() + " days");
                    double feeToPay = period.getDays() * 0.10;
                    String lblTotalFineText = "Total fine: €";
                    lblTotalFine.setText(lblTotalFineText + feeToPay);
                }
            }
        }

    }

    @FXML
    public void onButtonPayFineClick(){
        buttonReceiveItem.setDisable(false);
    }

    private void receiveItem(int itemCode) {

        for (Item i : db.items) {

            if (i.getItemCode() == itemCode) {

                if (i.isStatus() == IsItemAvailable.No) {

                    i.setStatus(IsItemAvailable.Yes);
                    i.setReturnDate(null);
                    populateItemTableView();
                    labelReceivePanel.setTextFill(Color.BLACK);
                    labelReceivePanel.setText("received successfully");

                } else {
                    labelReceivePanel.setTextFill(Color.RED);
                    labelReceivePanel.setText("item not landed");
                }
                return;
            }
        }

        labelReceivePanel.setTextFill(Color.RED);
        labelReceivePanel.setText("no item with this id");
    }

    @FXML
    public void onButtonAddItemClick(ActionEvent action) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource("addItem-view.fxml"));
        AddItemController controller = new AddItemController(db, this);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        Stage addItemStage = new Stage();
        addItemStage.setTitle("Add Item");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLE_SHEET)).toExternalForm());
        addItemStage.initModality(Modality.APPLICATION_MODAL);
        addItemStage.setScene(scene);
        addItemStage.show();
    }

    @FXML
    public void onButtonAddMemberClick(ActionEvent action) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource("addMember-view.fxml"));
        AddMemberController controller = new AddMemberController(db, this);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        Stage addMemberStage = new Stage();
        addMemberStage.setTitle("Add Member");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLE_SHEET)).toExternalForm());
        addMemberStage.initModality(Modality.APPLICATION_MODAL);
        addMemberStage.setScene(scene);
        addMemberStage.showAndWait();

    }

    @FXML
    public void onButtonEditItemClick() throws IOException {

        Item item = tvItems.getSelectionModel().getSelectedItem();

        if (item != null) {

            FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource("editItem-view.fxml"));
            EditItemController controller = new EditItemController(item, this);
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load());
            Stage editItemStage = new Stage();
            editItemStage.setTitle("Edit Item");
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLE_SHEET)).toExternalForm());
            editItemStage.initModality(Modality.APPLICATION_MODAL);
            editItemStage.setScene(scene);
            editItemStage.showAndWait();
        }
    }


    @FXML
    public void onButtonEditMemberClick() throws IOException {

        Member member = tvMembers.getSelectionModel().getSelectedItem();

        if (member != null) {

            FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource("editMember-view.fxml"));
            EditMemberController controller = new EditMemberController(member, this);
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load());
            Stage editMemberStage = new Stage();
            editMemberStage.setTitle("Edit Member");
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLE_SHEET)).toExternalForm());
            editMemberStage.initModality(Modality.APPLICATION_MODAL);
            editMemberStage.setScene(scene);
            editMemberStage.showAndWait();
        }
    }

    @FXML
    public void onButtonLendClick(ActionEvent event) {

        if (fieldItemCode.getText().equals("") || fieldMemberIdentifier.getText().equals("")) {
            labelLendingPanel.setTextFill(Color.RED);
            labelLendingPanel.setText("Fields can not be empty!");
            return;
        }

        int itemCode;
        int memberId;


        //if is not a valid integer, set integer to invalid code (0)
        try {
            itemCode = Integer.parseInt(fieldItemCode.getText());
            memberId = Integer.parseInt(fieldMemberIdentifier.getText());
        } catch (NumberFormatException nfe) {
            itemCode = 0;
            memberId = 0;
        }

        lendItem(itemCode, memberId);

    }

    private void lendItem(int itemCode, int memberId) {
        for (Item i : db.items) {

            if (i.getItemCode() == itemCode) {

                if (i.isStatus() == IsItemAvailable.No) {
                    labelLendingPanel.setTextFill(Color.RED);
                    labelLendingPanel.setText("item already lent");
                    break;
                }

                Member u = db.getMemberById(memberId);

                if (u != null) {
                    i.setLendingDate(LocalDate.now());
                    i.setMember(u);
                    i.setStatus(IsItemAvailable.No);
                    LocalDate expectedReturnDate = LocalDate.now().plus(3, ChronoUnit.WEEKS);
                    i.setReturnDate(expectedReturnDate);
                    labelLendingPanel.setTextFill(Color.BLACK);
                    labelLendingPanel.setText("lending successful");
                    refreshFields();
                    populateItemTableView();
                    return;
                } else {
                    labelLendingPanel.setTextFill(Color.RED);
                    labelLendingPanel.setText("no member with this id");
                    break;
                }
            }
            labelLendingPanel.setTextFill(Color.RED);
            labelLendingPanel.setText("no item with this id");
        }
    }

    @FXML
    public void onButtonImportClick(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open csv File");
        File csvFile = fileChooser.showOpenDialog(buttonReceiveItem.getScene().getWindow());

        if (csvFile != null) {
            try {
                Files.readAllLines(Paths.get(csvFile.getPath()))
                        .stream().skip(1)
                        .forEach(line ->
                                db.items.add(
                                        new Item(
                                                Integer.parseInt(line.split(";")[0]),
                                                null,
                                                IsItemAvailable.Yes,
                                                line.split(";")[2],
                                                line.split(";")[1]
                                        )
                                )
                        );
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        populateItemTableView();
    }

    private void refreshFields() {
        fieldItemCode.setText("");
        fieldMemberIdentifier.setText("");
    }


    private void setLabelWelcome(String string) {
        this.labelWelcome.setText(labelWelcome.getText() + string);
    }
}
