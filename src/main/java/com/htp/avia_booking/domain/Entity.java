package com.htp.avia_booking.domain;

import java.io.Serializable;

public class Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    protected long id;

    public Entity() {
    }

    public Entity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
