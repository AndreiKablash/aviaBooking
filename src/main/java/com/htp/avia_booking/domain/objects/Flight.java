package com.htp.avia_booking.domain.objects;

import com.htp.avia_booking.domain.Entity;
import com.htp.avia_booking.domain.source.objects.Aircraft;
import com.htp.avia_booking.domain.source.objects.City;

import java.util.Calendar;
import java.util.Objects;

public class Flight extends Entity {

    private String duration; //being absent in dataBase, used for output of date format information
    private String code;
    private Calendar dateDepart;
    private Calendar dateArrival;
    private Aircraft aircraft;
    private City cityFrom;
    private City cityTo;

    public Flight() {
        super();
    }

    public Flight(long id) {
        super(id);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Calendar getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Calendar dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Calendar getDateArrival() {
        return dateArrival;
    }

    public void setDateArrival(Calendar dateArrival) {
        this.dateArrival = dateArrival;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public City getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(City cityFrom) {
        this.cityFrom = cityFrom;
    }

    public City getCityTo() {
        return cityTo;
    }

    public void setCityTo(City cityTo) {
        this.cityTo = cityTo;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
