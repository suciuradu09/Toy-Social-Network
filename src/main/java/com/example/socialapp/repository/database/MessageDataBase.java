package com.example.socialapp.repository.database;

import com.example.socialapp.domain.Message;
import com.example.socialapp.repository.memory.InMemoryRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class MessageDataBase extends InMemoryRepository<Long, Message> {
    private final String url;
    private final String username;
    private final String password;

    public MessageDataBase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message findOne(Long id) {
        String idS = id.toString();
        if(exists(id)) {
            String sql = "SELECT * from messages where id ='" + idS + "'";
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();

                Long ID = resultSet.getLong("id");
                Long from = resultSet.getLong("from_user");
                Long to = resultSet.getLong("to_user");
                String message = resultSet.getString("message");
                String date = resultSet.getString("date");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime date_final = LocalDateTime.parse(date, dateTimeFormatter);
                Message message1 = new Message(from, to, message, date_final);
                message1.setId(ID);
                return message1;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        String sql = "SELECT * from messages";
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement  = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Long ID = resultSet.getLong("id");
                Long from = resultSet.getLong("from_user");
                Long to = resultSet.getLong("to_user");
                String message = resultSet.getString("message");
                String date = resultSet.getString("date");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime date_final = LocalDateTime.parse(date, dateTimeFormatter);
                Message message1 = new Message(from, to, message, date_final);
                message1.setId(ID);
                messages.add(message1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must be not null");
        }
        String sql = "INSERT INTO messages(id, from_user, to_user, message, date) VALUES(?, ?, ?, ?, ?)";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, entity.getId());
            statement.setLong(2, entity.getFrom());
            statement.setLong(3, entity.getTo());
            statement.setString(4, entity.getMessage());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String date = entity.getDate().format(formatter);
            statement.setString(5, date);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Long id) {
        if(id == null)
            throw new IllegalArgumentException("id must not be null");
        if(exists(id)) {
            String sql = "DELETE FROM messages WHERE id = ?";
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setLong(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
