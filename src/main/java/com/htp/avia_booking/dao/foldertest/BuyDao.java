package com.htp.avia_booking.dao.foldertest;

import com.htp.avia_booking.domain.objects.Reservation;
import com.htp.avia_booking.domain.source.objects.Place;
import com.htp.avia_booking.domain.objects.Flight;

public interface BuyDao  {

    Reservation buyTicket(Flight flight, Place place, String addInfo);
}
