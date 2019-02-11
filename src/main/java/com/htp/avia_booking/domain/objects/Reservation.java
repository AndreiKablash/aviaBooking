package com.htp.avia_booking.domain.objects;

import com.htp.avia_booking.domain.Entity;
import com.htp.avia_booking.domain.source.objects.Place;

import java.util.Calendar;
import java.util.Objects;

public class Reservation extends Entity {

    private Flight flight;
    private User user;
    private Place place;
    private String code;
    private Calendar reserveDateTime;
    private String addInfo;

    public Reservation() {
        super();
    }

    public Reservation(long id) {
        super(id);
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Calendar getReserveDateTime() {
        return reserveDateTime;
    }

    public void setReserveDateTime(Calendar reserveDateTime) {
        this.reserveDateTime = reserveDateTime;
    }

    public String getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(String addInfo) {
        this.addInfo = addInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(flight, that.flight) &&
                Objects.equals(user, that.user) &&
                Objects.equals(place, that.place) &&
                Objects.equals(code, that.code) &&
                Objects.equals(reserveDateTime, that.reserveDateTime) &&
                Objects.equals(addInfo, that.addInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flight, user, place, code, reserveDateTime, addInfo);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "flight=" + flight +
                ", user=" + user +
                ", place=" + place +
                ", code='" + code + '\'' +
                ", reserveDateTime=" + reserveDateTime +
                ", addInfo='" + addInfo + '\'' +
                '}';
    }
}
