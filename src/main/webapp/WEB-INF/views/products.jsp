<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Product" %>

<!DOCTYPE html>

<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>Products | Fashion Store</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/style.css">

<style>

.products-page {
    max-width: 1200px;
    margin: 50px auto;
    padding: 0 30px;
}

.products-title {
    font-size: 38px;
    color: #182f3d;
    margin-bottom: 10px;
}

.products-subtitle {
    color: #777;
    margin-bottom: 35px;
}

.product-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 25px;
}

.product-card {
    display: block;
    color: inherit;
    text-decoration: none;
    background: white;
    border: 1px solid #eee;
    padding-bottom: 20px;
    transition: transform 0.2s ease;
}

.product-card:hover {
    transform: translateY(-4px);
}

.product-image {
    width: 100%;
    height: 300px;
    overflow: hidden;
    background: #f5f5f5;
}

.product-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
}

.product-info {
    padding: 15px;
}

.product-name {
    font-size: 18px;
    font-weight: 600;
    color: #182f3d;
    margin-bottom: 7px;
}

.product-brand {
    color: #777;
    margin-bottom: 10px;
}

.product-price {
    font-weight: bold;
    color: #182f3d;
}

.empty-message {
    padding: 40px;
    text-align: center;
    color: #777;
}

@media (max-width: 900px) {

    .product-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (max-width: 550px) {

    .product-grid {
        grid-template-columns: 1fr;
    }

    .products-page {
        padding: 0 15px;
    }
}

</style>

</head>

<body>

<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<main class="products-page">

    <h1 class="products-title">
        Products
    </h1>

    <p class="products-subtitle">
        Discover fashion for every style and occasion.
    </p>

    <%

    List<Product> products =
        (List<Product>) request.getAttribute("products");

    if (products == null || products.isEmpty()) {

    %>

        <div class="empty-message">
            No products available.
        </div>

    <%

    } else {

        for (Product product : products) {

    %>

        <a class="product-card"
           href="<%= request.getContextPath() %>/product?id=<%= product.getProductId() %>">

            <div class="product-image">

                <img
                    src="<%= request.getContextPath() %>/assets/images/<%= product.getImageUrl() %>"
                    alt="<%= product.getProductName() %>">

            </div>

            <div class="product-info">

                <div class="product-name">
                    <%= product.getProductName() %>
                </div>

                <div class="product-brand">
                    <%= product.getBrand() %>
                </div>

                <div class="product-price">
                    ₹<%= product.getBasePrice() %>
                </div>

            </div>

        </a>

    <%

        }

    }

    %>

</main>

<jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>

</html>