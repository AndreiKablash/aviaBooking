package com.htp.avia_booking.service.impl;

import com.google.protobuf.ServiceException;
import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.factory.DaoFactory;
import com.htp.avia_booking.dao.interfaces.UserDao;
import com.htp.avia_booking.domain.objects.User;
import com.htp.avia_booking.exception.NoSuchEntityException;
import com.htp.avia_booking.service.UserService;
import com.htp.avia_booking.service.validator.LoginValidator;
import com.htp.avia_booking.service.validator.ValidationException;
import com.htp.avia_booking.service.validator.ValidatorInterface;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final DaoFactory factory = DaoFactory.getDaoFactory();
    private static final ValidatorInterface<User> VALIDATE = LoginValidator.getInstance();

    private UserServiceImpl() {
    }

    public static UserService getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public User loadById(Long userId) throws ServiceException, NoSuchEntityException {
        return null;
    }

    /**
     * Method check login and password information from some user and get user object if authorization success
     * @param user object, that provides authorization operation
     * @return null if client not exists in system; user object if authorization execute correctly
     * @throws ServiceException
     */
    @Override
    public User authorization(User user) throws ServiceException {
        try {
            UserDao userDao = factory.getUserDao();

            if(VALIDATE.isValid(user)) {

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
                throw new ValidationException("Validation Exception");
            }
        } catch (DaoException e) {
            throw new ServiceException("Service Exception", e);
        }
    }

    @Override
    public List<User> viewAll() throws ServiceException {
        return null;
    }

    private static class SingletonHolder {
        private static final UserServiceImpl instance = new UserServiceImpl();
    }

    @Override
    public User create(User user) throws ServiceException {
        try {
            if(VALIDATE.isValid(user)) {
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
                throw new ValidationException("Validation Exception");
            }
        } catch (DaoException e) {
            throw new ServiceException("Service Exception", e);
        }
    }

    @Override
    public List<User> loadAll() throws ServiceException {
        return null;
    }
}