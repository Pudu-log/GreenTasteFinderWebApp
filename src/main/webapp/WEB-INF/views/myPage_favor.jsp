<%--
  Created by IntelliJ IDEA.
  User: 황승현
  Date: 2024-11-15
  Time: 오후 12:45
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>즐겨찾기 목록</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
<jsp:include page="layout/header.jsp"/>
<jsp:include page="layout/nav.jsp"/>

<div class="content">
    <table>
        <thead>
        <tr>
            <th></th>
        </tr>
        </thead>
        <tbody id="add-tbody">
        </tbody>
    </table>
</div>

<jsp:include page="layout/footer.jsp"/>

</body>
<script>
    $(function () {
        let memberId = "${sessionScope.member.id}";
        console.log(memberId);
        axios.get('/api/my/getFavorList?val=B&id=' + memberId).then(response => {
            console.log(response);
        }).catch(error => {
            console.error(error);
        });
    })
</script>
</html>