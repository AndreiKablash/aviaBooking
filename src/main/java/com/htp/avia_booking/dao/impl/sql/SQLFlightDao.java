package com.htp.avia_booking.dao.impl.sql;

import com.htp.avia_booking.dao.AbstractDAO;
import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.connection_pool.ConnectionPoolException;
import com.htp.avia_booking.dao.interfaces.FlightDao;
import com.htp.avia_booking.domain.objects.Flight;
import com.htp.avia_booking.domain.source.objects.City;
import com.htp.avia_booking.enums.DBOperation;
import com.htp.avia_booking.util.GMTCalendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SQLFlightDao extends AbstractDAO<Flight> implements FlightDao {

    private static final String MIN = " мин.";
    private static final String HOUR = " ч.  ";
    private static final String DAY = " д.  ";
    private static final String GET_SELECTED_FLIGHT = "select * from flight " +
            "where date_depart>=? and  date_depart<? and city_from_id=? and city_to_id=?";

    public SQLFlightDao() {
        this.INSERT_STATEMENT = " ";
        this.UPDATE_STATEMENT = " ";
    }

    public static FlightDao getInstance() {
        return SQLFlightDao.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final FlightDao instance = new SQLFlightDao();
    }

    /**
     * Method to find flight in database by three parameters: date of departure, city of departure, city of arrival
     *
     * @param dateTime - day of departure
     * @param cityFrom - city of departure
     * @param cityTo   - city of arrival
     * @return List all flight which are in the database or empty list if no nodes in database
     * @throws DaoException
     */
    @Override
    public List<Flight> getFlight(Calendar dateTime, City cityFrom, City cityTo) throws DaoException {
        ArrayList<Flight> flightList = new ArrayList<>();
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(GET_SELECTED_FLIGHT);
             ResultSet set = statement.executeQuery()) {

            // set hours, minute, second, millisecond by zero to search all flight in 24 hours
            clearTime(dateTime);

            // set time interval for search in 1 day
            Calendar dateTimeInterval = (Calendar) (dateTime.clone());
            dateTimeInterval.add(Calendar.DATE, INTERVAL);
            statement.setLong(1, dateTime.getTimeInMillis());
            statement.setLong(2, dateTimeInterval.getTimeInMillis());
            statement.setLong(3, cityFrom.getId());
            statement.setLong(4, cityTo.getId());

            while (set.next()) {
                flightList.add(fillEntity(set));
            }
        } catch (SQLException ex) {
            LOGGER.error("SQLException exception", ex);
            ;
            throw new DaoException("Exception", ex);
        } catch (ConnectionPoolException ex) {
            LOGGER.error("SQLException exception", ex);
            ;
            throw new DaoException("Exception", ex);
        }
        return flightList;
    }


    @Override
    protected String getTableName() {
        return "avia.flight";
    }

    @Override
    protected Flight fillEntity(ResultSet resultSet) throws SQLException {
        Calendar dateDepart = GMTCalendar.getInstance();
        Calendar dateCome = GMTCalendar.getInstance();
        dateDepart.setTimeInMillis(resultSet.getLong("date_depart"));
        dateCome.setTimeInMillis(resultSet.getLong("date_arrival"));

        Flight flight = new Flight();
        flight.setId(resultSet.getLong("id"));
        flight.setCode(resultSet.getString("code"));
        flight.setDateDepart(dateDepart);
        flight.setDateArrival(dateCome);
        try {
            flight.setAircraft(SQLAircraftDao.getInstance().getById(resultSet.getLong("aircraft_id")));
            flight.setCityFrom(SQLCityDao.getInstance().getById(resultSet.getLong("city_from_id")));
            flight.setCityTo(SQLCityDao.getInstance().getById(resultSet.getLong("city_to_id")));
        } catch (DaoException ex) {
            LOGGER.error("SQLException exception", ex);
        }

        //Used to calculate the duration of flight
        StringBuilder sb = new StringBuilder();
        int dayDiff = dateCome.get(Calendar.DAY_OF_YEAR) - dateDepart.get(Calendar.DAY_OF_YEAR);
        int hourDiff = dateCome.get(Calendar.HOUR_OF_DAY) - dateDepart.get(Calendar.HOUR_OF_DAY);
        int minDiff = dateCome.get(Calendar.MINUTE) - dateDepart.get(Calendar.MINUTE);

        if (dayDiff > 0) {
            sb.append(dayDiff).append(DAY);
        }

        if (hourDiff > 0) {
            sb.append(hourDiff).append(HOUR);
        }

        if (minDiff > 0) {
            sb.append(minDiff).append(MIN);
        }

        flight.setDuration(sb.toString());

        return flight;
    }

    @Override
    protected void populateParameters(PreparedStatement preparedStatement, Flight entity, DBOperation operation)
            throws SQLException {
        switch (operation) {
            case CREATE:
                preparedStatement.setString(1, entity.getCode());
                preparedStatement.setLong(2, entity.getDateDepart().getTimeInMillis());
                preparedStatement.setLong(3, entity.getDateArrival().getTimeInMillis());
                preparedStatement.setLong(4, entity.getAircraft().getId());
                preparedStatement.setLong(5, entity.getCityFrom().getId());
                preparedStatement.setLong(6, entity.getCityTo().getId());
                break;
            case UPDATE:
                preparedStatement.setLong(1, entity.getAircraft().getId());
                preparedStatement.setLong(2, entity.getDateDepart().getTimeInMillis());
                preparedStatement.setLong(3, entity.getDateArrival().getTimeInMillis());
                break;
        }
    }
}