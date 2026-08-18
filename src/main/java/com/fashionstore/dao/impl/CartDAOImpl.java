package com.fashionstore.dao.impl;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.model.Cart;
import com.fashionstore.util.DBConnection;

import java.sql.*;

public class CartDAOImpl implements CartDAO {

    @Override
    public boolean createCart(Cart cart) {
        String sql = "INSERT INTO cart (user_id) VALUES (?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cart.getUserId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Cart getCartById(int cartId) {
        String sql = "SELECT * FROM cart WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapCart(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Cart getCartByUserId(int userId) {
        String sql = "SELECT * FROM cart WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapCart(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean updateCart(Cart cart) {
        String sql = "UPDATE cart SET updated_at = CURRENT_TIMESTAMP " +
                "WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cart.getCartId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCart(int cartId) {
        String sql = "DELETE FROM cart WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cartExistsForUser(int userId) {
        String sql = "SELECT COUNT(*) FROM cart WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Cart getOrCreateCart(int userId) {

        Cart existingCart = getCartByUserId(userId);

        if (existingCart != null) {
            return existingCart;
        }

        String sql = "INSERT INTO cart (user_id) VALUES (?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, userId);

            if (statement.executeUpdate() > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        return getCartById(keys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Cart mapCart(ResultSet resultSet) throws SQLException {

        Cart cart = new Cart();

        cart.setCartId(resultSet.getInt("cart_id"));
        cart.setUserId(resultSet.getInt("user_id"));
        cart.setCreatedAt(resultSet.getTimestamp("created_at"));
        cart.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        return cart;
    }
}