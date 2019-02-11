package com.htp.avia_booking.dao.interfaces;

import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.GenericDAO;
import com.htp.avia_booking.domain.objects.User;

public interface UserDao extends GenericDAO<User> {

    /**
     * Method to check user record in database by login and document_id
     * @param user client object, that necessary checks in database
     * @return true if the user is in database, else false
     * @throws DaoException there are errors while reading from database
     */
    boolean checkUser(User user) throws DaoException;

    /**
     * Method to check user in database by identification number
     * @param id  unique identification number of user
     * @return true if the user is in database, else false
     * @throws DaoException there are errors while reading from database
     */
    boolean checkUserById(long id)throws DaoException;


    /**
     * Method to get user record from database by login and password
     *
     * @param login - value of the user field login
     * @param password - value of the user field password
     * @return user record correspond with search @param
     * @throws DaoException
     */
    User getUserRecord(String login, String password) throws DaoException;
}
