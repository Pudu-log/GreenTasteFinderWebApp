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

            </div>
        </div>

        <jsp:include page="layout/footer.jsp" />

    </body>
</html>

<script type="module" src="/static/script/voteStore.js"></script>

