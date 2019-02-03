package com.htp.avia_booking.filter;

import com.htp.avia_booking.domain.objects.User;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserSecurityFilter implements Filter {
    private static final Logger LOGGER = Logger.getRootLogger();
    private static final String ADMIN = "admin";
    private static final String USER = "user";
    private static final String INDEX_PAGE = "/index";
    private static final String RESULT_PAGE = "/result";

    public void destroy() {}

    /**
     * The method takes two objects from the session: the client and administrator. If the client does not object,
     * the Supplies a transition to the main page of the application. When handling the customer's administrative
     * to the page, it will be redirected to the main page of the client.
     * If authentication as administrator of the request will be transmitted to the next filter in the filter chain.
     *
     * @param req - ServletRequest
     * @param resp - ServletResponse
     * @param chain - FilterChain
     *
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest request = (HttpServletRequest) req;

        HttpSession session = request.getSession();
        User administrator = (User) session.getAttribute(ADMIN);
        User user = (User) session.getAttribute(USER);

        if (user == null) {
            if (administrator == null) {
                LOGGER.info("Unauthorized attempt to enter the admin page");
                request.getRequestDispatcher(INDEX_PAGE).forward(request, response);
            }
        } else {
            LOGGER.info("Client" + user.getLogin() +  " attempt to enter the admin page");
            request.getRequestDispatcher(RESULT_PAGE).forward(request, response);
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {}
}
