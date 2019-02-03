package com.htp.avia_booking.dao.interfaces;

import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.GenericDAO;
import com.htp.avia_booking.domain.objects.User;

public interface UserDao extends GenericDAO<User> {

    //method to check passenger in database
    boolean checkUser(User user) throws DaoException;

    //method to check passenger in database by id
    boolean checkUserById(long id)throws DaoException;

    User getUserRecord(String login, String password) throws DaoException;
}
