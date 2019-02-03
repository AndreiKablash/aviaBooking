package com.htp.avia_booking.controller.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
    * Interface provide method for dealing with HttpServletRequest and HttpServletResponse
    */

public interface CommandInterface {
    /**
    * Method performs some logic to process the request and determines
    * which page will be a transition after completion method
    * @param request HttpServletRequest
    * @param response HttpServletResponse
    * @return the path to go to a specific page
    * * @throws CommandException
   */
        String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException;
}

