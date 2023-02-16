package com.example.socialapp.domain.validators;

import com.example.socialapp.exceptions.ValidationException;

public interface Validator<T> {
    /**
     * Method that verifies if a user is valid
     * @param entity - the entity that is validated
     * @throws ValidationException - if the entity is not valid
     */
    void validate(T entity) throws ValidationException;

    void validateID(T entity) throws ValidationException;

}
