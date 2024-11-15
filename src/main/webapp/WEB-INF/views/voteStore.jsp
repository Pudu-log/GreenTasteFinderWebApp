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
                        <img class="border-tl-10" src="https://breffee.net/data/editor/2210/20221013104826_fd5326c8ac17c04c88d91f03a8d313d8_5r8y.jpg">
                    </div>
                    <div class="store-wrap">
                        <div class="store-title border-tr-10">
                            <span class="border-tr-10"> 나는 가게이름~ 나는 가게이름~ 나는 가게이름~ 나는 가게이름~ 나는 가게이름~ 나는 가게이름~ 나는 가게이름~ 나는 가게이름~</span>
                        </div>
                        <div class="store-gbn">
                            <span>나는 중식!</span>
                        </div>
                        <div class="store-distance">
                            <span>100m</span>
                        </div>
                    </div>
                    <div class="store-2wrap">
                        <div class="store-addr">
                            <span>나는 주소~ 나는 주소~ 나는 주소~ 나는 주소~ 나는 주소~ 나는 주소~ 나는 주소~ 나는 주소~ 나는 주소~ 나는 주소~ </span>
                        </div>

                    </div>
                </div>
                <div class="store-box"></div>
                <div class="store-box"></div>
                <div class="store-box"></div>
                <div class="store-box"></div>
                <div class="store-box"></div>
                <div class="store-box"></div>
            </div>
            <%--<div class="store-list">
                <div class="store">
                    <div class="store-img">
                        <img src="https://breffee.net/data/editor/2210/20221013104826_fd5326c8ac17c04c88d91f03a8d313d8_5r8y.jpg">
                    </div>
                </div>
            </div>--%>
        </div>

        <jsp:include page="layout/footer.jsp" />

    </body>
</html>