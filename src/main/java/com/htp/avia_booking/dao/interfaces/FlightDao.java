package com.htp.avia_booking.dao.interfaces;

import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.GenericDAO;
import com.htp.avia_booking.domain.objects.Flight;
import com.htp.avia_booking.domain.source.objects.City;

import java.util.Calendar;
import java.util.List;

public interface FlightDao extends GenericDAO<Flight> {
//    //method to check flight
//    boolean checkFlight(Flight flight) throws DaoException;

    //method to find all flight satisfy the condition
    List<Flight> getFlight(Calendar date, City cityFrom, City cityTo) throws DaoException;
}
