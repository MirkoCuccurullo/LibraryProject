package com.example.libraryproject.controllers;


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

import static com.example.libraryproject.controllers.AddMemberController.EUROPEAN_DATE_FORMAT;


public class EditMemberController implements Initializable {

    private final MainController mainController;
    private final Member member;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private Label lblEditMember;

    public EditMemberController(Member member, MainController mainController) {
        this.mainController = mainController;
        this.member = member;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfFirstName.setText(member.getFirstName());
        tfLastName.setText(member.getSurname());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EUROPEAN_DATE_FORMAT);
        dpBirthDate.setConverter(new StringConverter<LocalDate>() {
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
        LocalDate localDate = LocalDate.parse(member.getDateOfBirth(), formatter);
        dpBirthDate.setValue(localDate);
    }

    @FXML
    public void onButtonCancelClick() {
        Stage currentStage = (Stage) tfLastName.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void onButtonEditMemberClick() {
        if (tfFirstName.getText().equals("") || tfLastName.getText().equals("")) {
            lblEditMember.setText("All field need to be filled");
            return;
        }

        String newFistName = tfFirstName.getText();
        String newLastName = tfLastName.getText();

        String newBirthDate = getDateFromDatePicker(dpBirthDate.getValue());

        if (newBirthDate == null) {
            lblEditMember.setText("Please set a valid date");
            return;
        }

        member.setFirstName(newFistName);
        member.setSurname(newLastName);
        member.setDateOfBirth(newBirthDate);

        mainController.populateMemberTableView();

        onButtonCancelClick();
    }

    private String getDateFromDatePicker(LocalDate oldDate) {

        if (!dpBirthDate.getValue().equals(oldDate)) {
            return dpBirthDate.getValue().format(DateTimeFormatter.ofPattern(EUROPEAN_DATE_FORMAT));
        } else {
            try {
                LocalDate.parse(dpBirthDate.getEditor().getText(), DateTimeFormatter.ofPattern(EUROPEAN_DATE_FORMAT));
            } catch (DateTimeParseException dtpe) {
                return null;
            }
            return dpBirthDate.getEditor().getText();
        }
    }

}
