<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="com.fashionstore.model.ProductSize" %>

<!DOCTYPE html>

<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>Product Details | Fashion Store</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/style.css">

<style>

.product-details-page {
    max-width: 1100px;
    margin: 50px auto;
    padding: 0 30px;
}

.product-details {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 50px;
}

.product-details-image {
    width: 100%;
    height: 550px;
    background: #f5f5f5;
    overflow: hidden;
}

.product-details-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.product-details-info h1 {
    font-size: 38px;
    color: #182f3d;
    margin-bottom: 10px;
}

.brand {
    color: #777;
    font-size: 18px;
    margin-bottom: 20px;
}

.price {
    font-size: 28px;
    font-weight: bold;
    color: #182f3d;
    margin-bottom: 30px;
}

.size-title {
    font-weight: bold;
    margin-bottom: 10px;
}

.size-options {
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
    margin-bottom: 25px;
}

.size-option {
    padding: 10px 18px;
    border: 1px solid #ccc;
}

.add-cart-button {
    display: inline-block;
    padding: 14px 30px;
    background: #182f3d;
    color: white;
    border: none;
    text-decoration: none;
    cursor: pointer;
}

@media (max-width: 750px) {

    .product-details {
        grid-template-columns: 1fr;
    }

    .product-details-image {
        height: 400px;
    }

    .product-details-page {
        padding: 0 15px;
    }
}

</style>

</head>

<body>

<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<%

Product product =
    (Product) request.getAttribute("product");

List<ProductSize> availableSizes =
    (List<ProductSize>)
        request.getAttribute("availableSizes");

%>

<main class="product-details-page">

    <div class="product-details">

        <div class="product-details-image">

            <img
                src="<%= request.getContextPath() %>/assets/images/<%= product.getImageUrl() %>"
                alt="<%= product.getProductName() %>">

        </div>

        <div class="product-details-info">

            <h1>
                <%= product.getProductName() %>
            </h1>

            <div class="brand">
                <%= product.getBrand() %>
            </div>

            <div class="price">
                ₹<%= product.getBasePrice() %>
            </div>

            <div class="size-title">
                Available Sizes
            </div>

            <div class="size-options">

                <%

                if (availableSizes != null &&
                    !availableSizes.isEmpty()) {

                    for (ProductSize size :
                         availableSizes) {

                %>

                    <div class="size-option">
                        <%= size.getSizeLabel() %>
                    </div>

                <%

                    }

                } else {

                %>

                    <div>
                        No sizes currently available.
                    </div>

                <%

                }

                %>

            </div>

            <a
                class="add-cart-button"
                href="<%= request.getContextPath() %>/cart">

                Add to Cart

            </a>

        </div>

    </div>

</main>

<jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>

</html>