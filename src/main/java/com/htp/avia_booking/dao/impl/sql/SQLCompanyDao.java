package com.htp.avia_booking.dao.impl.sql;

import com.htp.avia_booking.dao.AbstractDAO;
import com.htp.avia_booking.dao.interfaces.CompanyDao;
import com.htp.avia_booking.domain.source.objects.Company;
import com.htp.avia_booking.enums.DBOperation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLCompanyDao extends AbstractDAO<Company> implements CompanyDao {

    public SQLCompanyDao() {
        this.INSERT_STATEMENT = "INSERT INTO <%table_name%> (name,description) VALUES (?,?)";
        this.UPDATE_STATEMENT = "UPDATE <%table_name%> SET description =? WHERE id = ?";
    }

    public static CompanyDao getInstance() {
        return SQLCompanyDao.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final CompanyDao instance = new SQLCompanyDao();
    }


    @Override
    protected String getTableName() {
        return "avia.source_company";
    }

    @Override
    protected Company fillEntity(ResultSet resultSet) throws SQLException {
        Company company = new Company();
        company.setId(resultSet.getLong("id"));
        company.setName(resultSet.getString("name"));
        company.setDescription(resultSet.getString("description"));
        return company;
    }

    @Override
    protected void populateParameters(PreparedStatement preparedStatement, Company entity, DBOperation operation)
            throws SQLException {
        switch (operation) {
            case CREATE:
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getDescription());
                break;
            case UPDATE:
                preparedStatement.setString(1, entity.getDescription());
        }
    }
}
