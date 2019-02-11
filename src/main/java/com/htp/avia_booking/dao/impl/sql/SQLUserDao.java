package com.htp.avia_booking.dao.impl.sql;

import com.htp.avia_booking.dao.AbstractDAO;
import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.connection_pool.ConnectionPoolException;
import com.htp.avia_booking.dao.interfaces.UserDao;
import com.htp.avia_booking.domain.objects.User;
import com.htp.avia_booking.domain.source.objects.Role;
import com.htp.avia_booking.enums.DBOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLUserDao extends AbstractDAO<User> implements UserDao {

    private static final String SELECT_SPECIFIC_USER_BY_LOGIN_AND_PASSWORD = "SELECT u.* FROM avia.user u" +
            " WHERE u.login = ? AND u.password = ?";
    private static final String FIND_ROLES_BY_USERS_ID = "Select s.* from avia.user_to_role r  " +
            "inner join avia.source_role s on s.id = r.role_id where r.user_id = ?";

    private static final String GET_USER_RECORD_BY_LOGIN_PASSWORD= "SELECT u.* FROM avia.user u " +
            "inner join avia.user_to_role r on u.id = r.user_id WHERE u.login = ? and u.password = ?";

    public SQLUserDao() {
        this.INSERT_STATEMENT  = "INSERT INTO <%table_name%>(name, surname, document_id, email, login, " +
                "password, phone) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
        this.UPDATE_STATEMENT = "UPDATE <%table_name%> SET surname =?, document_id=?, email = ?, phone=? WHERE id = ?";
        this.GET_BY_ID = "SELECT  us.* FROM <%table_name%> us inner join user_to_role r on r.user_id=us.id WHERE r.user_id = ?";
        this.GET_ALL = "SELECT  us.* FROM <%table_name%> us inner join user_to_role r on r.user_id=us.id;";
    }

    public static UserDao getInstance() {
        return SQLUserDao.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final UserDao instance = new SQLUserDao();
    }

    @Override
    public boolean checkUser(User user) throws DaoException {
        ResultSet rs = null;
        boolean result;
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(SELECT_SPECIFIC_USER_BY_LOGIN_AND_PASSWORD)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            rs = statement.executeQuery();
            result = rs.first();
            return result;
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
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
    }

    @Override
    public boolean checkUserById(long id) throws DaoException {
        ResultSet rs = null;
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(GET_BY_ID)) {
            statement.setLong(1, id);
            rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("ConnectionPoolException ", ex);
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

    //method to find passengers role in database by passenger id
    private List<Role> findRolesById(long userId) throws DaoException {
        ResultSet rs = null;
        List<Role> roleList = new ArrayList<>();
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(FIND_ROLES_BY_USERS_ID)) {
            statement.setLong(1, userId);
            rs = statement.executeQuery();
            if (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setName(rs.getString("name"));
                role.setDescription(rs.getString("description"));
                roleList.add(role);
            }
        } catch (SQLException ex) {
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
        return roleList;
    }

    @Override
    public User getUserRecord(String login, String password) throws DaoException {
        ResultSet rs = null;
        User user;
        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(GET_USER_RECORD_BY_LOGIN_PASSWORD)) {
            statement.setString(1, login);
            statement.setString(2, password);
            rs = statement.executeQuery();
            if (rs.next()) {
                user = fillEntity(rs);
                return user;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("ConnectionPoolException", ex);
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
    protected String getTableName() {
        return "avia.user";
    }


    @Override
    protected User fillEntity(ResultSet rs) throws SQLException, DaoException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setDocumentId(rs.getString("document_id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setRole(this.findRolesById(user.getId()));
        return user;
    }

    @Override
    protected void populateParameters(PreparedStatement preparedStatement, User entity, DBOperation operation)
            throws SQLException {
        switch (operation){
            case CREATE:
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getSurname());
                preparedStatement.setString(3, entity.getDocumentId());
                preparedStatement.setString(4, entity.getEmail());
                preparedStatement.setString(5, entity.getLogin());
                preparedStatement.setString(6, entity.getPassword());
                preparedStatement.setString(7, entity.getPhone());
                break;
            case UPDATE:
                preparedStatement.setString(1, entity.getEmail());
                preparedStatement.setString(2, entity.getDocumentId());
                preparedStatement.setString(3, entity.getPhone());
        }
    }
}
