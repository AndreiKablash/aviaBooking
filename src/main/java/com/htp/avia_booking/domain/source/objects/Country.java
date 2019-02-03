package com.htp.avia_booking.domain.source.objects;

import com.htp.avia_booking.domain.Entity;

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

}
