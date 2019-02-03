package com.htp.avia_booking.dao.impl.sql;

import com.htp.avia_booking.dao.AbstractDAO;
import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.connection_pool.ConnectionPoolException;
import com.htp.avia_booking.dao.interfaces.AircraftDao;
import com.htp.avia_booking.domain.source.objects.Aircraft;
import com.htp.avia_booking.enums.DBOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAircraftDao extends AbstractDAO<Aircraft> implements AircraftDao {

    private static final String GET_AIRCRAFT_BY_NAME = "SELECT * FROM avia.source_aircraft WHERE name = ?";

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
    public Aircraft getAircraftByName(String name) throws DaoException {
        Aircraft aircraft;
        ResultSet rs = null;
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(GET_AIRCRAFT_BY_NAME)) {
            statement.setString(1, name);
            rs = statement.executeQuery();
            if (rs.next()) {
                aircraft = fillEntity(rs);
                return aircraft;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.error("SQLException exception", ex);
            throw new DaoException("Exception", ex);
        } catch (ConnectionPoolException ex) {
            LOGGER.error("ConnectionPoolException exception", ex);
            throw new DaoException("Exception", ex);
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("SQLException exception", ex);
                }
            }
        }
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
            aircraft.setCompany(SQLCompanyDao.getInstance().getById(aircraft.getId()));
        } catch (DaoException ex) {
            LOGGER.error("SQLException exception", ex);
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
}
