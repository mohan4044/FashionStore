package com.fashionstore.dao;

import com.fashionstore.model.ProductSize;
import java.util.List;

public interface ProductSizeDAO {

    boolean addProductSize(ProductSize productSize);

    ProductSize getProductSizeById(int productSizeId);

    ProductSize getProductSize(
            int productId,
            String sizeLabel
    );

    List<ProductSize> getSizesByProductId(int productId);

    List<ProductSize> getAvailableSizesByProductId(int productId);

    boolean updateProductSize(ProductSize productSize);

    boolean updateStock(
            int productSizeId,
            int stockQuantity
    );

    boolean decreaseStock(
            int productSizeId,
            int quantity
    );

    boolean increaseStock(
            int productSizeId,
            int quantity
    );

    boolean updateAvailability(
            int productSizeId,
            boolean available
    );

    boolean isSizeAvailable(
            int productId,
            String sizeLabel,
            int quantity
    );

    int getStockQuantity(
            int productId,
            String sizeLabel
    );

    boolean deleteProductSize(int productSizeId);
}