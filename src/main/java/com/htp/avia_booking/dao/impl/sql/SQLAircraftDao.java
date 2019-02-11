package com.htp.avia_booking.dao.impl.sql;

import com.htp.avia_booking.dao.AbstractDAO;
import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.connection_pool.ConnectionPoolException;
import com.htp.avia_booking.dao.interfaces.AircraftDao;
import com.htp.avia_booking.domain.source.objects.Aircraft;
import com.htp.avia_booking.domain.source.objects.Place;
import com.htp.avia_booking.enums.DBOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLAircraftDao extends AbstractDAO<Aircraft> implements AircraftDao {

    private static final String FIND_PLACES_BY_AIRCRAFT_ID = "SELECT avia.source_place.* " +
            "FROM avia.aircraft_to_place inner JOIN avia.source_place" +
            "ON avia.aircraft_to_place.place_id = source_place.id WHERE avia.aircraft_to_place.aircraft_id = ?";

    public SQLAircraftDao() {
        this.INSERT_STATEMENT = "INSERT INTO <%table_name%> (name,company_id,description) VALUES (?,?,?)";
    }

    public static AircraftDao getInstance() {
        return SQLAircraftDao.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final AircraftDao instance = new SQLAircraftDao();
    }

    @Override
    protected String getTableName() {
        return "avia.source_aircraft";
    }

    @Override
    protected Aircraft fillEntity(ResultSet resultSet) throws SQLException {
        Aircraft aircraft = new Aircraft();
        aircraft.setId(resultSet.getLong("id"));
        aircraft.setName(resultSet.getString("name"));
        try {
            aircraft.setCompany(SQLCompanyDao.getInstance().getById(resultSet.getLong("company_id")));
//            aircraft.setPlaceList(getPlacesByAircraftId(aircraft.getId()));
        } catch (DaoException ex) {
            LOGGER.error("DaoException", ex);
        }
        aircraft.setDescription(resultSet.getString("description"));
        return aircraft;
    }

    @Override
    protected void populateParameters(PreparedStatement preparedStatement, Aircraft entity, DBOperation operation)
            throws SQLException {
        switch (operation) {
            case CREATE:
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setLong(2, entity.getCompany().getId());
                preparedStatement.setString(3, entity.getDescription());
                break;
            case UPDATE:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * Method to get list of places in aircraft by identification number of aircraft
     *
     * @param aircraftId identification number
     * @return List<Place>
     * @throws DaoException
     */
    private List<Place> getPlacesByAircraftId(long aircraftId) throws DaoException{
        List<Place> placeList;
        ResultSet rs = null;
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(FIND_PLACES_BY_AIRCRAFT_ID)) {
            statement.setLong(1, aircraftId);
            rs = statement.executeQuery();
            placeList = new ArrayList<>();
            if (rs.next()) {
                Place place = SQLPlaceDao.getInstance().getPlaceByAircraftID(aircraftId);
                placeList.add(place);
            }
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
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
        return placeList;
    }
}
