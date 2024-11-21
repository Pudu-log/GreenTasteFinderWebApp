<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<head>
    <link rel="stylesheet" href="/static/css/login.css">
    <link rel="icon" href="data:,">
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>

<body id="particles-js"></body>
<div class="container">
    <form name="loginForm" class="box" onsubmit="return validateForm();">
        <h4><span>Login</span></h4>
        <input type="text" name="id" placeholder="ID" autocomplete="off">
        <i class="typcn typcn-eye" id="eye"></i>
        <input type="password" name="pw" placeholder="Passsword" id="pwd" autocomplete="off">
        <input type="submit" value="Sign in" class="btn1">
    </form>

    <a href="/join" class="dnthave">Don’t have an account? Sign up</a>
</div>
<script src="/static/js/login.js"></script>