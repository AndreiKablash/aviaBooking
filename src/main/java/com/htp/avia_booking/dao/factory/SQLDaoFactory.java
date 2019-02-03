package com.htp.avia_booking.dao.factory;

import com.htp.avia_booking.dao.impl.sql.*;
import com.htp.avia_booking.dao.interfaces.*;
import com.htp.avia_booking.domain.objects.Reservation;


public class SQLDaoFactory extends DaoFactory {
    private static final SQLDaoFactory instance = new SQLDaoFactory();

    private SQLDaoFactory() {
    }

    public static SQLDaoFactory getInstance() {
        return instance;
    }

    @Override
    public UserDao getUserDao() {
        return SQLUserDao.getInstance();
    }

    @Override
    public AircraftDao getAircraftDao() {
        return SQLAircraftDao.getInstance();
    }

    @Override
    public FlightDao getFlightDao() {
        return SQLFlightDao.getInstance();
    }

    @Override
    public PlaceDao getPlaceDao() {return SQLPlaceDao.getInstance();}

    @Override
    public FlightClassDao getFlightClassDao() {return SQLFlightClassDao.getInstance(); }

    @Override
    public CompanyDao getCompanyDao() {return SQLCompanyDao.getInstance();}

    @Override
    public CountryDao getCountryDao() {return SQLCountryDao.getInstance();}

    @Override
    public RoleDao getRoleDao() {return SQLRoleDao.getInstance();}

    @Override
    public CityDao getCityDao() {return SQLCityDao.getInstance();}

    @Override
    public ReservationDao getReservationDao() { return SQLReservationDao.getInstance();
    }
}
