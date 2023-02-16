package com.example.socialapp.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{

    /**
     * First name of the user
     */
    private String firstName;
    /**
     * Last name of user
     */
    private String lastName;
    /**
     * username of the user
     */
    private String username;
    /**
     * password of the user
     */
    private String password;
    /**
     * Salted value of the password
     */
    private String salt;

    private List<User> friends;


    /**
     * Constructor of the user
     * @param firstName - firstname of the user
     * @param lastName - lastname of the user
     */
    public User(String firstName, String lastName, String username, String password, String salt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.friends = new ArrayList<>();
    }
    /**
     * Get method for the firstname
     * @return firstname
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get method fot last name
     * @return lastname
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set method for first name
     * @param firstName - new firstname
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set method for friends list
     * @param friends - new friends list
     */

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    /**
     * Set method for lastname
     * @param lastName - new lastname
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * returns the users username
     * @return users username
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets the users username
     * @param username - new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * returns the users password
     * @return users password
     */
    public String getPassword() {
        return password;
    }

    /**
     * sets the users password
     * @param password - new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

/**
     * returns the users salt
     * @return users salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * sets the users salt
     * @param salt - new salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * Method used to print a user
     * @return - printed user
     */
    @Override
    public String toString() {
        return ">> " + this.getId() + " | " + firstName +  " | " + lastName + " | " + username + " | " + password;
    }

    /**
     * Method verifies if two users are the same.
     * @param o - user that is compared to
     * @return - true if the objects are the same, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName());
    }

    /**
     * Method returns the hashcode of a user
     * @return - user's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getPassword(), getSalt());
    }

}
