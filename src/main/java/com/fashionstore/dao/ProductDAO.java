package com.fashionstore.dao;

import com.fashionstore.model.Product;
import java.math.BigDecimal;
import java.util.List;

public interface ProductDAO {

    boolean addProduct(Product product);

    Product getProductById(int productId);

    List<Product> getAllProducts();

    List<Product> getActiveProducts();

    List<Product> getProductsByCategory(int categoryId);

    List<Product> getProductsByBrand(String brand);

    List<Product> searchProducts(String keyword);

    List<Product> searchProductsByCategory(String keyword, int categoryId);

    List<Product> getProductsByPriceRange(
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

    List<Product> getProductsByCategoryAndPrice(
            int categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

    List<Product> getProductsByCategoryAndBrand(
            int categoryId,
            String brand
    );

    List<Product> searchProductsWithFilters(
            String keyword,
            Integer categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

    List<Product> getProductsSortedByPriceAscending();

    List<Product> getProductsSortedByPriceDescending();

    List<Product> getProductsSortedByNewest();

    List<Product> getProductsSortedByName();

    List<Product> getProductsByPage(
            int pageNumber,
            int pageSize
    );

    int getTotalProductCount();

    int getFilteredProductCount(
            String keyword,
            Integer categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

    boolean updateProduct(Product product);

    boolean deleteProduct(int productId);

    boolean deactivateProduct(int productId);

    boolean activateProduct(int productId);
}