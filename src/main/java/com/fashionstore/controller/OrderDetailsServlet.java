package com.fashionstore.controller;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/order-details")
public class OrderDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
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

        Object userObject =
                session.getAttribute("loggedInUser");

        if (!(userObject instanceof User)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        User user = (User) userObject;

        String orderIdParameter =
                request.getParameter("orderId");

        if (orderIdParameter == null ||
                orderIdParameter.trim().isEmpty()) {

            response.sendRedirect(
                    request.getContextPath() + "/orders"
            );
            return;
        }

        int orderId;

        try {
            orderId = Integer.parseInt(orderIdParameter);
        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath() + "/orders"
            );
            return;
        }

        Order order = orderDAO.getOrderById(orderId);

        if (order == null ||
                order.getUserId() != user.getUserId()) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Order not found."
            );
            return;
        }

        List<OrderItem> orderItems =
                orderItemDAO.getOrderItemsByOrderId(orderId);

        request.setAttribute("order", order);
        request.setAttribute("orderItems", orderItems);

        request.getRequestDispatcher(
                "/WEB-INF/views/order-details.jsp"
        ).forward(request, response);
    }
}