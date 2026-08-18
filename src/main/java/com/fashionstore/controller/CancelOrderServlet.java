package com.fashionstore.controller;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.dao.ProductSizeDAO;

import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;
import com.fashionstore.dao.impl.ProductSizeDAOImpl;

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

@WebServlet("/cancel-order")
public class CancelOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private ProductSizeDAO productSizeDAO;

    @Override
    public void init() throws ServletException {

        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
        productSizeDAO = new ProductSizeDAOImpl();
    }

    @Override
    protected void doPost(
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
            orderId =
                    Integer.parseInt(orderIdParameter);
        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath() + "/orders"
            );
            return;
        }

        Order order =
                orderDAO.getOrderById(orderId);

        /*
         * Security:
         * Make sure this order belongs to
         * the currently logged-in user.
         */
        if (order == null ||
                order.getUserId() != user.getUserId()) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Order not found."
            );
            return;
        }

        /*
         * Only these statuses can be cancelled.
         */
        String currentStatus =
                order.getOrderStatus();

        if ("Delivered".equalsIgnoreCase(currentStatus) ||
                "Cancelled".equalsIgnoreCase(currentStatus)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/order-details?orderId="
                            + orderId
                            + "&cancel=failed"
            );
            return;
        }

        /*
         * Get items before cancelling so that
         * stock can be returned.
         */
        List<OrderItem> orderItems =
                orderItemDAO.getOrderItemsByOrderId(orderId);

        /*
         * Mark order as cancelled.
         */
        boolean cancelled =
                orderDAO.cancelOrder(orderId);

        if (!cancelled) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/order-details?orderId="
                            + orderId
                            + "&cancel=failed"
            );
            return;
        }

        /*
         * Return ordered quantities to stock.
         */
        if (orderItems != null) {

            for (OrderItem item : orderItems) {

                if (item.getSizeLabel() == null ||
                        item.getSizeLabel().trim().isEmpty()) {
                    continue;
                }

                com.fashionstore.model.ProductSize productSize =
                        productSizeDAO.getProductSize(
                                item.getProductId(),
                                item.getSizeLabel()
                        );

                if (productSize != null) {

                    productSizeDAO.increaseStock(
                            productSize.getProductSizeId(),
                            item.getQuantity()
                    );
                }
            }
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/order-details?orderId="
                        + orderId
                        + "&cancel=success"
        );
    }
}