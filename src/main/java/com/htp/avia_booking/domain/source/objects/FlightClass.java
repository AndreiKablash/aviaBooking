package com.htp.avia_booking.domain.source.objects;

import com.htp.avia_booking.domain.Entity;

public class FlightClass extends Entity {

    private String name;
    private String description;

    public FlightClass() {
        super();
    }

    public FlightClass(long id) {
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
