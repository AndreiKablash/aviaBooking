package com.htp.avia_booking.domain.source.objects;

import com.htp.avia_booking.domain.Entity;

import java.util.Arrays;
import java.util.Objects;

public class Country extends Entity {

    private String name;
    private String code;
    private byte[] icon;

    public Country() {
        super();
    }

    public Country(long id) {
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

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(name, country.name) &&
                Objects.equals(code, country.code) &&
                Arrays.equals(icon, country.icon);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, code);
        result = 31 * result + Arrays.hashCode(icon);
        return result;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", icon=" + Arrays.toString(icon) +
                '}';
    }
}
