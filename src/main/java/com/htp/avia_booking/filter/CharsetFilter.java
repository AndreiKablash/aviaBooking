package com.htp.avia_booking.filter;

import javax.servlet.*;
import java.io.IOException;

public class CharsetFilter implements Filter {
    private String encoding;
    private ServletContext context;

    @Override
    public void destroy() {
    }

  /**
   * Method change the encoding of request and response.
   * Then they will be transferred to a method for performing a next filter in the chain.*
   * @param request ServletRequest
   * @param response ServletResponse
   * @param chain FilterChain
   *
   * @throws IOException
   * @throws ServletException
   */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);

        chain.doFilter(request, response);
    }

    /**
     * This method takes initialization parameters and adjusts
     * filter configuration object FilterConfig.
     * @param config FilterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("characterEncoding");
        context = config.getServletContext();
    }
}


