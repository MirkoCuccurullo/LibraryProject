package com.example.libraryproject.controllers;


import com.example.libraryproject.database.Database;
import com.example.libraryproject.structure.Member;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AddMemberController implements Initializable{
    private final Database db;
    private final MainController controller;
    public static final String EUROPEAN_DATE_FORMAT = "dd-MM-yyyy";
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private DatePicker dpMemberDateOfBirth;
    @FXML
    private Label lblAddMember;

    public AddMemberController(Database db, MainController controller) {
        this.db = db;
        this.controller = controller;
    }

    @FXML
    public void onButtonAddMemberClick() {

        if (tfFirstName.getText().equals("") || tfLastName.getText().equals("") ){
            lblAddMember.setText("Add field need to be filled");
            return;
        }

        Member lastItemInList = db.members.get(db.members.size() - 1);
        int newIndex = lastItemInList.getId() + 1;
        String fistName = tfFirstName.getText();
        String lastName = tfLastName.getText();

        String pickedDate = getDateFromDatePicker();

        if (pickedDate == null) {
            lblAddMember.setText("Please set a valid date");
            return;
        }

        Member member = new Member(newIndex, fistName, lastName, pickedDate);
        db.members.add(member);

        controller.populateMemberTableView();

        onButtonCancelMemberClick();
    }

    @FXML
    public void onButtonCancelMemberClick() {
        Stage currentStage = (Stage) tfLastName.getScene().getWindow();
        currentStage.close();
    }


    private String getDateFromDatePicker() {

        if (dpMemberDateOfBirth.getValue() != null) {
            return dpMemberDateOfBirth.getValue().format(DateTimeFormatter.ofPattern(EUROPEAN_DATE_FORMAT));
        } else {
            try {
                LocalDate.parse(dpMemberDateOfBirth.getEditor().getText(), DateTimeFormatter.ofPattern(EUROPEAN_DATE_FORMAT));
            } catch (DateTimeParseException dtpe) {
                return null;
            }
            return dpMemberDateOfBirth.getEditor().getText();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EUROPEAN_DATE_FORMAT);
        dpMemberDateOfBirth.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (object != null){
                    return formatter.format(object);
                }
                else{
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()){
                    return LocalDate.parse(string, formatter);
                }else{
                    return null;
                }
            }
        });
    }
}
