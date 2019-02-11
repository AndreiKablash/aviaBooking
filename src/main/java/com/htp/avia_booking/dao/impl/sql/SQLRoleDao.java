package com.htp.avia_booking.dao.impl.sql;

import com.htp.avia_booking.dao.AbstractDAO;
import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.interfaces.RoleDao;
import com.htp.avia_booking.domain.source.objects.Role;
import com.htp.avia_booking.enums.DBOperation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SQLRoleDao extends AbstractDAO<Role> implements RoleDao {

    public SQLRoleDao() {
        this.INSERT_STATEMENT = "INSERT INTO <%table_name%> (name,description) VALUES (?, ?)";
        this.UPDATE_STATEMENT = "UPDATE <%table_name%> SET name =?, description = ? WHERE id = ?";
    }

    public static RoleDao getInstance() {
        return SQLRoleDao.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final RoleDao instance = new SQLRoleDao();
    }

    @Override
    protected void populateParameters(PreparedStatement preparedStatement, Role entity, DBOperation operation)
            throws SQLException {
        switch (operation){
            case CREATE:
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getDescription());
                break;
            case UPDATE:
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getDescription());
        }
    }

    @Override
    protected Role fillEntity(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("id"));
        role.setName(resultSet.getString("name"));
        role.setDescription(resultSet.getString("description"));
        return role;
    }

    @Override
    protected String getTableName() {
        return "avia.source_role";
    }

    @Override
    public List<Role> getByName(String name) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
