package com.htp.avia_booking.domain.objects;

import com.htp.avia_booking.domain.Entity;
import com.htp.avia_booking.domain.source.objects.Aircraft;
import com.htp.avia_booking.domain.source.objects.City;

import java.util.Calendar;
import java.util.Objects;

public class Flight extends Entity {

    private String duration; //being absent in dataBase, used for output of date format information
    private boolean existFreePlaces;

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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isExistFreePlaces() {
        return existFreePlaces;
    }

    public void setExistFreePlaces(boolean existFreePlaces) {
        this.existFreePlaces = existFreePlaces;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return existFreePlaces == flight.existFreePlaces &&
                Objects.equals(duration, flight.duration) &&
                Objects.equals(code, flight.code) &&
                Objects.equals(dateDepart, flight.dateDepart) &&
                Objects.equals(dateArrival, flight.dateArrival) &&
                Objects.equals(aircraft, flight.aircraft) &&
                Objects.equals(cityFrom, flight.cityFrom) &&
                Objects.equals(cityTo, flight.cityTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration, existFreePlaces, code, dateDepart, dateArrival, aircraft, cityFrom, cityTo);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "duration='" + duration + '\'' +
                ", existFreePlaces=" + existFreePlaces +
                ", code='" + code + '\'' +
                ", dateDepart=" + dateDepart +
                ", dateArrival=" + dateArrival +
                ", aircraft=" + aircraft +
                ", cityFrom=" + cityFrom +
                ", cityTo=" + cityTo +
                '}';
    }
}
