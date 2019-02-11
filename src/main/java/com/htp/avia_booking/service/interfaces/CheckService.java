package com.htp.avia_booking.service.interfaces;

import com.htp.avia_booking.domain.objects.Reservation;

public interface CheckService {

    Reservation checkReservationByCode(String code);

    Reservation checkReservationByDateReserv(String docNumber);

    Reservation checkReservationByDateReserv(long date);

    Reservation checkReservationByFamilyName(String familyName);
}
