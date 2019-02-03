package com.htp.avia_booking.dao.interfaces;

import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.GenericDAO;
import com.htp.avia_booking.domain.objects.Reservation;

import java.util.Calendar;

public interface ReservationDao extends GenericDAO<Reservation> {

    Reservation getReservationByDocumentNumber(String document_id) throws DaoException;

    Reservation getReservationBySurname(String surname) throws DaoException;

    Reservation getReservationByCode(String code) throws DaoException;

    Reservation getReservationByDateReserve(Calendar dateReservation)throws DaoException;

}
