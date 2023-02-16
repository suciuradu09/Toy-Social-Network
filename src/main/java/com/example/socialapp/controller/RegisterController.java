package com.example.socialapp.controller;

import com.example.socialapp.Main;
import com.example.socialapp.domain.User;
import com.example.socialapp.service.ServiceApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    private ServiceApp service;

    @FXML
    private TextField firstnameRegister;

    @FXML
    private TextField lastnameRegister;

    @FXML
    private TextField usernameRegister;

    @FXML
    private TextField passwordRegister;

    @FXML
    private TextField passwordRegister2;

    @FXML
    private Label registerLabel;

    @FXML
    private Label registerLabel2;

    @FXML
    private CheckBox checkBoxRegister;

    @FXML
    private CheckBox seePasswordCheck;

    public ServiceApp getService(){
        return service;
    }

    public void setService(ServiceApp service) {
        this.service = service;
    }

    public void registerButton() throws IOException {
        if(checkBoxRegister.isSelected())
        {
            String firstname = firstnameRegister.getText();
            String lastname = lastnameRegister.getText();
            String username = usernameRegister.getText();
            String password = passwordRegister.getText();
            for (User user : service.findAll())
            {
                if (user.getUsername().equals(username))
                {
                    registerLabel.setText("Username already exists!");
                    return;
                }
            }
            if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || password.isEmpty())
            {
                registerLabel.setText("Please fill in all the fields!");
            }
            else
            {
                service.saveUser(firstname, lastname, username, password);
                registerLabel.setText("Registration successful!");
                userLogin();
            }
        }
        else
        {
            registerLabel.setText("Please accept the terms and conditions!");
        }
    }

    public void userLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 717, 550);
        Stage stage = (Stage) registerLabel2.getScene().getWindow();
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(this.service);
        stage.setTitle("Login");
        stage.setScene(scene);
    }

    public void seePassword(){
        if (seePasswordCheck.isSelected()) {
            passwordRegister2.setText(passwordRegister.getText());
            passwordRegister2.setVisible(true);
            passwordRegister.setVisible(false);
            return;
        }
        passwordRegister.setText(passwordRegister2.getText());
        passwordRegister.setVisible(true);
        passwordRegister2.setVisible(false);
    }


}
