package com.example.socialapp.domain;

import java.io.Serializable;

public class Entity<ID> implements Serializable {

    private static final long serialVerisonUID = 733112453532525L;

    /**
     *  Entity id
     */
    private ID id;

    /**
     * Getter method for Entity id
     * @return - entity id
     */
    public ID getId() { return id; }

    /**
     * Setter method for Entity id
     * @param id - new id
     */
    public void setId(ID id) { this.id = id; }
}