package com.fashionstore.dao.impl;

import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.model.OrderItem;
import com.fashionstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements OrderItemDAO {

    @Override
    public boolean addOrderItem(OrderItem orderItem) {

        String sql = "INSERT INTO order_items " +
                "(order_id, product_id, product_name, quantity, " +
                "unit_price, subtotal, size_label) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setString(3, orderItem.getProductName());
            statement.setInt(4, orderItem.getQuantity());
            statement.setBigDecimal(5, orderItem.getUnitPrice());
            statement.setBigDecimal(6, orderItem.getSubtotal());
            statement.setString(7, orderItem.getSizeLabel());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OrderItem getOrderItemById(int orderItemId) {

        String sql = "SELECT * FROM order_items " +
                "WHERE order_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderItemId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapOrderItem(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {

        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM order_items " +
                "WHERE order_id = ? ORDER BY order_item_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    items.add(mapOrderItem(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    @Override
    public List<OrderItem> getOrderItemsByProductId(int productId) {

        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM order_items " +
                "WHERE product_id = ? ORDER BY order_item_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    items.add(mapOrderItem(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    @Override
    public boolean updateOrderItem(OrderItem orderItem) {

        String sql = "UPDATE order_items SET product_name = ?, " +
                "quantity = ?, unit_price = ?, subtotal = ?, " +
                "size_label = ? WHERE order_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, orderItem.getProductName());
            statement.setInt(2, orderItem.getQuantity());
            statement.setBigDecimal(3, orderItem.getUnitPrice());
            statement.setBigDecimal(4, orderItem.getSubtotal());
            statement.setString(5, orderItem.getSizeLabel());
            statement.setInt(6, orderItem.getOrderItemId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteOrderItem(int orderItemId) {

        String sql = "DELETE FROM order_items " +
                "WHERE order_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderItemId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteOrderItemsByOrderId(int orderId) {

        String sql = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private OrderItem mapOrderItem(
            ResultSet resultSet) throws SQLException {

        OrderItem item = new OrderItem();

        item.setOrderItemId(
                resultSet.getInt("order_item_id"));
        item.setOrderId(
                resultSet.getInt("order_id"));
        item.setProductId(
                resultSet.getInt("product_id"));
        item.setProductName(
                resultSet.getString("product_name"));
        item.setQuantity(
                resultSet.getInt("quantity"));
        item.setUnitPrice(
                resultSet.getBigDecimal("unit_price"));
        item.setSubtotal(
                resultSet.getBigDecimal("subtotal"));
        item.setSizeLabel(
                resultSet.getString("size_label"));

        return item;
    }
}