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

    /**
     * Method to find flight in database by three parameters: date of departure, city of departure, city of arrival
     *
     * @param dateTime - day of departure
     * @param cityFrom - city of departure
     * @param cityTo   - city of arrival
     * @return List all flight which are in the database or empty list if no nodes in database
     * @throws DaoException
     */
    List<Flight> getFlight(Calendar dateTime, City cityFrom, City cityTo) throws DaoException;
}
