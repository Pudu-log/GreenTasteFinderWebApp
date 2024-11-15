<%--
  Created by IntelliJ IDEA.
  User: 황승현
  Date: 2024-11-15
  Time: 오전 11:50
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>마이페이지</title>

    <link rel="stylesheet" href="/static/css/myPage.css"/>
</head>
<body>
<jsp:include page="layout/header.jsp"/>
<jsp:include page="layout/nav.jsp"/>

<div class="content">
    <div class="top">
        <h1>마이페이지</h1>
    </div>

    <div class="bottom">
        <div class="my-menus">
            <a href="myPage-userInfo">
                <img class="my-img" src="static/images/user-info.png">
                <p>내 정보</p>
            </a>
        </div>
        <div class="my-menus">
            <a href="myPage-favor">
                <img class="my-img" src="static/images/favor.png">
                <p>즐겨찾기 목록</p>
            </a>
        </div>
        <div class="my-menus">
            <a href="myPage-visit">
                <img class="my-img" src="static/images/flag.png">
                <p>방문기록</p>
            </a>
        </div>
        <div class="my-menus">
            <a href="myPage-good">
                <img class="my-img" src="static/images/good.png">
                <p>좋아요 목록</p>
            </a>
        </div>
    </div>
</div>

<jsp:include page="layout/footer.jsp"/>

</body>
</html>