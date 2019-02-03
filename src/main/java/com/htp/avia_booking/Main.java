package com.htp.avia_booking;

import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.connection_pool.ConnectionPool;
import com.htp.avia_booking.dao.connection_pool.ConnectionPoolException;
import com.htp.avia_booking.dao.factory.DaoFactory;
import com.htp.avia_booking.dao.interfaces.AircraftDao;
import com.htp.avia_booking.dao.interfaces.CityDao;
import com.htp.avia_booking.dao.interfaces.CompanyDao;
import com.htp.avia_booking.dao.interfaces.FlightClassDao;
import com.htp.avia_booking.domain.source.objects.Aircraft;
import com.htp.avia_booking.domain.source.objects.City;
import com.htp.avia_booking.domain.source.objects.Company;
import com.htp.avia_booking.domain.source.objects.FlightClass;

import java.util.ArrayList;

public class Main {
    private static final DaoFactory factory = DaoFactory.getDaoFactory();
    public static void main(String[] args) throws DaoException, ConnectionPoolException{
        ConnectionPool.getInstance().init();
//
//        AircraftDao aircraftDao = factory.getAircraftDao();
//        Aircraft aircraft = new Aircraft();
//        aircraft.setName("Embraer 170");
//        aircraft.setDescription("no description");
//
//
//        CompanyDao companyDao = factory.getCompanyDao();
//        Company company = new Company();
//        company.setId(3l);
//        company.setName("Empresa Brasileira de Aeronáutica S.A.");
//        aircraft.setCompany(company);
//        companyDao.create(company);
//
////        aircraftDao.create(aircraft);
//
//        FlightClassDao flightClassDao = factory.getFlightClassDao();
//        FlightClass flightClass = new FlightClass();
//        flightClassDao.getAll();
//
//        System.out.println(aircraftDao.getAircraftByName("Airbus A320"));
//        System.out.println("22222");
//
//        ArrayList<Aircraft>  aircrafts = new ArrayList<>();
//        aircrafts.addAll(aircraftDao.getAll());
//
//        for (Aircraft c: aircrafts) {
//            System.out.println(c.getId() + " "+ c.getName()+ " "+ c.getCompany().getName());
//        }
////
////        ArrayList<FlightClass>  flightClasses = new ArrayList<>();
////        flightClasses.addAll(flightClassDao.getAll());
////        for (FlightClass c: flightClasses
////        ) {
////            System.out.println(c.getName() + " " + c.getDescription());
////        }
//
//        // City block

        CityDao cityDao = factory.getCityDao();
        City city = new City();
        System.out.println(cityDao.getAll());

    }
}
