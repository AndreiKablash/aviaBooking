package com.htp.avia_booking.domain.source.objects;

import com.htp.avia_booking.domain.Entity;

public class Place extends Entity {

    private char row;
    private int seat;
    private FlightClass flightClass;
    private boolean busy;

    public Place() {
        super();
    }

    public Place(long id) {
        super(id);
    }

    public char getRow() {
        return row;
    }

    public void setRow(char row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
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
}
