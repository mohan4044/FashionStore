package com.fashionstore.dao.impl;

import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.model.CartItem;
import com.fashionstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAOImpl implements CartItemDAO {

    @Override
    public boolean addCartItem(CartItem cartItem) {

        String sql = "INSERT INTO cart_items " +
                "(cart_id, product_id, size_label, quantity, unit_price) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartItem.getCartId());
            statement.setInt(2, cartItem.getProductId());
            statement.setString(3, cartItem.getSizeLabel());
            statement.setInt(4, cartItem.getQuantity());
            statement.setBigDecimal(5, cartItem.getUnitPrice());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CartItem getCartItemById(int cartItemId) {

        String sql = "SELECT * FROM cart_items WHERE cart_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartItemId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapCartItem(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public CartItem getCartItem(
            int cartId,
            int productId,
            String sizeLabel) {

        String sql = "SELECT * FROM cart_items " +
                "WHERE cart_id = ? AND product_id = ? AND size_label = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);
            statement.setInt(2, productId);
            statement.setString(3, sizeLabel);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapCartItem(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CartItem> getCartItemsByCartId(int cartId) {

        List<CartItem> items = new ArrayList<>();

        String sql = "SELECT * FROM cart_items " +
                "WHERE cart_id = ? ORDER BY added_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    items.add(mapCartItem(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    @Override
    public boolean updateQuantity(
            int cartItemId,
            int quantity) {

        String sql = "UPDATE cart_items SET quantity = ? " +
                "WHERE cart_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, cartItemId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean increaseQuantity(
            int cartItemId,
            int quantity) {

        String sql = "UPDATE cart_items " +
                "SET quantity = quantity + ? " +
                "WHERE cart_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, cartItemId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean decreaseQuantity(
            int cartItemId,
            int quantity) {

        String sql = "UPDATE cart_items " +
                "SET quantity = quantity - ? " +
                "WHERE cart_item_id = ? AND quantity >= ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, cartItemId);
            statement.setInt(3, quantity);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeCartItem(int cartItemId) {

        String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartItemId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeProductFromCart(
            int cartId,
            int productId,
            String sizeLabel) {

        String sql = "DELETE FROM cart_items " +
                "WHERE cart_id = ? AND product_id = ? AND size_label = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);
            statement.setInt(2, productId);
            statement.setString(3, sizeLabel);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean clearCart(int cartId) {

        String sql = "DELETE FROM cart_items WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getCartItemCount(int cartId) {

        String sql = "SELECT COALESCE(SUM(quantity), 0) " +
                "FROM cart_items WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean cartItemExists(
            int cartId,
            int productId,
            String sizeLabel) {

        String sql = "SELECT COUNT(*) FROM cart_items " +
                "WHERE cart_id = ? AND product_id = ? AND size_label = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);
            statement.setInt(2, productId);
            statement.setString(3, sizeLabel);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private CartItem mapCartItem(
            ResultSet resultSet) throws SQLException {

        CartItem item = new CartItem();

        item.setCartItemId(resultSet.getInt("cart_item_id"));
        item.setCartId(resultSet.getInt("cart_id"));
        item.setProductId(resultSet.getInt("product_id"));
        item.setSizeLabel(resultSet.getString("size_label"));
        item.setQuantity(resultSet.getInt("quantity"));
        item.setUnitPrice(resultSet.getBigDecimal("unit_price"));
        item.setAddedAt(resultSet.getTimestamp("added_at"));

        return item;
    }
}