package com.example.socialapp.controller;

import com.example.socialapp.Main;
import com.example.socialapp.Utils.Utils;
import com.example.socialapp.domain.User;
import com.example.socialapp.service.ServiceApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private ServiceApp service;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Label register;

    @FXML
    private Label wrongLogin;

    private ServiceApp getService(){
        return service;
    }

    public void setService(ServiceApp service){
        this.service = service;
    }

    public void userLogin(ActionEvent actionEvent) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        String usernameText = username.getText();
        String passwordText = password.getText();
        if (usernameText.isEmpty() || passwordText.isEmpty()) {
            wrongLogin.setText("Please enter username and password!");
        }
        else{
        for( User user : service.findAll()){
            if(user.getUsername().equals(usernameText)){
                String hashedPassword = null;
                try { hashedPassword = Utils.bytesToHex(Utils.getSHA(passwordText, Utils.hexToBytes(user.getSalt())));
                }catch (Exception e)
                {e.printStackTrace();}
                assert hashedPassword != null;
                if (hashedPassword.equals(user.getPassword())) {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainpage.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 1209, 669);
                    stage.setTitle("Social App");
                    UserController userController = fxmlLoader.getController();
                    userController.setUser(user);
                    userController.setUtilizatorService(service);
                    stage.setScene(scene);
                    stage.show();
                    username.setText("");
                    password.setText("");
                    break;
                }
                else{
                    wrongLogin.setText("Wrong password!");
                    break;
                }
            }
        }
        }
    }

    public void userRegister() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 717, 550);
        Stage stage = (Stage) register.getScene().getWindow();
        RegisterController registerController = fxmlLoader.getController();
        registerController.setService(this.service);
        stage.setTitle("Register");
        stage.setScene(scene);
    }

}
