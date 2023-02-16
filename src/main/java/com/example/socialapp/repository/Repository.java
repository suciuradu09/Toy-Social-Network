package com.example.socialapp.repository;


import com.example.socialapp.domain.Entity;
import com.example.socialapp.exceptions.ValidationException;

public interface Repository<ID, E extends Entity<ID>> {

    /**
     * Repository interface
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    E findOne(ID id);

    /**
     * Find method for repository
     * @return all entities
     */
    Iterable<E> findAll();


    /**
     * Method for adding
     * @param entity
     *         entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     * @throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.
     */
    E save(E entity);


    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    E delete(ID id);


    /**
     * Updates an entity
     * @param entity
     *          entity must not be null
     * @return null - if the entity is updated,
     *                otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */
    E update(E entity);


    /**
     * Method for checking if an entity exists
     * @param id - the id of the entity
     * @return true if the entity exists, false otherwise
     */
    boolean exists(ID id);
}
