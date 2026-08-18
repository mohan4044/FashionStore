package com.fashionstore.controller;

import java.io.IOException;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String registered = request.getParameter("registered");

        if ("true".equals(registered)) {
            request.setAttribute(
                    "success",
                    "Registration successful. Please login."
            );
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/login.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email != null) {
            email = email.trim();
        }

        if (email == null || email.isEmpty()
                || password == null || password.isEmpty()) {

            request.setAttribute(
                    "error",
                    "Email and password are required."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp"
            ).forward(request, response);

            return;
        }


        // =====================================================
        // AUTHENTICATE USER
        // =====================================================

        User user = userDAO.login(email, password);

        if (user != null) {

            // Create session after successful login
            HttpSession session = request.getSession();

            session.setAttribute("loggedInUser", user);

            response.sendRedirect(
                    request.getContextPath() + "/home"
            );

        } else {

            request.setAttribute(
                    "error",
                    "Invalid email or password."
            );

            request.setAttribute(
                    "email",
                    email
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp"
            ).forward(request, response);
        }
    }
}
