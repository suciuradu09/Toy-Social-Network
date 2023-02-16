package com.example.socialapp.repository.database;

import com.example.socialapp.Utils.Events.FriendshipStatus;
import com.example.socialapp.domain.Friendship;
import com.example.socialapp.repository.memory.InMemoryRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendshipDataBase extends InMemoryRepository<Long, Friendship> {
    private final String url;
    private final String username;
    private final String password;

    public FriendshipDataBase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Friendship findOne(Long aLong) {
        String idS = aLong.toString();
        String sql = "SELECT * from friendship where id = \"id\"";
        try{
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement  = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long ID = resultSet.getLong("id");
            Long id_user1 = resultSet.getLong("id_user1");
            Long id_user2 = resultSet.getLong("id_user2");
            String date = resultSet.getString("date");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime date_final = LocalDateTime.parse(date, dateTimeFormatter);
            Friendship friendship = new Friendship(id_user1, id_user2, date_final);
            friendship.setId(ID);
            return friendship;
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendshipSet = new HashSet<>();
        String sql = "SELECT * from friendship";
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement  = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Long ID = resultSet.getLong("id");
                Long user1 = resultSet.getLong("user1");
                Long user2 = resultSet.getLong("user2");
                String date = resultSet.getString("date");
                String stringStatus = resultSet.getString("status");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime date_final = LocalDateTime.parse(date, dateTimeFormatter);
                Friendship friendship = new Friendship(user1, user2, date_final);
                friendship.setId(ID);
                FriendshipStatus status = FriendshipStatus.valueOf(stringStatus);
                friendship.setStatus(status);
                friendshipSet.add(friendship);
            }
            return friendshipSet;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return friendshipSet;
    }

    @Override
    public Friendship save(Friendship entity) {

        String sql = "insert into friendship (id, user1, user2, date, status) values(?,?,?,?,?)";
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, entity.getId());
            statement.setLong(2, entity.getId1());
            statement.setLong(3, entity.getId2());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String date = entity.getDate().format(formatter);
            statement.setString(4, date);
            statement.setString(5, entity.getStatus().toString());

            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Long id) {
        String idS = id.toString();
        String sql = "delete from friendship where id ='" + idS + "'";
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship update(Friendship entity) {
        String sql = "update friendship set user1 = ?, user2 = ?, date = ?, status = ? WHERE id = ?";
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);;
            statement.setLong(1, entity.getId1());
            statement.setLong(2, entity.getId2());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String dateTime = entity.getDate().format(formatter);
            statement.setString(3,dateTime);
            statement.setString(4, entity.getStatus().toString());
            statement.setLong(5, entity.getId());
            statement.executeUpdate();
            return null;
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
        return super.exists(aLong);
    }

    @Override
    public List<Friendship> deleteAll() {
        return super.deleteAll();
    }
}
