package com.example.bloodbankmanagementsystem2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        // Check whether user is logged in
        if (session != null &&
                session.getAttribute("loggedInUser") != null) {

            return true;
        }

        // User is not logged in
        response.sendRedirect("/login?loginRequired=true");

        return false;
    }
}