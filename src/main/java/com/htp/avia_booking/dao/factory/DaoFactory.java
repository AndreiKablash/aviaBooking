package com.htp.avia_booking.dao.factory;

import com.htp.avia_booking.dao.interfaces.*;

public abstract class DaoFactory {
    public static DaoFactory getDaoFactory() {
        return SQLDaoFactory.getInstance();
    }
    public abstract UserDao getUserDao();
    public abstract AircraftDao getAircraftDao();
    public abstract FlightDao getFlightDao();
    public abstract PlaceDao getPlaceDao();
    public abstract FlightClassDao getFlightClassDao();
    public abstract CompanyDao getCompanyDao();
    public abstract CountryDao getCountryDao();
    public abstract RoleDao getRoleDao();
    public abstract CityDao getCityDao();
    public abstract ReservationDao getReservationDao();
}
