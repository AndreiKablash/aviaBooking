package com.htp.avia_booking.dao.impl.sql;

import com.htp.avia_booking.dao.AbstractDAO;
import com.htp.avia_booking.dao.interfaces.FlightClassDao;
import com.htp.avia_booking.domain.source.objects.FlightClass;
import com.htp.avia_booking.enums.DBOperation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLFlightClassDao extends AbstractDAO<FlightClass> implements FlightClassDao {

    public SQLFlightClassDao() {
        this.INSERT_STATEMENT = "INSERT INTO <%table_name%>(name,description)" +
                " VALUES (?,?)";
        this.UPDATE_STATEMENT = "UPDATE <%table_name%> SET name =?, description =?, WHERE id = ?";
    }

    public static FlightClassDao getInstance() {
        return SQLFlightClassDao.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final FlightClassDao instance = new SQLFlightClassDao();
    }

    @Override
    protected FlightClass fillEntity(ResultSet resultSet) throws SQLException {
        FlightClass flightClass = new FlightClass();
        flightClass.setId(resultSet.getLong("id"));
        flightClass.setName(resultSet.getString("name"));
        flightClass.setDescription(resultSet.getString("description"));
        return flightClass;
    }

    @Override
    protected void populateParameters(PreparedStatement preparedStatement, FlightClass entity, DBOperation operation)
            throws SQLException {
        switch (operation) {
            case CREATE:
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getDescription());
                break;

            case UPDATE:
                preparedStatement.setString(1, entity.getDescription());
                break;
        }
    }

    @Override
    protected String getTableName() {
        return "avia.source_flight_class";
    }

}
