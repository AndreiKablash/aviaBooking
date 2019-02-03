package com.htp.avia_booking.dao.interfaces;

import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.GenericDAO;
import com.htp.avia_booking.domain.source.objects.Place;

public interface PlaceDao extends GenericDAO<Place> {

//    boolean checkPlace(Place place) throws DaoException;
//
//    boolean checkPlace() throws DaoException;

    Place getPlaceByFlightClass(long flightClassId) throws DaoException;

    Place getPlaceByAircraftID(long aircraftId) throws DaoException;

    boolean getPlaceBusy(long aircraftId, long flightId) throws DaoException;
}
