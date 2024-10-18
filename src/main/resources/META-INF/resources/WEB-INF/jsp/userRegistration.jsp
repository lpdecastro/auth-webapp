<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>User Registration Page</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script type="text/javascript" src="js/jquery-3.7.1.min.js"></script>
    <script>
        $(document).ready(function () {
            console.log("ready!");
            $("#email").blur(function () {
                $.ajax({
                    type: "POST",
                    url: "findByEmail",
                    data: {"email": $("#email").val(), "_csrf": $("#csrf").val()},
                    success: function (emailExists) {
                        if (emailExists) {
                            $("#errorMessage").html("User already exists.");
                        } else {
                            $("#errorMessage").html("");
                        }
                    }
                });
            });
        });
    </script>
</head>
<body>
<!-- Section: Design Block -->
<section class="text-center text-lg-start">
    <style>
        .cascading-right {
            margin-right: -50px;
        }

        @media (max-width: 991.98px) {
            .cascading-right {
                margin-right: 0;
            }
        }
    </style>

    <!-- Jumbotron -->
    <div class="container py-4">
        <div class="row g-0 align-items-center">
            <div class="col-lg-6 mb-5 mb-lg-0">
                <div class="card cascading-right" style="
            background: hsla(0, 0%, 100%, 0.55);
            backdrop-filter: blur(30px);
            ">
                    <div class="card-body p-5 shadow-5 text-center">
                        <h2 class="fw-bold mb-5">Sign up now</h2>

                        <div id="errorMessage" class="divider d-flex align-items-center my-4 text-danger">
                            <p class="text-center fw-bold mx-3 mb-0">${errorMessage}</p>
                        </div>

                        <form action="registerUser" method="post">
                            <!-- 2 column grid layout with text inputs for the first and last names -->
                            <div class="row">
                                <div class="col-md-6 mb-4">
                                    <div class="form-outline">
                                        <input type="text" id="form3Example1"
                                               oninvalid="setCustomValidity('Please enter First Name')"
                                               oninput="this.setCustomValidity('')"
                                               name="firstName" class="form-control" required/>
                                        <label class="form-label" for="form3Example1">First name</label>
                                    </div>
                                </div>
                                <div class="col-md-6 mb-4">
                                    <div class="form-outline">
                                        <input type="text" id="form3Example2"
                                               oninvalid="setCustomValidity('Please enter Last Name')"
                                               oninput="this.setCustomValidity('')"
                                               name="lastName" class="form-control" required/>
                                        <label class="form-label" for="form3Example2">Last name</label>
                                    </div>
                                </div>
                            </div>

                            <!-- Email input -->
                            <div class="form-outline mb-4">
                                <input type="email" id="email"
                                       oninvalid="setCustomValidity('Please enter valid email')"
                                       oninput="this.setCustomValidity('')"
                                       name="email" class="form-control" required/>
                                <label class="form-label" for="form3Example3">Email address</label>
                            </div>

                            <!-- Password input -->
                            <div class="form-outline mb-4">
                                <input type="password" id="form3Example4" name="password"
                                       oninvalid="setCustomValidity('Please enter password')"
                                       oninput="this.setCustomValidity('')"
                                       class="form-control" required/>
                                <label class="form-label" for="form3Example4">Password</label>
                            </div>

                            <input name="_csrf" id="csrf" type="hidden" value="${_csrf.token}"/>

                            <!-- Submit button -->
                            <button type="submit" class="btn btn-primary btn-block mb-4">
                                Sign up
                            </button>

                            <!-- Register buttons -->
                            <div class="text-center">
                                <p>or sign up with:</p>
                                <button type="button" class="btn btn-link btn-floating mx-1">
                                    <i class="fab fa-facebook-f"></i>
                                </button>

                                <button type="button" class="btn btn-link btn-floating mx-1">
                                    <i class="fab fa-google"></i>
                                </button>

                                <button type="button" class="btn btn-link btn-floating mx-1">
                                    <i class="fab fa-twitter"></i>
                                </button>

                                <button type="button" class="btn btn-link btn-floating mx-1">
                                    <i class="fab fa-github"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="col-lg-6 mb-5 mb-lg-0">
                <img src="https://mdbootstrap.com/img/new/ecommerce/vertical/004.jpg" class="w-100 rounded-4 shadow-4"
                     alt=""/>
            </div>
        </div>
    </div>
    <!-- Jumbotron -->
</section>
<!-- Section: Design Block -->
</body>
</html>