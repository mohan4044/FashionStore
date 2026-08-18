package com.fashionstore.dao;

import com.fashionstore.model.Order;
import java.util.List;

public interface OrderDAO {

    boolean createOrder(Order order);

    Order getOrderById(int orderId);

    List<Order> getOrdersByUserId(int userId);

    List<Order> getAllOrders();

    boolean updateOrderStatus(
            int orderId,
            String orderStatus
    );

    boolean updatePaymentStatus(
            int orderId,
            String paymentStatus
    );

    boolean updateOrder(Order order);

    boolean cancelOrder(int orderId);

    int getOrderCountByUserId(int userId);
}