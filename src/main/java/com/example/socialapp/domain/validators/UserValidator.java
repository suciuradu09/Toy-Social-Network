package com.example.socialapp.domain.validators;

import com.example.socialapp.domain.User;
import com.example.socialapp.exceptions.ValidationException;

import java.util.Objects;

public class UserValidator implements Validator<User>{
    /**
     * Method that verifies if a user is valid
     * @param entity - the entity that is validated
     * @throws ValidationException - if the entity is not valid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String error = "";

        if( entity.getId() < 0 )
            error += "Id must be a positive integer.\n";
        if(Objects.equals(entity.getFirstName(), ""))
            error += "Firstname is null!";
        if(Objects.equals(entity.getLastName(), ""))
            error += "Lastname is null!";
        if( !Character.isUpperCase(entity.getFirstName().charAt(0)))
            error += "Firstname should have capital letter.\n";
        if( !Character.isUpperCase(entity.getLastName().charAt(0)))
            error += "Lastname should have capital letter.\n";
        if( entity.getFirstName().length() <= 2)
            error += "Firstname should be at least 2 characters long.\n";
        if( entity.getLastName().length() <= 2)
            error += "Lastname should be at least 2 characters long.\n";
        boolean first = entity.getFirstName().chars().allMatch(Character::isLetter);
        if(!first)
            error += "Firstname should contain only letters\n";
        boolean second = entity.getLastName().chars().allMatch(Character::isLetter);
        if(!second)
            error += "Lastname should contain only letters\n";
        if(error.length() > 0)
            throw new ValidationException(error);
    }

    @Override
    public void validateID(User entity) throws ValidationException {
        String error = "";

        if( entity.getId() < 0 )
            error += "Id must be a positive integer.\n";
        if(error.length() > 0)
            throw new ValidationException(error);
    }

}