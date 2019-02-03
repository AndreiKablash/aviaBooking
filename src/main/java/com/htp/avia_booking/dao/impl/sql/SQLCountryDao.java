package com.htp.avia_booking.dao.impl.sql;

import com.htp.avia_booking.dao.AbstractDAO;
import com.htp.avia_booking.dao.interfaces.CountryDao;
import com.htp.avia_booking.domain.source.objects.Country;
import com.htp.avia_booking.enums.DBOperation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLCountryDao extends AbstractDAO<Country> implements CountryDao {

    public static CountryDao getInstance() {
        return SQLCountryDao.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final CountryDao instance = new SQLCountryDao();
    }

    public SQLCountryDao() {
        this.INSERT_STATEMENT = "INSERT INTO <%table_name%> (name,code) VALUES (?, ?)";
        this.UPDATE_STATEMENT = "UPDATE <%table_name%> SET name =?, code = ? WHERE id = ?";
    }

    /**
     * Method to get table name
     *
     * @return string with table name in database
     */
    @Override
    protected String getTableName() {
        return "avia.source_country";
    }

    @Override
    protected void populateParameters(PreparedStatement preparedStatement, Country entity, DBOperation operation)
            throws SQLException {
        switch (operation) {
            case CREATE:
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getCode());
                preparedStatement.setBytes(3, entity.getIcon());
                break;
            case UPDATE:
                preparedStatement.setLong(1, entity.getId());
                preparedStatement.setString(2, entity.getName());
                preparedStatement.setString(3, entity.getCode());
                preparedStatement.setBytes(3, entity.getIcon());
                break;
        }
    }
    @Override
    protected Country fillEntity(ResultSet rs) throws SQLException {
        Country country = new Country();
        country.setId(rs.getLong("id"));
        country.setName(rs.getString("name"));
        country.setCode(rs.getString("code"));
        country.setIcon(rs.getBytes("icon"));
        return country;
    }
}
