<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Order" %>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>My Orders | Fashion Store</title>

    <!-- Global CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">

    <style>

        /* =====================================================
           ORDERS PAGE
        ===================================================== */

        .orders-page {

            max-width: 1200px;

            margin: 50px auto;

            padding: 0 30px;

        }


        .orders-title {

            font-size: 38px;

            margin-bottom: 8px;

            color: #182f3d;

        }


        .orders-subtitle {

            color: #777;

            margin-bottom: 40px;

        }


        /* =====================================================
           ORDER CARD
        ===================================================== */

        .order-card {

            border: 1px solid #ddd;

            background: #fff;

            padding: 25px;

            margin-bottom: 20px;

        }


        /* =====================================================
           ORDER HEADER
        ===================================================== */

        .order-header {

            display: flex;

            justify-content: space-between;

            align-items: center;

            border-bottom: 1px solid #eee;

            padding-bottom: 18px;

            margin-bottom: 18px;

        }


        .order-number {

            font-size: 18px;

            font-weight: bold;

            color: #182f3d;

        }


        .order-number a {

            color: #182f3d;

            text-decoration: none;

        }


        .order-number a:hover {

            text-decoration: underline;

        }


        .order-date {

            color: #777;

            font-size: 14px;

        }


        /* =====================================================
           ORDER INFORMATION
        ===================================================== */

        .order-info {

            display: grid;

            grid-template-columns: repeat(3, 1fr);

            gap: 25px;

        }


        .info-label {

            display: block;

            color: #777;

            font-size: 13px;

            margin-bottom: 6px;

        }


        .info-value {

            font-weight: 600;

            color: #182f3d;

        }


        /* =====================================================
           STATUS
        ===================================================== */

        .status {

            display: inline-block;

            padding: 6px 12px;

            border: 1px solid #ddd;

            font-size: 13px;

            color: #182f3d;

            background: #fafafa;

        }


        /* =====================================================
           ORDER ACTIONS
        ===================================================== */

        .order-actions {

            margin-top: 25px;

            padding-top: 20px;

            border-top: 1px solid #eee;

            display: flex;

            gap: 12px;

            align-items: center;

        }


        .view-order-button {

            display: inline-block;

            padding: 10px 18px;

            background: #182f3d;

            color: white;

            text-decoration: none;

            border: none;

            cursor: pointer;

            font-size: 14px;

        }


        .view-order-button:hover {

            opacity: 0.9;

        }


        /* =====================================================
           CANCEL BUTTON
        ===================================================== */

        .cancel-order-button {

            display: inline-block;

            padding: 10px 18px;

            background: white;

            color: #b42318;

            border: 1px solid #b42318;

            cursor: pointer;

            font-size: 14px;

        }


        .cancel-order-button:hover {

            background: #b42318;

            color: white;

        }


        /* =====================================================
           EMPTY ORDERS
        ===================================================== */

        .empty-orders {

            border: 1px solid #ddd;

            padding: 60px 20px;

            text-align: center;

        }


        .empty-orders h2 {

            color: #182f3d;

        }


        .empty-orders p {

            color: #777;

        }


        .shop-button {

            display: inline-block;

            margin-top: 20px;

            padding: 12px 25px;

            background: #182f3d;

            color: white;

            text-decoration: none;

        }


        .shop-button:hover {

            opacity: 0.9;

        }


        /* =====================================================
           MOBILE
        ===================================================== */

        @media (max-width: 800px) {

            .orders-page {

                padding: 0 15px;

            }


            .order-info {

                grid-template-columns: 1fr;

            }


            .order-header {

                flex-direction: column;

                align-items: flex-start;

                gap: 10px;

            }


            .order-actions {

                flex-direction: column;

                align-items: flex-start;

            }

        }

    </style>

</head>


<body>


<!-- =====================================================
     NAVBAR
===================================================== -->

<jsp:include page="/WEB-INF/views/partials/navbar.jsp"/>


<%
    List<Order> orders =
            (List<Order>) request.getAttribute("orders");
%>


<!-- =====================================================
     ORDERS PAGE
===================================================== -->

<div class="orders-page">


    <h1 class="orders-title">

        My Orders

    </h1>


    <p class="orders-subtitle">

        View your previous orders and their current status.

    </p>


    <%
        if (orders == null || orders.isEmpty()) {
    %>


        <!-- =================================================
             EMPTY ORDERS
        ================================================= -->

        <div class="empty-orders">

            <h2>

                No orders yet

            </h2>

            <p>

                You haven't placed any orders yet.

            </p>


            <a class="shop-button"
               href="<%= request.getContextPath() %>/products">

                Start Shopping

            </a>

        </div>


    <%
        } else {

            for (Order order : orders) {

                String orderStatus =
                        order.getOrderStatus();

                boolean canCancel =
                        orderStatus != null
                        && !"Delivered".equalsIgnoreCase(orderStatus)
                        && !"Cancelled".equalsIgnoreCase(orderStatus);
    %>


        <!-- =================================================
             ORDER CARD
        ================================================= -->

        <div class="order-card">


            <!-- =================================================
                 ORDER HEADER
            ================================================= -->

            <div class="order-header">


                <div class="order-number">

                    <a href="<%= request.getContextPath() %>/order-details?orderId=<%= order.getOrderId() %>">

                        Order #<%= order.getOrderId() %>

                    </a>

                </div>


                <div class="order-date">

                    <%= order.getOrderDate() %>

                </div>


            </div>


            <!-- =================================================
                 ORDER INFORMATION
            ================================================= -->

            <div class="order-info">


                <!-- TOTAL -->

                <div>

                    <span class="info-label">

                        Total

                    </span>

                    <span class="info-value">

                        ₹<%= order.getTotalAmount() %>

                    </span>

                </div>


                <!-- PAYMENT -->

                <div>

                    <span class="info-label">

                        Payment

                    </span>

                    <span class="info-value">

                        <%= order.getPaymentMethod() %>

                    </span>

                </div>


                <!-- PAYMENT STATUS -->

                <div>

                    <span class="info-label">

                        Payment Status

                    </span>

                    <span class="status">

                        <%= order.getPaymentStatus() %>

                    </span>

                </div>


                <!-- ORDER STATUS -->

                <div>

                    <span class="info-label">

                        Order Status

                    </span>

                    <span class="status">

                        <%= order.getOrderStatus() %>

                    </span>

                </div>


                <!-- DELIVERY ADDRESS -->

                <div>

                    <span class="info-label">

                        Delivery Address

                    </span>

                    <span class="info-value">

                        <%= order.getDeliveryAddress() %>

                    </span>

                </div>


            </div>


            <!-- =================================================
                 ORDER ACTIONS
            ================================================= -->

            <div class="order-actions">


                <!-- VIEW ORDER -->

                <a class="view-order-button"
                   href="<%= request.getContextPath() %>/order-details?orderId=<%= order.getOrderId() %>">

                    View Order

                </a>


                <%
                    if (canCancel) {
                %>


                    <!-- =================================================
                         CANCEL ORDER
                    ================================================= -->

                    <form action="<%= request.getContextPath() %>/cancel-order"
                          method="post"
                          onsubmit="return confirm('Are you sure you want to cancel Order #<%= order.getOrderId() %>?');">


                        <input type="hidden"
                               name="orderId"
                               value="<%= order.getOrderId() %>">


                        <button type="submit"
                                class="cancel-order-button">

                            Cancel Order

                        </button>


                    </form>


                <%
                    }
                %>


            </div>


        </div>


    <%
            }
        }
    %>


</div>


<!-- =====================================================
     FOOTER
===================================================== -->

<jsp:include page="/WEB-INF/views/partials/footer.jsp"/>


</body>

</html>