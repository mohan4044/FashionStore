<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Order" %>
<%@ page import="com.fashionstore.model.OrderItem" %>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Order Details | Fashion Store</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/style.css">

<style>

.order-details-page {
    max-width: 1100px;
    margin: 50px auto;
    padding: 0 30px;
}

.order-details-title {
    font-size: 38px;
    color: #182f3d;
    margin-bottom: 10px;
}

.order-details-subtitle {
    color: #777;
    margin-bottom: 35px;
}

.details-section {
    border: 1px solid #ddd;
    background: #fff;
    padding: 30px;
    margin-bottom: 25px;
}

.details-section h2 {
    margin-top: 0;
    color: #182f3d;
}

.order-info {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
}

.info-label {
    display: block;
    color: #777;
    font-size: 13px;
    margin-bottom: 5px;
}

.info-value {
    font-weight: 600;
}

.order-item {
    display: flex;
    justify-content: space-between;
    border-bottom: 1px solid #eee;
    padding: 18px 0;
}

.order-item:last-child {
    border-bottom: none;
}

.item-name {
    font-weight: 600;
}

.item-details {
    color: #777;
    font-size: 14px;
    margin-top: 6px;
    line-height: 1.6;
}

.item-price {
    font-weight: 600;
    white-space: nowrap;
}

.total-row {
    display: flex;
    justify-content: space-between;
    border-top: 1px solid #ddd;
    padding-top: 20px;
    margin-top: 15px;
    font-size: 22px;
    font-weight: bold;
    color: #182f3d;
}

.action-row {
    display: flex;
    gap: 15px;
    flex-wrap: wrap;
    margin-top: 25px;
}

.back-button,
.cancel-button {
    display: inline-block;
    padding: 12px 25px;
    text-decoration: none;
    border: none;
    cursor: pointer;
    font-size: 15px;
}

.back-button {
    background: #182f3d;
    color: white;
}

.cancel-button {
    background: #a33;
    color: white;
}

.cancel-button:hover {
    background: #822;
}

.success-message {
    padding: 15px;
    margin-bottom: 25px;
    background: #eaf7ed;
    color: #26733a;
    border: 1px solid #b9dfc2;
}

.error-message {
    padding: 15px;
    margin-bottom: 25px;
    background: #f8eaea;
    color: #a33;
    border: 1px solid #e5bcbc;
}

.status-badge {
    display: inline-block;
    padding: 5px 12px;
    border: 1px solid #ddd;
    font-size: 14px;
}

@media (max-width: 700px) {

    .order-details-page {
        padding: 0 15px;
    }

    .order-info {
        grid-template-columns: 1fr;
    }

    .order-item {
        gap: 20px;
    }
}

</style>

</head>

<body>

<jsp:include page="partials/navbar.jsp"/>

<%

Order order =
        (Order) request.getAttribute("order");

List<OrderItem> orderItems =
        (List<OrderItem>) request.getAttribute("orderItems");

String cancelStatus =
        request.getParameter("cancel");

%>

<div class="order-details-page">

<% if ("success".equals(cancelStatus)) { %>

    <div class="success-message">
        Order cancelled successfully. The ordered stock has been returned.
    </div>

<% } else if ("failed".equals(cancelStatus)) { %>

    <div class="error-message">
        This order could not be cancelled.
    </div>

<% } %>


<h1 class="order-details-title">

    Order #<%= order.getOrderId() %>

</h1>


<p class="order-details-subtitle">

    Order placed on <%= order.getOrderDate() %>

</p>


<!-- ORDER INFORMATION -->

<div class="details-section">

    <h2>Order Information</h2>

    <div class="order-info">

        <div>

            <span class="info-label">
                Order Status
            </span>

            <span class="info-value status-badge">
                <%= order.getOrderStatus() %>
            </span>

        </div>


        <div>

            <span class="info-label">
                Payment Status
            </span>

            <span class="info-value">
                <%= order.getPaymentStatus() %>
            </span>

        </div>


        <div>

            <span class="info-label">
                Payment Method
            </span>

            <span class="info-value">
                <%= order.getPaymentMethod() %>
            </span>

        </div>


        <div>

            <span class="info-label">
                Delivery Address
            </span>

            <span class="info-value">
                <%= order.getDeliveryAddress() %>
            </span>

        </div>

    </div>

</div>


<!-- ORDER ITEMS -->

<div class="details-section">

    <h2>Items</h2>

<%

if (orderItems != null &&
        !orderItems.isEmpty()) {

    for (OrderItem item : orderItems) {

%>

    <div class="order-item">

        <div>

            <div class="item-name">
                <%= item.getProductName() %>
            </div>

            <div class="item-details">

                Size:
                <%= item.getSizeLabel() %>

                <br>

                Quantity:
                <%= item.getQuantity() %>

                <br>

                Unit Price:
                ₹<%= item.getUnitPrice() %>

            </div>

        </div>

        <div class="item-price">

            ₹<%= item.getSubtotal() %>

        </div>

    </div>

<%

    }

} else {

%>

    <p>No items found for this order.</p>

<%

}

%>


<div class="total-row">

    <span>
        Total
    </span>

    <span>
        ₹<%= order.getTotalAmount() %>
    </span>

</div>

</div>


<!-- ACTIONS -->

<div class="action-row">

    <a class="back-button"
       href="<%= request.getContextPath() %>/orders">

        ← Back to My Orders

    </a>


<%

String status = order.getOrderStatus();

boolean canCancel =
        status != null &&
        !status.equalsIgnoreCase("Delivered") &&
        !status.equalsIgnoreCase("Cancelled");

if (canCancel) {

%>

    <form method="post"
          action="<%= request.getContextPath() %>/cancel-order"
          onsubmit="return confirm('Are you sure you want to cancel this order?');">

        <input type="hidden"
               name="orderId"
               value="<%= order.getOrderId() %>">

        <button type="submit"
                class="cancel-button">

            Cancel Order

        </button>

    </form>

<%

}

%>

</div>

</div>


<jsp:include page="partials/footer.jsp"/>

</body>

</html>