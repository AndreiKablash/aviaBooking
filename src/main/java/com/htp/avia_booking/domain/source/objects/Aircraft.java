package com.htp.avia_booking.domain.source.objects;

import com.htp.avia_booking.domain.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Aircraft extends Entity  {

    private String name;
    private Company company;
    private List<Place> placeList = new ArrayList<>();
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

    public List<Place> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(List<Place> placeList) {
            if (placeList != null)
                this.placeList.addAll(placeList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aircraft aircraft = (Aircraft) o;
        return Objects.equals(name, aircraft.name) &&
                Objects.equals(company, aircraft.company) &&
                Objects.equals(placeList, aircraft.placeList) &&
                Objects.equals(description, aircraft.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, company, placeList, description);
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "name='" + name + '\'' +
                ", company=" + company +
                ", placeList=" + placeList +
                ", description='" + description + '\'' +
                '}';
    }
}
