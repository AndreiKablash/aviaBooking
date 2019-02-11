package com.htp.avia_booking.domain.source.objects;

import com.htp.avia_booking.domain.Entity;

import java.util.Objects;

public class Place extends Entity {

    private int row;
    private char seat;
    private FlightClass flightClass;
    private boolean busy;

    public Place() {
        super();
    }

    public Place(long id) {
        super(id);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public char getSeat() {
        return seat;
    }

    public void setSeat(char seat) {
        this.seat = seat;
    }

    public FlightClass getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(FlightClass flightClass) {
        this.flightClass = flightClass;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return row == place.row &&
                seat == place.seat &&
                busy == place.busy &&
                Objects.equals(flightClass, place.flightClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, seat, flightClass, busy);
    }

    @Override
    public String toString() {
        return "Place{" +
                "row=" + row +
                ", seat=" + seat +
                ", flightClass=" + flightClass +
                ", busy=" + busy +
                '}';
    }
}
