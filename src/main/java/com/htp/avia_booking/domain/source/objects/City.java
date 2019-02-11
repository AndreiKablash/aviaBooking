package com.htp.avia_booking.domain.source.objects;

import com.htp.avia_booking.domain.Entity;

import java.util.Objects;

public class City extends Entity {

    private String name;
    private String code;
    private Country country;
    private String description;

    public City() {
        super();
    }

    public City(long id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(name, city.name) &&
                Objects.equals(code, city.code) &&
                Objects.equals(country, city.country) &&
                Objects.equals(description, city.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code, country, description);
    }

    @Override
    public String
    toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", country=" + country +
                ", description='" + description + '\'' +
                '}';
    }
}
