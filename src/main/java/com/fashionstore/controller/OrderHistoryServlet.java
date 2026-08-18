package com.fashionstore.controller;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.model.Order;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderHistoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        Object userObject = session.getAttribute("loggedInUser");

        if (!(userObject instanceof User)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        User user = (User) userObject;

        int userId = user.getUserId();

        List<Order> orders =
                orderDAO.getOrdersByUserId(userId);

        request.setAttribute("orders", orders);

        request.getRequestDispatcher(
                "/WEB-INF/views/orders.jsp"
        ).forward(request, response);
    }
}