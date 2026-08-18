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
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String category = request.getParameter("category");
        String brand = request.getParameter("brand");

        String minPriceParam = request.getParameter("minPrice");
        String maxPriceParam = request.getParameter("maxPrice");

        String sort = request.getParameter("sort");

        Integer categoryId = parseInteger(category);
        BigDecimal minPrice = parseBigDecimal(minPriceParam);
        BigDecimal maxPrice = parseBigDecimal(maxPriceParam);

        List<Product> products;


        /*
         * =====================================================
         * PRODUCT FILTERING
         * =====================================================
         */

        boolean hasFilters =
                hasText(keyword)
                || categoryId != null
                || hasText(brand)
                || minPrice != null
                || maxPrice != null;


        if (hasFilters) {

            products = productDAO.searchProductsWithFilters(
                    emptyToNull(keyword),
                    categoryId,
                    emptyToNull(brand),
                    minPrice,
                    maxPrice
            );

        } else {

            /*
             * =================================================
             * NO FILTERS
             * =================================================
             */

            if ("priceAsc".equals(sort)) {

                products = productDAO.getProductsSortedByPriceAscending();

            } else if ("priceDesc".equals(sort)) {

                products = productDAO.getProductsSortedByPriceDescending();

            } else if ("name".equals(sort)) {

                products = productDAO.getProductsSortedByName();

            } else if ("newest".equals(sort)) {

                products = productDAO.getProductsSortedByNewest();

            } else {

                products = productDAO.getActiveProducts();
            }
        }


        /*
         * =====================================================
         * SEND DATA TO JSP
         * =====================================================
         */

        request.setAttribute("products", products);

        request.setAttribute("keyword", keyword);
        request.setAttribute("category", category);
        request.setAttribute("brand", brand);
        request.setAttribute("minPrice", minPriceParam);
        request.setAttribute("maxPrice", maxPriceParam);
        request.setAttribute("sort", sort);


        /*
         * =====================================================
         * FORWARD TO PRODUCTS JSP
         * =====================================================
         */

        request.getRequestDispatcher("/WEB-INF/views/products.jsp")
               .forward(request, response);
    }


    /*
     * =========================================================
     * HELPER METHODS
     * =========================================================
     */

    private boolean hasText(String value) {

        return value != null && !value.trim().isEmpty();
    }


    private String emptyToNull(String value) {

        if (!hasText(value)) {
            return null;
        }

        return value.trim();
    }


    private Integer parseInteger(String value) {

        if (!hasText(value)) {
            return null;
        }

        try {
            return Integer.parseInt(value.trim());

        } catch (NumberFormatException e) {

            return null;
        }
    }


    private BigDecimal parseBigDecimal(String value) {

        if (!hasText(value)) {
            return null;
        }

        try {
            return new BigDecimal(value.trim());

        } catch (NumberFormatException e) {

            return null;
        }
    }
}
