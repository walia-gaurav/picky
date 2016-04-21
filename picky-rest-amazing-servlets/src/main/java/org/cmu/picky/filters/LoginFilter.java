package org.cmu.picky.filters;


import org.cmu.picky.services.AuthService;
import org.cmu.picky.servlets.LoginServlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {

    private static AuthService authService;

    public static void init(AuthService _authService) {
        authService = _authService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException,
            IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String loginURI = request.getContextPath() + "/api/login";
        String signUpURI = request.getContextPath() + "/api/signup";

        if (request.getRequestURI().equals(loginURI) || request.getRequestURI().endsWith(signUpURI)
                || authService.getUser(request) != null) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(LoginServlet.UNAUTHORIZED_STATUS);
        }
    }

    @Override
    public void destroy() {}

}