<%--
  Created by IntelliJ IDEA.
  User: 황승현
  Date: 2024-11-15
  Time: 오후 12:44
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <title>내 정보</title>
    <link rel="stylesheet" href="static/css/mypage/myPage_userInfo.css">
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

</head>
<body>
<jsp:include page="layout/header.jsp"/>
<jsp:include page="layout/nav.jsp"/>
<div class="content">
    <div class="form-container">
        <form name="update-form" class="styled-form">
            <h4>내 정보</h4>

            <div class="form-group">
                <label>가입일시</label>
                <span>${member.insertDt}</span>
            </div>

            <div class="form-group" id="update-div">

            </div>

            <div class="form-group">
                <label>아이디</label>
                <input type="text" name="id" value="${member.id}" readonly>
            </div>

            <div class="form-group">
                <label for="pw">비밀번호</label>
                <input type="password" name="pw" id="pw" value="${member.pw}" placeholder="*****" autocomplete="off"
                       readonly>
            </div>
            <div class="form-group">
                <label for="pw"></label>
                <button type="button" id="changePw-btn">비밀번호 변경</button>
            </div>

            <div class="form-group">
                <label for="name">이름</label>
                <input type="text" maxlength="20" name="name" id="name" value="${member.name}" autocomplete="off">
            </div>

            <div class="form-group">
                <label for="roomSelect">강의실</label>
                <select name="roomCode" id="roomSelect" class="room-select">
                </select>
            </div>

            <div class="form-group">
                <label for="email">이메일</label>
                <input type="text" name="email" id="email" value="${member.email}" autocomplete="off">
            </div>

            <div class="form-group">
                <label for="birth">생년월일</label>
                <input type="date" name="birth" id="birth"
                       value="<fmt:formatDate value='${member.birth}' pattern='yyyy-MM-dd' />" autocomplete="off">
            </div>

            <button type="submit" class="submit-btn">수정</button>
        </form>
    </div>
</div>

<!-- Add this before the closing </body> tag -->
<div id="passwordModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <br>
        <h4>비밀번호 변경</h4>
        <form name="passwordChangeForm">
            <div class="form-group">
                <label for="newPassword">새 비밀번호</label>
                <input type="password" id="newPassword" name="newPassword" required>
            </div>
            <div class="form-group">
                <label for="confirmPassword">비밀번호 확인</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
            <button type="submit" class="submit-btn">확인</button>
        </form>
    </div>
</div>
<jsp:include page="layout/footer.jsp"/>
</body>
<script src="/static/js/myPage/myPage_userInfo.js"></script>
<script>
    $(document).ready(function () {
        setUpdateDiv("${member.updateDt}");
        setSelect(${member.roomCode});
    })
</script>
</html>
