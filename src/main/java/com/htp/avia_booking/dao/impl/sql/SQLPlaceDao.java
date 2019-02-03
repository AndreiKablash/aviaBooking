package com.htp.avia_booking.dao.impl.sql;

import com.htp.avia_booking.dao.AbstractDAO;
import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.connection_pool.ConnectionPoolException;
import com.htp.avia_booking.dao.interfaces.PlaceDao;
import com.htp.avia_booking.domain.source.objects.Place;
import com.htp.avia_booking.enums.DBOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLPlaceDao extends AbstractDAO<Place> implements PlaceDao {

    private static final String SELECT_PLACE_BY_FLIGHT_CLASS_ID = "select * from avia.source_place where flight_class_id=?";
    private static final String SELECT_PLACE_BY_AIRCRAFT_ID = "SELECT * FROM avia.source_place " +
            "where id in (select place_id from avia.aircraft_to_place where aircraft_id=?) order by flight_class_id, row";
    private static final String SELECT_PLACE_BY_RESERVATION= "SELECT p.id, p.seat_letter, p.seat,p.flight_class_id, " +
            "if ((select r.id from reservation r where r.flight_id=? and r.place_id=p.id)>0,1,0) as busy " +
            "FROM avia.source_place p where id in (select place_id from avia.aircraft_to_place a1 " +
            "where a1.aircraft_id=?)  order by flight_class_id, row";

    private SQLPlaceDao() {
        this.INSERT_STATEMENT = "INSERT INTO <%table_name%> (row,seat,flight_class)id) VALUES (?,?,?)";
    }

    public static PlaceDao getInstance() {
        return SQLPlaceDao.SingletonHolder.instance;
    }


    private static class SingletonHolder {
        private static final PlaceDao instance = new SQLPlaceDao();
    }

    @Override
    public Place getPlaceByFlightClass(long flightClassId) throws DaoException {
        Place place =null;
        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(SELECT_PLACE_BY_FLIGHT_CLASS_ID);
            ResultSet set = statement.executeQuery())
        {
            statement.setLong(1, flightClassId);
            while (set.next()) {
                place = fillEntity(set);
            }
            return place;
        }catch (SQLException ex) {
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }
    }

    @Override
    public Place getPlaceByAircraftID(long aircraftId) throws DaoException {
        Place place;
        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(SELECT_PLACE_BY_AIRCRAFT_ID);
            ResultSet set = statement.executeQuery())
        {
            statement.setLong(1, aircraftId);
            if (set.next()) {
                place = fillEntity(set);
                return place;
            } else {
                return null;
            }
        }catch (SQLException ex) {
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }
    }

    @Override
    public boolean getPlaceBusy(long aircraftId, long flightId) throws DaoException {
        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(SELECT_PLACE_BY_RESERVATION);
            ResultSet set = statement.executeQuery())
        {
            statement.setLong(1, flightId);
            statement.setLong(2, aircraftId);

            return set.next();
        }catch (SQLException ex) {
            LOGGER.error("SQLException exception", ex);
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("SQLException exception", ex);
            throw new DaoException("Exception", ex);
        }
    }

    @Override
    protected String getTableName() {
        return "avia.source_place";
    }

    @Override
    protected Place fillEntity(ResultSet resultSet) throws SQLException {
        Place place = new Place();
        place.setId(resultSet.getLong("id"));
        place.setRow(resultSet.getString("row").charAt(0));
        place.setSeat(resultSet.getInt("seat"));
        // поле busy - не во всех запросах, по-умолчанию место - свободно
        try {
            place.setBusy(getBooleanFromInt(resultSet.getInt("busy")));
        } catch (Exception e) {
            place.setBusy(false); // catch без перехвата - не очень правильное решение
            LOGGER.error("SQLException exception", e);
        }
        try {
            place.setFlightClass(SQLFlightClassDao.getInstance().getById(resultSet.getLong("flight_class_id")));
        } catch (DaoException e) {
            LOGGER.error("SQLException exception", e);
        }
        return place;
    }

    @Override
    protected void populateParameters(PreparedStatement preparedStatement, Place entity, DBOperation operation)
            throws SQLException {
        switch (operation){
            case CREATE:
                preparedStatement.setString(1, String.valueOf(entity.getRow()));
                preparedStatement.setInt(2, entity.getSeat());
                preparedStatement.setLong(3, entity.getFlightClass().getId());
        }
    }

    private boolean getBooleanFromInt(int number) {
        if (number > 0) {
            return true;
        } else {
            return false;
        }
    }
}
