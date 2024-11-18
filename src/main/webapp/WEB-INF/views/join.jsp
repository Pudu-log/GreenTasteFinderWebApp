<%-- Created by IntelliJ IDEA. User: 한우성 Date: 2024-11-15 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<head>
    <link rel="stylesheet" href="/static/css/login.css">
    <link rel="icon" href="data:,">
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>

<body id="particles-js">
    <div class="container">
        <form name="signupForm" class="box" onsubmit="return validateForm()">
            <h4><span>Sign Up</span></h4>

            <div class="input-group">
                <input type="text" name="id" placeholder="ID" autocomplete="off">
                <button type="button" class="btn2" onclick="idCheck()">중복 체크</button>
            </div>


            <input type="password" name="pw" placeholder="Password" id="pwd" autocomplete="off">
            <input type="password" name="passwordConfirm" placeholder="Confirm Password" id="pwdConfirm"
                   autocomplete="off">
            <span class="error" id="passwordError">비밀번호가 일치하지 않습니다.</span>

            <input type="text" name="name" placeholder="Name" autocomplete="off">
            <select name="roomCode" id="roomSelect" class="room-select">
                <option value="">강의실을 선택해 주세요</option>
            </select>
            <input type="email" name="email" placeholder="Email" autocomplete="off">
            <input type="date" name="birth" placeholder="Date of Birth" autocomplete="off">
            <input type="submit" value="Sign Up" class="btn1">
        </form>
        <a href="/login" class="dnthave">Already have an account? Sign in</a>
    </div>
</body>

<script src="/static/js/join.js"></script>
