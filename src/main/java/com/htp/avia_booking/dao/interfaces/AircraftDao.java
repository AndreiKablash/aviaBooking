package com.htp.avia_booking.dao.interfaces;

import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.GenericDAO;
import com.htp.avia_booking.domain.source.objects.Aircraft;

public interface AircraftDao extends GenericDAO<Aircraft> {

    Aircraft getAircraftByName(String name) throws DaoException;

}
