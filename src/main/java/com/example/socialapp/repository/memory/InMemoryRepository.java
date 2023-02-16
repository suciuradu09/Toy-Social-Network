package com.example.socialapp.repository.memory;

import com.example.socialapp.domain.Entity;
import com.example.socialapp.exceptions.DuplicateException;
import com.example.socialapp.exceptions.LackException;
import com.example.socialapp.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing InMemoryRepository
 * @param <ID> - entity ID
 * @param <E> - entity type
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    /**
     * Data container
     */
    private Map<ID,E> entities;

    /**
     * Constructor for InMemoryRepository
     */
    public InMemoryRepository() {
        entities = new HashMap<ID,E>();
    }

    /**
     * Method that returns entities with ID, id.
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return - entity
     */
    @Override
    public E findOne(ID id) {
        if(!exists(id))
            throw new LackException("Entity with specified ID doesn't exist.\n\n");
        return entities.get(id);
    }

    /**
     * Method that returns all the entities from the container
     * @return - all entities
     */
    @Override
    public Iterable<E> findAll() {
        if(entities.values().size() == 0){
            throw new LackException("No entities in the container.\n\n");
        }
        return entities.values();
    }

    /**
     * Method stores entities in the container
     * @param entity - entity that will be added in the container
     *         entity must be not null
     * @return - returns the entity if it already exists in  the container, null if successfully added
     */
    @Override
    public E save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null .. \n");
        }
        for (E enti : entities.values()) {
            if (enti.getId() == entity.getId()) {
                throw new DuplicateException("ID already exists!\n");
            }
        }
        entities.put(entity.getId(), entity);
        return null;
    }

    /**
     * Deletes the entity with specified ID
     * @param id - entities id, searched in the container
     *      id must be not null
     * @return - null if deletion completed successfully, otherwise throws LackException
     */
    @Override
    public E delete(ID id) {
        if( entities.get(id) == null)
            throw new LackException("Entity with specified ID doesn't exist!\n\n");
        entities.remove(id);
        return null;
    }

    /**
     * Update method for an entity stored in the container
     * @param entity - new values for searched entity,
     *          entity must not be null
     * @return - null if the update completed successfully, otherwise returns the entity
     */
    @Override
    public E update(E entity) {
        if(!exists(entity.getId()))
            throw new LackException("Entity with specified ID doesn't exist!\n\n");
        entities.put(entity.getId(), entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;
    }

    /**
     * Size of the entities container
     *
     * @return - size of the container
     */
    public int containerSize(){
        return entities.values().size();
    }

    @Override
    public boolean exists(ID id) {
        if(entities.get(id) == null)
            throw new LackException("Entity with specified ID doesn't exist!\n\n");
        return entities.get(id) != null;
    }

    /**
     * Delete all entities from the container
     * @return - List of deleted items from the container
     */
    public List<E> deleteAll(){
        List<E> all = new ArrayList<>();
        if (containerSize() > 0)
        {
            for (E entity : this.findAll()) {
               all.add(entity);
               this.entities.remove(entity.getId());
            }
        }
        return all;
    }
}
