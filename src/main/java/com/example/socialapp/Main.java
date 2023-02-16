package com.example.socialapp;

import com.example.socialapp.controller.LoginController;
import com.example.socialapp.domain.Friendship;
import com.example.socialapp.domain.Message;
import com.example.socialapp.domain.User;
import com.example.socialapp.domain.validators.UserValidator;
import com.example.socialapp.repository.database.FriendshipDataBase;
import com.example.socialapp.repository.database.MessageDataBase;
import com.example.socialapp.repository.database.UserDataBase;
import com.example.socialapp.repository.memory.InMemoryRepository;
import com.example.socialapp.service.ServiceApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        UserValidator validator = new UserValidator();
        InMemoryRepository<Long, User> userRepoDB = new UserDataBase("jdbc:postgresql://localhost:2486/postgres", "postgres", "admin");
        InMemoryRepository<Long, Friendship> friendshipRepoDB = new FriendshipDataBase("jdbc:postgresql://localhost:2486/postgres", "postgres", "admin");
        InMemoryRepository<Long, Message> messagesDB = new MessageDataBase("jdbc:postgresql://localhost:2486/postgres", "postgres", "admin");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 717, 550);
        primaryStage.setTitle("Social App");
        primaryStage.setScene(scene);
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(new ServiceApp(userRepoDB, friendshipRepoDB, messagesDB, validator));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}