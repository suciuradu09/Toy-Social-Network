package com.example.socialapp.service;

import com.example.socialapp.domain.Entity;

/**
 * Service interface
 * @param <ID> - Entity ID
 * @param <E> - Entity type
 */
public interface Service<ID, E extends Entity<ID>>{

    E findOne(ID id);

    Iterable<E> findAll();

    void saveUser(ID id, String firstName, String lastName, String username, String password);

    E delete(ID id);

    E update(String firstname, String lastname, String username, String password, ID id);
}
