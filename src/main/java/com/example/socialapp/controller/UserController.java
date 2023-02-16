package com.example.socialapp.controller;

import com.example.socialapp.Utils.Events.FriendshipStatus;
import com.example.socialapp.Utils.Events.UtilizatorChangeData;
import com.example.socialapp.Utils.Observer.Observer;
import com.example.socialapp.domain.*;
import com.example.socialapp.service.ServiceApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UtilizatorChangeData> {

    private User loggedUser;

    private ServiceApp service;

    ObservableList<User> model = FXCollections.observableArrayList();
    ObservableList<UserDTO> modelFriends = FXCollections.observableArrayList();
    ObservableList<UserDTO> modelRequests = FXCollections.observableArrayList();
    ObservableList<UserDTO> modelSentRequests = FXCollections.observableArrayList();
    ObservableList<FriendChat> modelChatFriends = FXCollections.observableArrayList();
    ObservableList<Label> modelMessages = FXCollections.observableArrayList();

    @FXML
    private Pane profilePane;

    @FXML
    private Label usernameProfile;

    @FXML
    private Label firstnameProfile;

    @FXML
    private Label lastnameProfile;

    @FXML
    private Button profileButton;

    @FXML
    private Button tableUsersButton;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableView<UserDTO> friendsTable;

    @FXML
    private TableColumn<UserDTO, String> friendFirstname;

    @FXML
    private TableColumn<UserDTO, String> friendLastname;

    @FXML
    private TableColumn<UserDTO, Date> friendsFrom;

    @FXML
    private Button deleteFriendButton;

    @FXML
    private Pane paneFriendsTable;

    @FXML
    private Pane chatPane;

    @FXML
    private TableView<UserDTO> tablePending;

    @FXML
    private TableView<UserDTO> requestsSent;

    @FXML
    private TableView<FriendChat> recentMessagesTable;

    @FXML
    private TableColumn<UserDTO, String> requestsSentColumn;

    @FXML
    private TableColumn<UserDTO, String> friendFirstnamePending;

    @FXML
    private TableColumn<UserDTO, String> friendLastnamePending;

    @FXML
    private TableColumn<UserDTO, Date> friendsFromPending;

    @FXML
    private TableColumn<UserDTO, String> recentMessages;

    @FXML
    private Button cancelRequestButton;

    @FXML
    private Button addFriendButton;

    @FXML
    private ListView<Label> messagesListView;

    @FXML
    private Pane paneRequestsTable;

    @FXML
    private TextField searchBar;

    @FXML
    private AnchorPane anchorPaneChat;

    @FXML
    private Label labelSelectChatFriend;

    @FXML
    private TextField textChatInput;

    @FXML
    private Button buttonSend;

    public User getUser(){
        return this.loggedUser;
    }

    public void setUser(User user){
        this.loggedUser = user;
    }

    public void setUtilizatorService(ServiceApp service) {
        this.service = service;
        service.addObserver(this);
        initModel();
    }

    public void initModel(){
        Iterable<User> messages = service.findAll();
        List<User> users = StreamSupport.stream(messages.spliterator(), false).toList();
        model.setAll(users);
        initializeTableUsers();
        initializeTableFriends();
    }

    public void initializeTableUsers(){
        tableView.toFront();
        Iterable<User> messages = service.findAll();
        List<User> users = StreamSupport.stream(messages.spliterator(), false).toList();
        model.setAll(users);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableView.setItems(model);
    }

    public void initializeTableFriends(){
        paneFriendsTable.toFront();
        modelFriends.clear();
        getFriendList();
        friendFirstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        friendLastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        friendsFrom.setCellValueFactory(new PropertyValueFactory<>("date"));
        friendsTable.setItems(modelFriends);
    }

    public void initializeRequests(){
        paneRequestsTable.toFront();
        initializeTableRequests();
        initializeTableSentRequests();
    }

    public void initializeTableRequests(){
        modelRequests.clear();
        getFriendList();
        friendFirstnamePending.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        friendLastnamePending.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        friendsFromPending.setCellValueFactory(new PropertyValueFactory<>("date"));
        tablePending.setItems(modelRequests);
    }

    public void initializeTableSentRequests(){
        modelSentRequests.clear();
        getRequestList();
        requestsSentColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        requestsSent.setItems(modelSentRequests);
    }

    public void initializeTableChatFriends(){
        modelChatFriends.clear();
        getFriendListForChat();
        recentMessages.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        recentMessagesTable.setItems(modelChatFriends);
    }

    public void initializeChatPane(){
        labelSelectChatFriend.toFront();
        chatPane.toFront();
        anchorPaneChat.setVisible(false);
        buttonSend.setVisible(false);
        textChatInput.setPrefWidth(680);
        initializeTableChatFriends();
    }

    public void initializeChatMessages(){
        anchorPaneChat.setVisible(true);
        anchorPaneChat.toFront();
        modelMessages.clear();
        getUserMessages();
        messagesListView.setItems(modelMessages);
    }

    public void getUserMessages(){
        Iterable<Message> messages = service.getAllMessages();
        List<Message> messagesList = new java.util.ArrayList<>(StreamSupport.stream(messages.spliterator(), false).toList());
        messagesList.sort(Comparator.comparing(Message::getDate));
        FriendChat selectedFriend = recentMessagesTable.getSelectionModel().getSelectedItem();
        for ( Message mess : messagesList){
            if (mess.getFrom().equals(loggedUser.getId()) && mess.getTo().equals(selectedFriend.getId())){
                Label label = new Label(mess.getMessage());
                modelMessages.add(label);
                label.setPrefHeight(45);
                label.setPrefWidth(680);
                label.setAlignment(Pos.CENTER_RIGHT);
                label.setFont(new Font(18));
            }
            if (mess.getTo().equals(loggedUser.getId()) && mess.getFrom().equals(selectedFriend.getId())){
                Label label = new Label(mess.getMessage());
                modelMessages.add(label);
                label.setPrefHeight(45);
                label.setPrefWidth(680);
                label.setAlignment(Pos.CENTER_LEFT);
                label.setFont(new Font(18));
            }
        }
    }

    public void getRequestList(){
        Iterable<Friendship> friendships = service.getAllFriendships();
        for( Friendship friendship : friendships){
            if (friendship.getStatus().equals(FriendshipStatus.PENDING))
            {
                if (friendship.getId1().equals(loggedUser.getId())){
                    UserDTO userDTO = new UserDTO(friendship.getId(),
                            service.findOne(friendship.getId2()).getFirstName(),
                            service.findOne(friendship.getId2()).getLastName(),
                            friendship.getDate().toString());
                    modelSentRequests.add(userDTO);
                }
                else if(friendship.getId2().equals(loggedUser.getId())){
                    UserDTO userDTO = new UserDTO(friendship.getId(),
                            service.findOne(friendship.getId1()).getFirstName(),
                            service.findOne(friendship.getId1()).getLastName(),
                            friendship.getDate().toString());
                    modelRequests.add(userDTO);
                }
            }
        }
    }

    public void getFriendListForChat(){
        Iterable<Friendship> friendships = service.getAllFriendships();
        for ( Friendship friendship : friendships) {
            if (friendship.getStatus().equals(FriendshipStatus.ACCEPTED)) {
                if (friendship.getId1().equals(loggedUser.getId())) {
                    FriendChat user = new FriendChat(service.findOne(friendship.getId2()).getFirstName(), service.findOne(friendship.getId2()).getLastName());
                    user.setId(friendship.getId2());
                    modelChatFriends.add(user);
                } else if (friendship.getId2().equals(loggedUser.getId())) {
                    FriendChat user = new FriendChat(service.findOne(friendship.getId1()).getFirstName(), service.findOne(friendship.getId1()).getLastName());
                    user.setId(friendship.getId1());
                    modelChatFriends.add(user);
                }
            }
        }
    }

    public void getFriendList(){
        Iterable<Friendship> friendships = service.getAllFriendships();
        for ( Friendship friendship : friendships){
            if( friendship.getStatus().equals(FriendshipStatus.ACCEPTED)){
                if(friendship.getId1().equals(loggedUser.getId()))
                {
                    UserDTO userDTO = new UserDTO(friendship.getId(),
                            service.findOne(friendship.getId2()).getFirstName(),
                            service.findOne(friendship.getId2()).getLastName(),
                            friendship.getDate().toString());
                    modelFriends.add(userDTO);
                }
                else if ( friendship.getId2().equals(loggedUser.getId())){
                    UserDTO userDTO = new UserDTO(friendship.getId(),
                            service.findOne(friendship.getId1()).getFirstName(),
                            service.findOne(friendship.getId1()).getLastName(),
                            friendship.getDate().toString());
                    modelFriends.add(userDTO);
                }
            }

        }
    }

    public void deleteFriend(){
        UserDTO userDTO = friendsTable.getSelectionModel().getSelectedItem();
        service.deleteFriendship(userDTO.getId());
        initializeTableFriends();
    }

    public void setSearchBar(){
        FilteredList<UserDTO> filteredData = new FilteredList<>(modelFriends, p -> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(userDTO -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (userDTO.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (userDTO.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<UserDTO> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(friendsTable.comparatorProperty());
        friendsTable.setItems(sortedList);
    }

    public void addFriend(){
        String[] resultSeach = searchBar.getText().split(" ");
        String firstName = resultSeach[0];
        String lastName = resultSeach[1];
        AtomicBoolean found = new AtomicBoolean(false);
        try {
            service.findAll().forEach(user -> {
                if (user.getFirstName().equals(firstName) && user.getLastName().equals(lastName)) {
                    service.saveFriend(loggedUser.getId(), user.getId());
                    found.set(true);
                }
            });
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        if (found.get()){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Success", "Friend request sent!");
            modelFriends.clear();
            modelRequests.clear();
            searchBar.clear();
            modelSentRequests.clear();
            getFriendList();
        }
        else{
            MessageAlert.showErrorMessage(null, "User not found!");
        }
    }

    public void onTextWritten(){
        textChatInput.setPrefWidth(550);
        buttonSend.setVisible(true);
    }

    public void acceptButtonClicked(){
        UserDTO userDTO = tablePending.getSelectionModel().getSelectedItem();

        service.updateFriendshipStatus(userDTO.getId());
        initializeTableRequests();
    }

    public void removeButtonClicked(){
        UserDTO userDTO = tablePending.getSelectionModel().getSelectedItem();
        service.deleteFriendship(userDTO.getId());
        initializeTableRequests();
    }

    public void cancelRequestButtonClicked(){
        UserDTO userDTO = requestsSent.getSelectionModel().getSelectedItem();
        service.deleteFriendship(userDTO.getId());
        initializeTableSentRequests();
    }

    public void buttonSendClicked(){
        if(textChatInput.getText().length() > 0)
        {
            FriendChat friendChat = recentMessagesTable.getSelectionModel().getSelectedItem();
            service.saveMessage(loggedUser.getId(), friendChat.getId(), textChatInput.getText());
            textChatInput.clear();
            initializeChatMessages();
        }
    }

    @Override
    public void update(UtilizatorChangeData utilizatorChangeData) {
        initModel();
    }

    public void logoutButton() throws IOException {
        Stage stage = (Stage) profilePane.getScene().getWindow();
        stage.close();
    }

    public void viewProfile(){
        profilePane.toFront();
        usernameProfile.setText(loggedUser.getUsername());
        firstnameProfile.setText(loggedUser.getFirstName());
        lastnameProfile.setText(loggedUser.getLastName());
    }
}
