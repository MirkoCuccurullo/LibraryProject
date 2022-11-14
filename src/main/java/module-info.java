module com.example.libraryproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.libraryproject to javafx.fxml;
    exports com.example.libraryproject;
    exports com.example.libraryproject.controllers;
    opens com.example.libraryproject.controllers to javafx.fxml;
    exports com.example.libraryproject.structure;
    opens com.example.libraryproject.structure to javafx.fxml;
    exports com.example.libraryproject.enumerations;
    opens com.example.libraryproject.enumerations to javafx.fxml;
    exports com.example.libraryproject.database;
    opens com.example.libraryproject.database to javafx.fxml;
}