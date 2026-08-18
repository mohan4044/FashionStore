package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductSizeDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.dao.impl.ProductSizeDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductSize;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/product")
public class ProductDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;
    private ProductSizeDAO productSizeDAO;

    @Override
    public void init() throws ServletException {

        productDAO = new ProductDAOImpl();
        productSizeDAO = new ProductSizeDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String idParameter = request.getParameter("id");

        if (idParameter == null ||
                idParameter.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Product ID is required."
            );

            return;
        }

        try {

            int productId =
                    Integer.parseInt(idParameter);

            Product product =
                    productDAO.getProductById(productId);

            if (product == null) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Product not found."
                );

                return;
            }

            List<ProductSize> availableSizes =
                    productSizeDAO.getAvailableSizesByProductId(
                            productId
                    );

            request.setAttribute(
                    "product",
                    product
            );

            request.setAttribute(
                    "availableSizes",
                    availableSizes
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/product-details.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product ID."
            );
        }
    }
}