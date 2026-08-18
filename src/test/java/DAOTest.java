import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;

import java.util.List;

public class DAOTest {

    public static void main(String[] args) {

        ProductDAO productDAO = new ProductDAOImpl();

        System.out.println("===== ALL PRODUCTS =====");

        List<Product> products = productDAO.getAllProducts();

        for (Product product : products) {
            System.out.println(
                    product.getProductId() + " | " +
                    product.getProductName() + " | " +
                    product.getBrand() + " | ₹" +
                    product.getBasePrice()
            );
        }

        System.out.println("\n===== MEN'S PRODUCTS =====");

        List<Product> menProducts =
                productDAO.getProductsByCategory(1);

        for (Product product : menProducts) {
            System.out.println(
                    product.getProductName() + " | " +
                    product.getBrand()
            );
        }

        System.out.println("\n===== SEARCH: NIKE =====");

        List<Product> searchResults =
                productDAO.searchProducts("Nike");

        for (Product product : searchResults) {
            System.out.println(
                    product.getProductName() + " | " +
                    product.getBrand()
            );
        }

        System.out.println("\n===== PRICE RANGE =====");

        List<Product> priceResults =
                productDAO.getProductsByPriceRange(
                        new java.math.BigDecimal("1000"),
                        new java.math.BigDecimal("3000")
                );

        for (Product product : priceResults) {
            System.out.println(
                    product.getProductName() + " | ₹" +
                    product.getBasePrice()
            );
        }
    }
}