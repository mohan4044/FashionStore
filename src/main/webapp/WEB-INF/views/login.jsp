<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Login | Fashion Store</title>

   <link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/style.css">

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/register.css">

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/login.css">

</head>

<body>

    <!-- =====================================================
         NAVBAR
         ===================================================== -->

    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />


    <!-- =====================================================
         LOGIN PAGE
         ===================================================== -->

    <main class="auth-page">

        <div class="auth-container">

            <div class="auth-header">

                <h1>Welcome Back</h1>

                <p>
                    Login to your Fashion Store account.
                </p>

            </div>


            <!-- Success Message -->

            <% if (request.getAttribute("success") != null) { %>

                <div class="form-success">

                    <%= request.getAttribute("success") %>

                </div>

            <% } %>


            <!-- Error Message -->

            <% if (request.getAttribute("error") != null) { %>

                <div class="form-error">

                    <%= request.getAttribute("error") %>

                </div>

            <% } %>


            <!-- Login Form -->

            <form
                action="${pageContext.request.contextPath}/login"
                method="post"
                class="auth-form">


                <!-- Email -->

                <div class="form-group">

                    <label for="email">
                        Email
                    </label>

                    <input
                        type="email"
                        id="email"
                        name="email"
                        value="${email}"
                        placeholder="Enter your email"
                        autocomplete="email"
                        required>

                </div>


                <!-- Password -->

                <div class="form-group">

                    <label for="password">
                        Password
                    </label>

                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Enter your password"
                        autocomplete="current-password"
                        required>

                </div>


                <!-- Submit -->

                <button
                    type="submit"
                    class="auth-button">

                    Login

                </button>

            </form>


            <!-- Register Link -->

            <div class="auth-footer">

                <p>
                    Don't have an account?

                    <a href="${pageContext.request.contextPath}/register">
                        Create Account
                    </a>
                </p>

            </div>

        </div>

    </main>


    <!-- =====================================================
         FOOTER
         ===================================================== -->

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>

</html>