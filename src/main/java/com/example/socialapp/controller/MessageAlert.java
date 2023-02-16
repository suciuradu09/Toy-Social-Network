package com.example.socialapp.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert {
    public static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.initOwner(owner);
        alert.showAndWait();
    }
    static void showErrorMessage(Stage owner, String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("Mesaj eroare");
        alert.setContentText(text);
        alert.showAndWait();
    }
}
