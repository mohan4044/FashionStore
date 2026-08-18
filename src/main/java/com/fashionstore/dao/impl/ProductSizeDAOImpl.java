package com.fashionstore.dao.impl;

import com.fashionstore.dao.ProductSizeDAO;
import com.fashionstore.model.ProductSize;
import com.fashionstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductSizeDAOImpl implements ProductSizeDAO {

    @Override
    public boolean addProductSize(ProductSize productSize) {
        String sql = "INSERT INTO product_sizes " +
                "(product_id, size_label, stock_quantity, sku_code, is_available) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productSize.getProductId());
            statement.setString(2, productSize.getSizeLabel());
            statement.setInt(3, productSize.getStockQuantity());
            statement.setString(4, productSize.getSkuCode());
            statement.setBoolean(5, productSize.isAvailable());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ProductSize getProductSizeById(int productSizeId) {
        String sql = "SELECT * FROM product_sizes WHERE product_size_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productSizeId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapProductSize(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ProductSize getProductSize(
            int productId,
            String sizeLabel) {

        String sql = "SELECT * FROM product_sizes " +
                "WHERE product_id = ? AND size_label = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            statement.setString(2, sizeLabel);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapProductSize(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ProductSize> getSizesByProductId(int productId) {
        String sql = "SELECT * FROM product_sizes " +
                "WHERE product_id = ? ORDER BY product_size_id";

        return getSizesWithProductId(sql, productId);
    }

    @Override
    public List<ProductSize> getAvailableSizesByProductId(int productId) {
        String sql = "SELECT * FROM product_sizes " +
                "WHERE product_id = ? " +
                "AND is_available = TRUE " +
                "AND stock_quantity > 0 " +
                "ORDER BY product_size_id";

        return getSizesWithProductId(sql, productId);
    }

    @Override
    public boolean updateProductSize(ProductSize productSize) {
        String sql = "UPDATE product_sizes SET size_label = ?, " +
                "stock_quantity = ?, sku_code = ?, is_available = ? " +
                "WHERE product_size_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, productSize.getSizeLabel());
            statement.setInt(2, productSize.getStockQuantity());
            statement.setString(3, productSize.getSkuCode());
            statement.setBoolean(4, productSize.isAvailable());
            statement.setInt(5, productSize.getProductSizeId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStock(
            int productSizeId,
            int stockQuantity) {

        String sql = "UPDATE product_sizes " +
                "SET stock_quantity = ? WHERE product_size_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, stockQuantity);
            statement.setInt(2, productSizeId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean decreaseStock(
            int productSizeId,
            int quantity) {

        String sql = "UPDATE product_sizes " +
                "SET stock_quantity = stock_quantity - ? " +
                "WHERE product_size_id = ? " +
                "AND stock_quantity >= ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, productSizeId);
            statement.setInt(3, quantity);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean increaseStock(
            int productSizeId,
            int quantity) {

        String sql = "UPDATE product_sizes " +
                "SET stock_quantity = stock_quantity + ? " +
                "WHERE product_size_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, productSizeId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateAvailability(
            int productSizeId,
            boolean available) {

        String sql = "UPDATE product_sizes SET is_available = ? " +
                "WHERE product_size_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, available);
            statement.setInt(2, productSizeId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isSizeAvailable(
            int productId,
            String sizeLabel,
            int quantity) {

        String sql = "SELECT stock_quantity FROM product_sizes " +
                "WHERE product_id = ? " +
                "AND size_label = ? " +
                "AND is_available = TRUE";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            statement.setString(2, sizeLabel);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("stock_quantity") >= quantity;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int getStockQuantity(
            int productId,
            String sizeLabel) {

        String sql = "SELECT stock_quantity FROM product_sizes " +
                "WHERE product_id = ? AND size_label = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            statement.setString(2, sizeLabel);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("stock_quantity");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean deleteProductSize(int productSizeId) {
        String sql = "DELETE FROM product_sizes WHERE product_size_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productSizeId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<ProductSize> getSizesWithProductId(
            String sql,
            int productId) {

        List<ProductSize> sizes = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    sizes.add(mapProductSize(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sizes;
    }

    private ProductSize mapProductSize(
            ResultSet resultSet) throws SQLException {

        ProductSize productSize = new ProductSize();

        productSize.setProductSizeId(
                resultSet.getInt("product_size_id"));
        productSize.setProductId(
                resultSet.getInt("product_id"));
        productSize.setSizeLabel(
                resultSet.getString("size_label"));
        productSize.setStockQuantity(
                resultSet.getInt("stock_quantity"));
        productSize.setSkuCode(
                resultSet.getString("sku_code"));
        productSize.setAvailable(
                resultSet.getBoolean("is_available"));

        return productSize;
    }
}