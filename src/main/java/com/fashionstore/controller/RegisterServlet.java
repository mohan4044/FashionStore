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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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

        request.getRequestDispatcher(
                "/WEB-INF/views/register.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");


        // Remove unnecessary spaces
        if (fullName != null) {
            fullName = fullName.trim();
        }

        if (email != null) {
            email = email.trim();
        }

        if (phone != null) {
            phone = phone.trim();
        }

        if (gender != null) {
            gender = gender.trim();
        }

        if (address != null) {
            address = address.trim();
        }


        // =====================================================
        // BASIC VALIDATION
        // =====================================================

        if (isEmpty(fullName)
                || isEmpty(email)
                || isEmpty(phone)
                || isEmpty(password)
                || isEmpty(confirmPassword)
                || isEmpty(gender)
                || isEmpty(address)) {

            request.setAttribute(
                    "error",
                    "All fields are required."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }


        // =====================================================
        // PASSWORD CONFIRMATION
        // =====================================================

        if (!password.equals(confirmPassword)) {

            request.setAttribute(
                    "error",
                    "Passwords do not match."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }


        // =====================================================
        // EMAIL CHECK
        // =====================================================

        if (userDAO.emailExists(email)) {

            request.setAttribute(
                    "error",
                    "An account with this email already exists."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }


        // =====================================================
        // PHONE CHECK
        // =====================================================

        if (userDAO.phoneExists(phone)) {

            request.setAttribute(
                    "error",
                    "An account with this phone number already exists."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }


        // =====================================================
        // CREATE USER
        // =====================================================

        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setGender(gender);
        user.setAddress(address);


        // =====================================================
        // REGISTER
        // =====================================================

        boolean registered = userDAO.register(user);

        if (registered) {

            response.sendRedirect(
                    request.getContextPath() + "/login?registered=true"
            );

        } else {

            request.setAttribute(
                    "error",
                    "Registration failed. Please try again."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);
        }
    }


    // =========================================================
    // HELPER METHOD
    // =========================================================

    private boolean isEmpty(String value) {

        return value == null || value.trim().isEmpty();
    }
}