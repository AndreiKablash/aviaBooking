package com.htp.avia_booking.dao.impl.sql;

import com.htp.avia_booking.dao.AbstractDAO;
import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.interfaces.CityDao;
import com.htp.avia_booking.domain.source.objects.City;
import com.htp.avia_booking.enums.DBOperation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLCityDao extends AbstractDAO<City> implements CityDao {

    public SQLCityDao() {
        this.INSERT_STATEMENT = "INSERT INTO <%table_name%>(name,code,country_id,description)" +
                " VALUES (?,?,?,?)";
        this.UPDATE_STATEMENT = "UPDATE <%table_name%> SET name =?, code = ?,description =?, WHERE id = ?";
    }

    public static CityDao getInstance() {
        return SQLCityDao.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final CityDao instance = new SQLCityDao();
    }

    @Override
    protected String getTableName() {
        return "avia.source_city";
    }

    @Override
    protected City fillEntity(ResultSet resultSet) throws SQLException {
        City city = new City();
        city.setId(resultSet.getLong("id"));
        city.setName(resultSet.getString("name"));
        city.setCode(resultSet.getString("code"));
        try {
            city.setCountry(SQLCountryDao.getInstance().getById(resultSet.getLong("country_id")));
        } catch (DaoException ex) {
            LOGGER.error("SQLException", ex);
        }
        city.setDescription(resultSet.getString("description"));
        return city;
    }

    @Override
    protected void populateParameters(PreparedStatement preparedStatement, City entity, DBOperation operation)
            throws SQLException {
        switch (operation) {
            case CREATE:
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getCode());
                preparedStatement.setLong(3, entity.getCountry().getId());
                preparedStatement.setString(4, entity.getDescription());
                break;
            case UPDATE:
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getCode());
                preparedStatement.setString(3, entity.getDescription());
        }
    }
}
