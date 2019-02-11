package com.htp.avia_booking.service.interfaces;

import com.htp.avia_booking.domain.objects.Flight;
import com.htp.avia_booking.domain.source.objects.City;

import java.util.List;

public interface SearchService {

    List<Flight> searchFlight(long date, City cityFrom, City cityTo);
}
