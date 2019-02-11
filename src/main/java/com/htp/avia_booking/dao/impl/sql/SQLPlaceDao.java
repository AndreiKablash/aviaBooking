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
import java.util.ArrayList;
import java.util.List;

public class SQLPlaceDao extends AbstractDAO<Place> implements PlaceDao {

    private static final String SELECT_PLACE_BY_FLIGHT_CLASS_ID = "select * from avia.source_place where flight_class_id=?";
    private static final String SELECT_PLACE_BY_AIRCRAFT_ID = "SELECT * FROM avia.source_place " +
            "where id in (select place_id from avia.aircraft_to_place where aircraft_id=?) order by flight_class_id, row";
    private static final String SELECT_PLACE_BY_RESERVATION= "SELECT p.id, p.seat, p.row, p.flight_class_id," +
            "if ((select r.id from avia.reservation r where r.flight_id=? and r.place_id=p.id)>0,1,0) as busy" +
            "FROM avia.source_place p where id in (select place_id from avia.aircraft_to_place a1" +
            "where a1.aircraft_id=?)  order by flight_class_id, p.row;";

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
        ResultSet rs = null;
        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(SELECT_PLACE_BY_FLIGHT_CLASS_ID)){
            statement.setLong(1, flightClassId);
            rs = statement.executeQuery();
            while (rs.next()) {
                place = fillEntity(rs);
            }
            return place;
        }catch (SQLException ex) {
            LOGGER.error("SQLException", ex);;
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("ConnectionPoolException", ex);;
            throw new DaoException("Exception", ex);
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("SQLException", ex);
                }
            }
        }
    }

    @Override
    public Place getPlaceByAircraftID(long aircraftId) throws DaoException {
        Place place;
        ResultSet rs = null;
        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(SELECT_PLACE_BY_AIRCRAFT_ID)) {
            statement.setLong(1, aircraftId);
            rs = statement.executeQuery();
            if (rs.next()) {
                place = fillEntity(rs);
                return place;
            } else {
                return null;
            }
        }catch (SQLException ex) {
            LOGGER.error("SQLException", ex);;
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("ConnectionPoolException", ex);;
            throw new DaoException("Exception", ex);
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("SQLException", ex);
                }
            }
        }
    }

    @Override
    public List<Place> getPlaceBusy(long aircraftId, long flightId) throws DaoException {
        List<Place> placeList;
        ResultSet rs = null;
        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(SELECT_PLACE_BY_RESERVATION)){
            statement.setLong(1, flightId);
            statement.setLong(2, aircraftId);
            rs = statement.executeQuery();
            placeList = new ArrayList<>();
            while (rs.next()) {
                placeList.add(fillEntity(rs));
            }
        }catch (SQLException ex) {
            LOGGER.error("SQLException ", ex);
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("ConnectionPoolException", ex);
            throw new DaoException("Exception", ex);
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("SQLException", ex);
                }
            }
        }
        return placeList;
    }

    @Override
    protected String getTableName() {
        return "avia.source_place";
    }

    @Override
    protected Place fillEntity(ResultSet resultSet) throws SQLException {
        Place place = new Place();
        place.setId(resultSet.getLong("id"));
        place.setRow(resultSet.getInt("row"));
        place.setSeat(resultSet.getString("seat").charAt(0));
        //by default places  - available;
        try {
            place.setBusy(getBooleanFromInt(resultSet.getInt("busy")));
        } catch (Exception e) {
            place.setBusy(false);
            LOGGER.error("SQLException", e);
        }
        try {
            place.setFlightClass(SQLFlightClassDao.getInstance().getById(resultSet.getLong("flight_class_id")));
        } catch (DaoException e) {
            LOGGER.error("SQLException", e);
        }
        return place;
    }

    @Override
    protected void populateParameters(PreparedStatement preparedStatement, Place entity, DBOperation operation)
            throws SQLException {
        switch (operation){
            case CREATE:
                preparedStatement.setInt(1, entity.getRow());
                //character set
                preparedStatement.setString(2, String.valueOf(entity.getSeat()));
                preparedStatement.setLong(3, entity.getFlightClass().getId());
            case UPDATE:
                throw new UnsupportedOperationException();
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
