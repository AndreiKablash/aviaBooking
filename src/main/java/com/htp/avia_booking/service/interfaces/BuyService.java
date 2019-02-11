package com.htp.avia_booking.service.interfaces;

import com.htp.avia_booking.domain.objects.Flight;
import com.htp.avia_booking.domain.objects.User;
import com.htp.avia_booking.domain.source.objects.Place;

public interface BuyService {

    boolean buyTicket(Flight flight, Place place, User user, String addInfo);
}
