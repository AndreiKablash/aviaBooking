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

    private static final String SELECT_SPECIFIC_USER = "SELECT login,document_id FROM avia.user " +
            "WHERE login = ? OR document_id = ?";
    private static final String FIND_ROLES_BY_USERS_ID = "SELECT role.id, role.name,role.description " +
            "FROM avia.user_to_role " +
            "inner JOIN avia.role ON avia.user_to_role.role_id = role.id" +
            "WHERE user_to_role.user_id = ?";


    private static final String GET_USER_RECORD = "SELECT name, passenger.surname," +
            "passenger.document_id, passenger.email, passenger.login, passenger.password, passenger.phone, role.name" +
            "role.description"
            + "FROM avia.passenger LEFT OUTER JOIN avia.user_to_role ON passenger.id = user_to_role.user_id "
            + "LEFT OUTER JOIN avia.role ON user_to_role.user_id = role.name"
            + "WHERE passenger.id = ? ";

    public SQLUserDao() {
        this.INSERT_STATEMENT  = "INSERT INTO avia.user(name, surname, document_id, email, login, " +
                "password, phone) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
        this.UPDATE_STATEMENT = "UPDATE avia.user SET surname =?, document_id=?, email = ?, phone=? WHERE id = ?";
        this.GET_BY_ID = "SELECT avia.user.*, avia.source_role.* FROM avia.user_to_role utr" +
                "INNER JOIN avia.user ON avia.user.id = utr.user_id" +
                "INNER JOIN avia.source_role ON utr.role_id = avia.source_role.id" +
                "WHERE avia.user.id = ?";
    }

    public static UserDao getInstance() {
        return SQLUserDao.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final UserDao instance = new SQLUserDao();
    }

    /**
     * Method to check user record in database by login and document_id
     * @param user client object, that necessary checks in database
     * @return true if the user is in database, else false
     * @throws DaoException there are errors while reading from database
     */
    @Override
    public boolean checkUser(User user) throws DaoException {
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(SELECT_SPECIFIC_USER);
             ResultSet rs = statement.executeQuery()) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getDocumentId());
            return rs.next();
        } catch (SQLException ex) {
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }
    }

    /**
     * Method to check user in database by identification number
     * @param id  unique identification number of user
     * @return true if the user is in database, else false
     * @throws DaoException there are errors while reading from database
     */

    @Override
    public boolean checkUserById(long id) throws DaoException {
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(GET_BY_ID);
             ResultSet rs = statement.executeQuery()) {
            statement.setLong(1, id);
            return rs.next();
        } catch (SQLException ex) {
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }
    }


    //method to find passengers role in database by passenger id
    public List<Role> findRolesById(long userId) throws DaoException {
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(FIND_ROLES_BY_USERS_ID);
             ResultSet set = statement.executeQuery()) {
            statement.setLong(1, userId);
            List<Role> roles = new ArrayList<>();
            if (set.next()) {
                Role role = new Role();
                role.setId(set.getLong("id"));
                role.setName(set.getString("name"));
                role.setDescription(set.getString("description"));
                roles.add(role);
            }
            return roles;
        } catch (SQLException ex) {
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }
    }
    @Override
    public User getUserRecord(String login, String password) throws DaoException {
        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(GET_USER_RECORD)) {
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            rs.next();
            User user;
            if (rs.next()) {
                user = fillEntity(rs);
                return user;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }catch (ConnectionPoolException ex){
            LOGGER.error("SQLException exception", ex);;
            throw new DaoException("Exception", ex);
        }
    }

//    @Override
//    public Long create(User user) throws DaoException {
//        try (Connection connect = pool.getConnection();
//             PreparedStatement statement = connect.prepareStatement(INSERT_STATEMENT);
//             PreparedStatement statementNew = connect.prepareStatement(INSERT_ROLE);
//             PreparedStatement statementThird = connect.prepareStatement(LAST_INSERT_ID)) {
//            connect.setAutoCommit(false);
//            statement.setString(1, user.getName());
//            statement.setString(2, user.getSurname());
//            statement.setString(3, user.getEmail());
//            statement.setString(4, user.getDocumentId());
//            statement.executeUpdate();
//            connect.commit();
//            statementNew.setString(1, user.getLogin());
//            statementNew.setString(2, user.getPassword());
//            statementNew.executeUpdate();
//            connect.commit();
//
//            ResultSet set = statementThird.executeQuery();
//            set.next();
//            return Long.valueOf(set.getInt(LAST_ID));
//        } catch (SQLException | ConnectionPoolException e) {
//            throw new DaoException("Exception", e);
//        }
//    }


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
                preparedStatement.setLong(3, entity.getId());
        }
    }
}
