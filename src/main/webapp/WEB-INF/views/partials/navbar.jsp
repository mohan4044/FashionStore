<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.fashionstore.model.User" %>

<%
    User loggedInUser =
            (User) session.getAttribute("loggedInUser");
%>

<nav class="main-navbar">

    <div class="navbar-container">

        <!-- BRAND -->

        <a class="navbar-brand"
           href="<%= request.getContextPath() %>/home">

            FASHION STORE

        </a>


        <!-- NAVIGATION -->

        <div class="navbar-links">

            <a href="<%= request.getContextPath() %>/home">
                Home
            </a>

            <a href="<%= request.getContextPath() %>/products">
                Products
            </a>

            <a href="<%= request.getContextPath() %>/products?category=1">
                Men
            </a>

            <a href="<%= request.getContextPath() %>/products?category=2">
                Women
            </a>

            <a href="<%= request.getContextPath() %>/products?category=3">
                Kids
            </a>

            <a href="<%= request.getContextPath() %>/products?category=4">
                Footwear
            </a>

            <a href="<%= request.getContextPath() %>/products?category=5">
                Accessories
            </a>

        </div>


        <!-- SEARCH -->

        <form class="navbar-search"
              method="get"
              action="<%= request.getContextPath() %>/products">

            <input
                    type="text"
                    name="keyword"
                    placeholder="Search fashion products">

            <button type="submit">
                Search
            </button>

        </form>


        <!-- USER ACTIONS -->

        <div class="navbar-actions">

            <a href="<%= request.getContextPath() %>/cart">
                Cart
            </a>


            <% if (loggedInUser != null) { %>

                <a href="<%= request.getContextPath() %>/orders">
                    My Orders
                </a>

                <span class="navbar-user">
                    Hi, <%= loggedInUser.getFullName() %>
                </span>

                <a class="logout-link"
                   href="<%= request.getContextPath() %>/logout">

                    Logout

                </a>

            <% } else { %>

                <a href="<%= request.getContextPath() %>/login">
                    Login
                </a>

                <a class="register-link"
                   href="<%= request.getContextPath() %>/register">

                    Register

                </a>

            <% } %>

        </div>

    </div>

</nav>