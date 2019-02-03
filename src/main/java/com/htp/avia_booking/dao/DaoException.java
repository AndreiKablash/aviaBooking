package com.htp.avia_booking.dao;

/**
 * class to create DaoException object
 */
public class DaoException extends Exception {

        private static final long serialVersionUID = 1L;

        public DaoException(String message){
            super(message);
        }

        public DaoException(String message, Exception ex){
            super(message, ex);
        }
}
