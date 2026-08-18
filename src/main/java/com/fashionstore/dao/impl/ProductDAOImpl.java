package com.fashionstore.dao.impl;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.model.Product;
import com.fashionstore.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products " +
                "(category_id, product_name, description, brand, base_price, " +
                "discount_percent, image_url, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, product.getCategoryId());
            statement.setString(2, product.getProductName());
            statement.setString(3, product.getDescription());
            statement.setString(4, product.getBrand());
            statement.setBigDecimal(5, product.getBasePrice());
            statement.setBigDecimal(6, product.getDiscountPercent());
            statement.setString(7, product.getImageUrl());
            statement.setBoolean(8, product.isActive());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product getProductById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapProduct(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM products ORDER BY product_id DESC";
        return getProducts(sql);
    }

    @Override
    public List<Product> getActiveProducts() {
        String sql = "SELECT * FROM products " +
                "WHERE is_active = TRUE ORDER BY product_id DESC";

        return getProducts(sql);
    }

    @Override
    public List<Product> getProductsByCategory(int categoryId) {
        String sql = "SELECT * FROM products " +
                "WHERE category_id = ? AND is_active = TRUE " +
                "ORDER BY product_id DESC";

        return getProductsWithInt(sql, categoryId);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        String sql = "SELECT * FROM products " +
                "WHERE brand = ? AND is_active = TRUE " +
                "ORDER BY product_id DESC";

        return getProductsWithString(sql, brand);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        String sql = "SELECT * FROM products " +
                "WHERE is_active = TRUE " +
                "AND (product_name LIKE ? OR brand LIKE ? OR description LIKE ?) " +
                "ORDER BY product_id DESC";

        String search = "%" + keyword + "%";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, search);
            statement.setString(2, search);
            statement.setString(3, search);

            return getProductList(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Product> searchProductsByCategory(String keyword, int categoryId) {
        String sql = "SELECT * FROM products " +
                "WHERE is_active = TRUE " +
                "AND category_id = ? " +
                "AND (product_name LIKE ? OR brand LIKE ? OR description LIKE ?) " +
                "ORDER BY product_id DESC";

        String search = "%" + keyword + "%";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);
            statement.setString(2, search);
            statement.setString(3, search);
            statement.setString(4, search);

            return getProductList(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Product> getProductsByPriceRange(
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        String sql = "SELECT * FROM products " +
                "WHERE is_active = TRUE " +
                "AND base_price BETWEEN ? AND ? " +
                "ORDER BY base_price";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, minPrice);
            statement.setBigDecimal(2, maxPrice);

            return getProductList(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Product> getProductsByCategoryAndPrice(
            int categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        String sql = "SELECT * FROM products " +
                "WHERE is_active = TRUE " +
                "AND category_id = ? " +
                "AND base_price BETWEEN ? AND ? " +
                "ORDER BY base_price";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);
            statement.setBigDecimal(2, minPrice);
            statement.setBigDecimal(3, maxPrice);

            return getProductList(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(
            int categoryId,
            String brand) {

        String sql = "SELECT * FROM products " +
                "WHERE is_active = TRUE " +
                "AND category_id = ? AND brand = ? " +
                "ORDER BY product_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);
            statement.setString(2, brand);

            return getProductList(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Product> searchProductsWithFilters(
            String keyword,
            Integer categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM products WHERE is_active = TRUE");

        List<Object> parameters = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (product_name LIKE ? " +
                    "OR brand LIKE ? OR description LIKE ?)");

            String search = "%" + keyword.trim() + "%";

            parameters.add(search);
            parameters.add(search);
            parameters.add(search);
        }

        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            parameters.add(categoryId);
        }

        if (brand != null && !brand.trim().isEmpty()) {
            sql.append(" AND brand = ?");
            parameters.add(brand);
        }

        if (minPrice != null) {
            sql.append(" AND base_price >= ?");
            parameters.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND base_price <= ?");
            parameters.add(maxPrice);
        }

        sql.append(" ORDER BY product_id DESC");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql.toString())) {

            setParameters(statement, parameters);

            return getProductList(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Product> getProductsSortedByPriceAscending() {
        String sql = "SELECT * FROM products " +
                "WHERE is_active = TRUE ORDER BY base_price ASC";

        return getProducts(sql);
    }

    @Override
    public List<Product> getProductsSortedByPriceDescending() {
        String sql = "SELECT * FROM products " +
                "WHERE is_active = TRUE ORDER BY base_price DESC";

        return getProducts(sql);
    }

    @Override
    public List<Product> getProductsSortedByNewest() {
        String sql = "SELECT * FROM products " +
                "WHERE is_active = TRUE ORDER BY created_at DESC";

        return getProducts(sql);
    }

    @Override
    public List<Product> getProductsSortedByName() {
        String sql = "SELECT * FROM products " +
                "WHERE is_active = TRUE ORDER BY product_name ASC";

        return getProducts(sql);
    }

    @Override
    public List<Product> getProductsByPage(
            int pageNumber,
            int pageSize) {

        int offset = (pageNumber - 1) * pageSize;

        String sql = "SELECT * FROM products " +
                "WHERE is_active = TRUE " +
                "ORDER BY product_id DESC LIMIT ? OFFSET ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, pageSize);
            statement.setInt(2, offset);

            return getProductList(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public int getTotalProductCount() {
        String sql = "SELECT COUNT(*) FROM products WHERE is_active = TRUE";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int getFilteredProductCount(
            String keyword,
            Integer categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM products WHERE is_active = TRUE");

        List<Object> parameters = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (product_name LIKE ? " +
                    "OR brand LIKE ? OR description LIKE ?)");

            String search = "%" + keyword.trim() + "%";

            parameters.add(search);
            parameters.add(search);
            parameters.add(search);
        }

        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            parameters.add(categoryId);
        }

        if (brand != null && !brand.trim().isEmpty()) {
            sql.append(" AND brand = ?");
            parameters.add(brand);
        }

        if (minPrice != null) {
            sql.append(" AND base_price >= ?");
            parameters.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND base_price <= ?");
            parameters.add(maxPrice);
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql.toString())) {

            setParameters(statement, parameters);

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
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET category_id = ?, " +
                "product_name = ?, description = ?, brand = ?, " +
                "base_price = ?, discount_percent = ?, image_url = ?, " +
                "is_active = ? WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, product.getCategoryId());
            statement.setString(2, product.getProductName());
            statement.setString(3, product.getDescription());
            statement.setString(4, product.getBrand());
            statement.setBigDecimal(5, product.getBasePrice());
            statement.setBigDecimal(6, product.getDiscountPercent());
            statement.setString(7, product.getImageUrl());
            statement.setBoolean(8, product.isActive());
            statement.setInt(9, product.getProductId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deactivateProduct(int productId) {
        return updateProductStatus(productId, false);
    }

    @Override
    public boolean activateProduct(int productId) {
        return updateProductStatus(productId, true);
    }

    private boolean updateProductStatus(
            int productId,
            boolean active) {

        String sql = "UPDATE products SET is_active = ? " +
                "WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, active);
            statement.setInt(2, productId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Product> getProducts(String sql) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(mapProduct(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    private List<Product> getProductsWithInt(
            String sql,
            int value) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, value);

            return getProductList(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private List<Product> getProductsWithString(
            String sql,
            String value) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, value);

            return getProductList(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private List<Product> getProductList(
            PreparedStatement statement) throws SQLException {

        List<Product> products = new ArrayList<>();

        try (ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(mapProduct(resultSet));
            }
        }

        return products;
    }

    private void setParameters(
            PreparedStatement statement,
            List<Object> parameters) throws SQLException {

        for (int i = 0; i < parameters.size(); i++) {
            Object parameter = parameters.get(i);

            if (parameter instanceof Integer) {
                statement.setInt(i + 1, (Integer) parameter);
            } else if (parameter instanceof BigDecimal) {
                statement.setBigDecimal(i + 1, (BigDecimal) parameter);
            } else {
                statement.setString(i + 1, parameter.toString());
            }
        }
    }

    private Product mapProduct(ResultSet resultSet)
            throws SQLException {

        Product product = new Product();

        product.setProductId(resultSet.getInt("product_id"));
        product.setCategoryId(resultSet.getInt("category_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setDescription(resultSet.getString("description"));
        product.setBrand(resultSet.getString("brand"));
        product.setBasePrice(resultSet.getBigDecimal("base_price"));
        product.setDiscountPercent(
                resultSet.getBigDecimal("discount_percent"));
        product.setImageUrl(resultSet.getString("image_url"));
        product.setActive(resultSet.getBoolean("is_active"));
        product.setCreatedAt(resultSet.getTimestamp("created_at"));

        return product;
    }
}