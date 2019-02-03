package com.htp.avia_booking.domain.source.objects;


import com.htp.avia_booking.domain.Entity;

public class Role extends Entity {

    private String name;
    private String description;

    public Role() {
        super();
    }

    public Role(long id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

