package com.fashionstore.dao.impl;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.model.Order;
import com.fashionstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean createOrder(Order order) {

        String sql = "INSERT INTO orders " +
                "(user_id, total_amount, payment_method, payment_status, " +
                "order_status, delivery_address) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getUserId());
            statement.setBigDecimal(2, order.getTotalAmount());
            statement.setString(3, order.getPaymentMethod());
            statement.setString(4, order.getPaymentStatus());
            statement.setString(5, order.getOrderStatus());
            statement.setString(6, order.getDeliveryAddress());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                return false;
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    order.setOrderId(
                            generatedKeys.getInt(1)
                    );

                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Order getOrderById(int orderId) {

        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapOrder(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders " +
                "WHERE user_id = ? ORDER BY order_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public List<Order> getAllOrders() {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders ORDER BY order_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                orders.add(mapOrder(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public boolean updateOrderStatus(
            int orderId,
            String orderStatus) {

        String sql = "UPDATE orders SET order_status = ? " +
                "WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, orderStatus);
            statement.setInt(2, orderId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePaymentStatus(
            int orderId,
            String paymentStatus) {

        String sql = "UPDATE orders SET payment_status = ? " +
                "WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, paymentStatus);
            statement.setInt(2, orderId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateOrder(Order order) {

        String sql = "UPDATE orders SET total_amount = ?, " +
                "payment_method = ?, payment_status = ?, " +
                "order_status = ?, delivery_address = ? " +
                "WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, order.getTotalAmount());
            statement.setString(2, order.getPaymentMethod());
            statement.setString(3, order.getPaymentStatus());
            statement.setString(4, order.getOrderStatus());
            statement.setString(5, order.getDeliveryAddress());
            statement.setInt(6, order.getOrderId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cancelOrder(int orderId) {

        String sql = "UPDATE orders SET order_status = 'Cancelled' " +
                "WHERE order_id = ? " +
                "AND order_status NOT IN ('Delivered', 'Cancelled')";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getOrderCountByUserId(int userId) {

        String sql = "SELECT COUNT(*) FROM orders WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private Order mapOrder(ResultSet resultSet)
            throws SQLException {

        Order order = new Order();

        order.setOrderId(
                resultSet.getInt("order_id"));

        order.setUserId(
                resultSet.getInt("user_id"));

        order.setOrderDate(
                resultSet.getTimestamp("order_date"));

        order.setTotalAmount(
                resultSet.getBigDecimal("total_amount"));

        order.setPaymentMethod(
                resultSet.getString("payment_method"));

        order.setPaymentStatus(
                resultSet.getString("payment_status"));

        order.setOrderStatus(
                resultSet.getString("order_status"));

        order.setDeliveryAddress(
                resultSet.getString("delivery_address"));

        return order;
    }
}