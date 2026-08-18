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
    position: relative;
}

.size-option input {
    position: absolute;
    opacity: 0;
    pointer-events: none;
}

.size-option label {
    display: inline-block;
    min-width: 55px;
    padding: 10px 18px;
    border: 1px solid #ccc;
    text-align: center;
    cursor: pointer;
    background: white;
    transition: 0.2s;
}

.size-option label:hover {
    border-color: #182f3d;
}

.size-option input:checked + label {
    background: #182f3d;
    color: white;
    border-color: #182f3d;
}

.quantity-title {
    font-weight: bold;
    margin-bottom: 10px;
}

.quantity-input {
    width: 70px;
    padding: 10px;
    border: 1px solid #ccc;
    margin-bottom: 25px;
}

.add-cart-button {
    display: inline-block;
    padding: 14px 30px;
    background: #182f3d;
    color: white;
    border: none;
    cursor: pointer;
    font-size: 16px;
}

.add-cart-button:hover {
    background: #24485c;
}

.no-size-message {
    color: #777;
    margin-bottom: 20px;
}

.description {
    color: #555;
    font-size: 16px;
    line-height: 1.7;
    margin-bottom: 30px;
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

    <!-- ================================
         PRODUCT IMAGE
         ================================ -->

    <div class="product-details-image">

        <img
            src="<%= request.getContextPath() %>/assets/images/<%= product.getImageUrl() %>"
            alt="<%= product.getProductName() %>">

    </div>


    <!-- ================================
         PRODUCT INFORMATION
         ================================ -->

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
        <div class="description">
    	<%= product.getDescription() %>
		</div>


        <!-- ================================
             ADD TO CART FORM
             ================================ -->

        <form
            method="post"
            action="<%= request.getContextPath() %>/cart-item">


            <!-- Product ID -->

            <input
                type="hidden"
                name="productId"
                value="<%= product.getProductId() %>">


            <!-- Unit Price -->

            <input
                type="hidden"
                name="unitPrice"
                value="<%= product.getBasePrice() %>">


            <!-- ================================
                 SIZE
                 ================================ -->

            <div class="size-title">
                Available Sizes
            </div>


            <div class="size-options">

                <%

                if (availableSizes != null &&
                    !availableSizes.isEmpty()) {

                    int sizeIndex = 0;

                    for (ProductSize size : availableSizes) {

                        sizeIndex++;

                %>

                    <div class="size-option">

                        <input
                            type="radio"
                            id="size<%= sizeIndex %>"
                            name="sizeLabel"
                            value="<%= size.getSizeLabel() %>"
                            required>

                        <label for="size<%= sizeIndex %>">

                            <%= size.getSizeLabel() %>

                        </label>

                    </div>

                <%

                    }

                } else {

                %>

                    <div class="no-size-message">

                        No sizes currently available.

                    </div>

                <%

                }

                %>

            </div>


            <!-- ================================
                 QUANTITY
                 ================================ -->

            <div class="quantity-title">

                Quantity

            </div>

            <input
                class="quantity-input"
                type="number"
                name="quantity"
                value="1"
                min="1"
                max="10"
                required>


            <!-- ================================
                 ADD TO CART
                 ================================ -->

            <%

            if (availableSizes != null &&
                !availableSizes.isEmpty()) {

            %>

                <button
                    type="submit"
                    class="add-cart-button">

                    Add to Cart

                </button>

            <%

            }

            %>

        </form>

    </div>

</div>

</main>

<jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>

</html>