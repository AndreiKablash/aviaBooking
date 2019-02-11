package com.htp.avia_booking.service.impl;

import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.interfaces.UserDao;
import com.htp.avia_booking.domain.objects.User;
import com.htp.avia_booking.service.AbstractService;
import com.htp.avia_booking.service.ServiceException;
import com.htp.avia_booking.service.interfaces.UserService;
import com.htp.avia_booking.service.validator.LoginValidator;
import com.htp.avia_booking.service.validator.RegistrationValidator;
import com.htp.avia_booking.service.validator.ValidationException;
import com.htp.avia_booking.service.validator.ValidatorInterface;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl extends AbstractService<User> implements UserService {

    private static final String USER_ROLE = "user";
    private static final ValidatorInterface<User> LOGIN = LoginValidator.getInstance();
    private static final ValidatorInterface<User> REGISTRATION = RegistrationValidator.getInstance();

    private UserServiceImpl() {
    }

    public static UserService getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final UserServiceImpl instance = new UserServiceImpl();
    }

    @Override
    public User authorization(User user) throws ServiceException {
        try {
            UserDao userDao = factory.getUserDao();

            if(LOGIN.isValid(user)) {

                String password = user.getPassword();
                String passwordMD5 = DigestUtils.md5Hex(password);
                user.setPassword(passwordMD5);

                boolean check = userDao.checkUser(user);
                if (!check) {
                    return null;
                } else{
                    return userDao.getUserRecord(user.getLogin(), user.getPassword());
                }
            } else {
                LOGGER.info("Validation Exception in authorization method");
                throw new ValidationException("Validation Exception");
            }
        } catch (DaoException ex) {
            LOGGER.error("DaoException in authorization method ", ex);
            throw new ServiceException("Service Exception", ex);
        }
    }

    @Override
    public User create(User user) throws ServiceException {
        try {
            if(REGISTRATION.isValid(user)) {
                String password = user.getPassword();
                String passwordMD5 = DigestUtils.md5Hex(password);
                user.setPassword(passwordMD5);
                UserDao userDao = factory.getUserDao();

                boolean check = userDao.checkUser(user);
                if (!check) {
                    long id = userDao.create(user);
                    user.setId(id);
                    return user;
                } else {
                    return null;
                }
            } else {
                LOGGER.info("Validation Exception in registration method");
                throw new ValidationException("Validation Exception");
            }
        } catch (DaoException ex) {
            LOGGER.error("DaoException in registration method ", ex);
            throw new ServiceException("Service Exception", ex);
        }
    }

    //Check for valid information before creation

    @Override
    public User update(User user) throws ServiceException {
        try {
            if(REGISTRATION.isValid(user)){
            UserDao userDao = factory.getUserDao();
            long id = userDao.update(user);
            return userDao.getById(id);}
            else{
                return null;
            }
        } catch (DaoException ex) {
            LOGGER.error("DaoException in update method", ex);
            throw new ServiceException("Service Exception", ex);
        }
    }

    @Override
    public boolean deleteUser(long id) throws ServiceException {
        try {
            UserDao userDao = factory.getUserDao();
            User user = new User();
            user.setId(id);
            boolean check = userDao.checkUserById(id);
            if(!check) {
                return false;
            } else {
                return userDao.delete(user);
            }
        } catch (DaoException ex) {
            LOGGER.error("DaoException in deleteUser method ", ex);
            throw new ServiceException("Service Exception", ex);
        }
    }

    @Override
    public List<User> loadAll() throws ServiceException {
        List<User> userList;
        try {
            UserDao userDao = factory.getUserDao();
            userList = userDao.getAll();
            return userList;
        } catch (DaoException ex) {
            LOGGER.error("DaoException in deleteUser loadAll ", ex);
            throw new ServiceException("Service Exception", ex);
        }
    }

    @Override
    public User loadById(Long userId) throws ServiceException{
        try {
            UserDao userDao = factory.getUserDao();
            User user = userDao.getById(userId);
            if(user != null) {
                return user;
            } else {
                return null;
            }
        } catch (DaoException ex) {
            LOGGER.error("DaoException in loadById method ", ex);
            throw new ServiceException("Service Exception", ex);
        }
    }

    @Override
    public List<User> viewAll() throws ServiceException {
        List<User> userList;
        List<User> finalList = new ArrayList<>();
        try {
            UserDao userDao = factory.getUserDao();
            userList = userDao.getAll();
            for(User u : userList) {
                if(u.getRole().listIterator().next().getName().equals(USER_ROLE)) {
                    finalList.add(u);
                }
            }
            return finalList;
        } catch (DaoException e) {
            LOGGER.error("DaoException in viewAll method");
            throw new ServiceException("Service Exception", e);
        }
    }
}