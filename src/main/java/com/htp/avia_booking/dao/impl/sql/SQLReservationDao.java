package com.htp.avia_booking.dao.impl.sql;

import com.htp.avia_booking.dao.AbstractDAO;
import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.connection_pool.ConnectionPoolException;
import com.htp.avia_booking.dao.interfaces.ReservationDao;
import com.htp.avia_booking.domain.objects.Reservation;
import com.htp.avia_booking.enums.DBOperation;
import com.htp.avia_booking.util.GMTCalendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class SQLReservationDao extends AbstractDAO<Reservation> implements ReservationDao {

    private static final String SELECT_RESERVATION_BY_USER_DOCUMENT_ID = "SELECT * FROM avia.reservation r " +
            "inner join avia.user u on u.id=r.user_id where u.document_id=?";
    private static final String SELECT_RESERVATION_BY_USER_SURNAME = "SELECT * FROM avia.reservation r " +
            "inner join avia.user u on u.id=r.user_id where u.surname=?" ;
    private static final String SELECT_RESERVATION_BY_RESERVATION_CODE = "SELECT * FROM avia.reservation r " +
            "inner join avia.user u on u.id=r.user_id where r.code=?" ;
    private static final String SELECT_RESERVATION_BY_RESERVATION_DATE = "SELECT * FROM avia.reservation" +
            "where reserve_datetime>=1 and  reserve_datetime<2";


    public SQLReservationDao() {
        this.INSERT_STATEMENT = "insert into <%table_name%> (flight_id, passenger_id, place_id, add_info, " +
                "reserve_datetime, code) values (?,?,?,?,?,?)";
    }

    public static ReservationDao getInstance() {
        return SQLReservationDao.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final ReservationDao instance = new SQLReservationDao();
    }


    @Override
    public Reservation getReservationByDocumentNumber(String document_id) throws DaoException {
        Reservation  reservation = null;
        ResultSet rs = null;
        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(SELECT_RESERVATION_BY_USER_DOCUMENT_ID)){
            statement.setString(1,document_id);
            rs = statement.executeQuery();
            while (rs.next()) {
                 reservation= fillEntity(rs);
            }
            return reservation;
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
    }

    @Override
    public Reservation getReservationBySurname(String surname) throws DaoException {
        Reservation  reservation = null;
        ResultSet rs = null;
        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(SELECT_RESERVATION_BY_USER_SURNAME)){
            statement.setString(1,surname);
            rs = statement.executeQuery();
            while (rs.next()) {
                reservation= fillEntity(rs);
            }
            return reservation;
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
    }

    @Override
    public Reservation getReservationByCode(String code) throws DaoException {
        Reservation  reservation = null;
        ResultSet rs =null;
        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(SELECT_RESERVATION_BY_RESERVATION_CODE)){
            statement.setString(1,code);
            rs = statement.executeQuery();
            while (rs.next()) {
                reservation= fillEntity(rs);
            }
            return reservation;
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
    }


    @Override
    public Reservation getReservationByDateReserve(Calendar dateReservation) throws DaoException {
        Reservation  reservation = null;
        ResultSet rs = null;
        // set hours, minute, second, millisecond by zero to search all flight in 24 hours
        clearTime(dateReservation);
        // in which interval to search for (by default - 24 hours)
        Calendar dateTimeInterval = (Calendar) (dateReservation.clone());
        dateTimeInterval.add(Calendar.DATE, TWENTY_FOUR_HOURS_SEARCH_INTERVAL);

        try(Connection connect = pool.getConnection();
            PreparedStatement statement = connect.prepareStatement(SELECT_RESERVATION_BY_RESERVATION_DATE)){
            statement.setLong(1, dateReservation.getTimeInMillis());
            statement.setLong(2, dateTimeInterval.getTimeInMillis());
            rs = statement.executeQuery();
            while (rs.next()) {
                reservation= fillEntity(rs);
            }
            return reservation;
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
    }

    @Override
    protected String getTableName() {
        return "avia.reservation";
    }

    @Override
    protected Reservation fillEntity(ResultSet resultSet) throws SQLException, DaoException {
        Reservation reservation = new Reservation();
        reservation.setId(resultSet.getLong("id"));
        reservation.setFlight(SQLFlightDao.getInstance().getById(resultSet.getLong("flight_id")));
        reservation.setUser(SQLUserDao.getInstance().getById(resultSet.getLong("user_id")));
        reservation.setPlace(SQLPlaceDao.getInstance().getById(resultSet.getLong("place_id")));
        reservation.setCode(resultSet.getString("code"));
        Calendar c = GMTCalendar.getInstance();
        c.setTimeInMillis(resultSet.getLong("reserve_datetime"));
        reservation.setReserveDateTime(c);
        reservation.setAddInfo(resultSet.getString("add_info"));
        return reservation;
    }

    @Override
    protected void populateParameters(PreparedStatement preparedStatement, Reservation entity, DBOperation operation)
            throws SQLException {
        switch (operation){
            case CREATE:
                preparedStatement.setLong(1, entity.getFlight().getId());
                preparedStatement.setLong(2, entity.getUser().getId());
                preparedStatement.setLong(3, entity.getPlace().getId());
                preparedStatement.setString(4, entity.getCode());
                preparedStatement.setLong(5, entity.getReserveDateTime().getTimeInMillis());
                preparedStatement.setString(6, entity.getAddInfo());
                break;
            case UPDATE:
                throw new UnsupportedOperationException();
        }
    }
}
