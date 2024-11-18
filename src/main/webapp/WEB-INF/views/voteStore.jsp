<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: GGG
  Date: 2024-11-14
  Time: 오후 5:02
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Main.jsp -->
<!DOCTYPE html>
<html lang="en">
    <%
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());
    %>
    <head>
        <link rel="stylesheet" href="/static/css/voteStore.css" />
        <meta charset="UTF-8">
        <title>Website Layout</title>
    </head>
    <body>
        <jsp:include page="layout/header.jsp" />
        <jsp:include page="layout/nav.jsp" />

        <div class="content">
            <div class="title-div">
                <input type="date" name="title-date" value="<%= now%>">
                <span>메뉴 선정</span>
            </div>

            <div class="store-content">

                <div class="store-box">
                    <div class="store-img border-tl-10">
                        <img class="border-tl-10" src="${img}">
                    </div>
                    <div class="store-wrap">
                        <div class="store-title border-tr-10">
                            <span class="border-tr-10">${store.name}</span>
                        </div>
                        <div class="store-gbn">
                            <span>${store.types[0]}</span>
                        </div>
                        <div class="store-distance">
                            <span>${distance}M</span>
                        </div>
                    </div>
                    <div class="store-2wrap">
                        <div class="store-addr">
                            <span>${store.formatted_address}</span>
                        </div>
                        <div class="store-vote-wrap">
                            <span>홍길동 외 1명</span>
                        </div>
                        <div class="store-status">
                            <span>운영중</span>
                        </div>
                        <div class="store-vote-list">
                            <ul>
                                <li>홍길동</li>
                                <li>홍길순</li>
                                <li>홍길자</li>
                                <li>홍길민</li>
                                <li>홍길민</li>
                                <li>홍길민</li>
                                <li>홍길민</li>
                                <li>홍길민</li>
                                <li>홍길민</li>
                                <li>홍길민</li>
                                <li>홍길민</li>
                                <li>홍홍홍</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="layout/footer.jsp" />

    </body>
</html>

<script type="module" src="/static/script/voteStore.js"></script>

