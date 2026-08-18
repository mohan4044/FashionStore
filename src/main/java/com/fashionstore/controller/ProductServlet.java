package com.fashionstore.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * =====================================================
         * GET PARAMETERS
         * =====================================================
         */

        String keyword =
                request.getParameter("keyword");

        String category =
                request.getParameter("category");

        String brand =
                request.getParameter("brand");

        String minPriceParam =
                request.getParameter("minPrice");

        String maxPriceParam =
                request.getParameter("maxPrice");

        String sort =
                request.getParameter("sort");


        /*
         * =====================================================
         * CATEGORY
         * =====================================================
         *
         * Supports both:
         *
         * category=1
         * category=Men
         *
         * Database:
         *
         * 1 = Men
         * 2 = Women
         * 3 = Kids
         * 4 = Footwear
         * 5 = Accessories
         *
         */

        Integer categoryId =
                getCategoryId(category);


        /*
         * =====================================================
         * PRICE
         * =====================================================
         */

        BigDecimal minPrice =
                parseBigDecimal(minPriceParam);

        BigDecimal maxPrice =
                parseBigDecimal(maxPriceParam);


        /*
         * =====================================================
         * DEBUG
         * =====================================================
         */

        System.out.println(
                "===== PRODUCT FILTER DEBUG ====="
        );

        System.out.println(
                "Category parameter = " + category
        );

        System.out.println(
                "Category ID = " + categoryId
        );


        /*
         * =====================================================
         * CHECK WHETHER FILTERS EXIST
         * =====================================================
         */

        boolean hasFilters =
                hasText(keyword)
                || categoryId != null
                || hasText(brand)
                || minPrice != null
                || maxPrice != null;


        List<Product> products;


        /*
         * =====================================================
         * FILTERED PRODUCTS
         * =====================================================
         */

        if (hasFilters) {

            products =
                    productDAO.searchProductsWithFilters(
                            emptyToNull(keyword),
                            categoryId,
                            emptyToNull(brand),
                            minPrice,
                            maxPrice
                    );

        }

        /*
         * =====================================================
         * NO FILTERS
         * =====================================================
         */

        else {

            if ("priceAsc".equals(sort)) {

                products =
                        productDAO
                                .getProductsSortedByPriceAscending();

            }

            else if ("priceDesc".equals(sort)) {

                products =
                        productDAO
                                .getProductsSortedByPriceDescending();

            }

            else if ("name".equals(sort)) {

                products =
                        productDAO
                                .getProductsSortedByName();

            }

            else if ("newest".equals(sort)) {

                products =
                        productDAO
                                .getProductsSortedByNewest();

            }

            else {

                products =
                        productDAO.getActiveProducts();
            }
        }


        /*
         * =====================================================
         * SEND PRODUCTS TO JSP
         * =====================================================
         */

        request.setAttribute(
                "products",
                products
        );

        request.setAttribute(
                "keyword",
                keyword
        );

        request.setAttribute(
                "category",
                category
        );

        request.setAttribute(
                "brand",
                brand
        );

        request.setAttribute(
                "minPrice",
                minPriceParam
        );

        request.setAttribute(
                "maxPrice",
                maxPriceParam
        );

        request.setAttribute(
                "sort",
                sort
        );


        /*
         * =====================================================
         * FORWARD TO PRODUCTS JSP
         * =====================================================
         */

        request.getRequestDispatcher(
                "/WEB-INF/views/products.jsp"
        ).forward(
                request,
                response
        );
    }


    /*
     * =========================================================
     * CATEGORY → CATEGORY ID
     * =========================================================
     *
     * IMPORTANT:
     *
     * Navbar sends:
     *
     * /products?category=1
     *
     * Home page sends:
     *
     * /products?category=Men
     *
     * This method supports BOTH.
     *
     */

    private Integer getCategoryId(String category) {

        /*
         * No category
         */

        if (!hasText(category)) {

            return null;
        }


        String value =
                category.trim();


        /*
         * =====================================================
         * FIRST: CHECK IF CATEGORY IS A NUMBER
         * =====================================================
         */

        try {

            int categoryId =
                    Integer.parseInt(value);

            /*
             * Valid category IDs from database:
             *
             * 1 Men
             * 2 Women
             * 3 Kids
             * 4 Footwear
             * 5 Accessories
             */

            if (categoryId >= 1 &&
                categoryId <= 5) {

                return categoryId;
            }

        }

        catch (NumberFormatException e) {

            /*
             * Not a number.
             *
             * Continue and check category name.
             */
        }


        /*
         * =====================================================
         * SECOND: CHECK CATEGORY NAME
         * =====================================================
         */

        if ("Men".equalsIgnoreCase(value)) {

            return 1;
        }

        if ("Women".equalsIgnoreCase(value)) {

            return 2;
        }

        if ("Kids".equalsIgnoreCase(value)) {

            return 3;
        }

        if ("Footwear".equalsIgnoreCase(value)) {

            return 4;
        }

        if ("Accessories".equalsIgnoreCase(value)) {

            return 5;
        }


        /*
         * Unknown category
         */

        return null;
    }


    /*
     * =========================================================
     * CHECK TEXT
     * =========================================================
     */

    private boolean hasText(String value) {

        return value != null
                && !value.trim().isEmpty();
    }


    /*
     * =========================================================
     * EMPTY STRING → NULL
     * =========================================================
     */

    private String emptyToNull(String value) {

        if (!hasText(value)) {

            return null;
        }

        return value.trim();
    }


    /*
     * =========================================================
     * STRING → BIGDECIMAL
     * =========================================================
     */

    private BigDecimal parseBigDecimal(String value) {

        if (!hasText(value)) {

            return null;
        }

        try {

            return new BigDecimal(
                    value.trim()
            );

        }

        catch (NumberFormatException e) {

            return null;
        }
    }
}