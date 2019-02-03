package com.htp.avia_booking.domain.source.objects;

import com.htp.avia_booking.domain.Entity;

public class Aircraft extends Entity  {

    private String name;
    private Company company;
    private String description;

    public Aircraft() {
        super();
    }

    public Aircraft(long id) { super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
