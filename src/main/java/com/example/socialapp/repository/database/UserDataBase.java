package com.example.socialapp.repository.database;

import com.example.socialapp.domain.User;
import com.example.socialapp.repository.memory.InMemoryRepository;


import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDataBase extends InMemoryRepository<Long, User> {

    private final String url;
    private final String username;
    private final String password;

    public UserDataBase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public User findOne(Long id) {
        String idS = id.toString();
        if(exists(id)) {
            String sql = "SELECT * from users where id ='" + idS + "'";
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();

                Long ID = resultSet.getLong("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String username = resultSet.getString("username");
                String password_user = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(firstName, lastName, username, password_user, salt);
                user.setId(ID);
                return user;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * from users";
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement  = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Long ID = resultSet.getLong("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(firstName, lastName, username, password, salt);
                user.setId(ID);
                users.add(user);
            }
            return users;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {

        String sql = "INSERT INTO users (id, firstname, lastname, username, password, salt) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getUsername());
            statement.setString(5, entity.getPassword());
            statement.setString(6, entity.getSalt());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long id) {
        String idS = id.toString();
        String sql = "DELETE FROM users WHERE id = '" + idS + "'";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User entity) {
        String sql = "UPDATE users SET firstname = ?, lastname = ?, username = ?, password = ? WHERE id = ?";
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement  = connection.prepareStatement(sql);
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setLong(3, entity.getId());
            statement.setString(4, entity.getUsername());
            statement.setString(5, entity.getPassword());
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int containerSize() {
        return super.containerSize();
    }

    @Override
    public boolean exists(Long aLong) {
        Iterable<User> userIterator = findAll();
        for(User user : userIterator) {
            if(user.getId().equals(aLong)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<User> deleteAll() {
        String sql = "DELETE FROM users";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
