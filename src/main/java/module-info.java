module com.example.socialapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.socialapp to javafx.fxml;
    opens com.example.socialapp.controller to javafx.fxml;
    opens com.example.socialapp.domain to javafx.base;

    exports com.example.socialapp;
}