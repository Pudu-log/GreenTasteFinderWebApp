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
    <link rel="stylesheet" href="/static/css/myPage/myPage_act.css">
</head>
<body>
<jsp:include page="layout/header.jsp"/>
<jsp:include page="layout/nav.jsp"/>


<div class="content">
    <div class="restaurant-list">

    </div>
</div>

<jsp:include page="layout/footer.jsp"/>

</body>
<script>
    $(function () {
        const memberId = "${sessionScope.member.id}";
        const gubn = "${gubn}";
        setTitle(gubn);

        console.log(memberId);
        console.log(gubn);
        axios.get('/api/my/getActStoreList?gubn=' + gubn + '&id=' + memberId)
            .then(response => {
                let resDatas = response.data;
                console.log(resDatas);
                let addHtml = '';
                resDatas.forEach((res) => {
                    addHtml += '<div class="restaurant-item">';
                    addHtml += '<img src="' + res.icon + '" alt="' + res.name + '아이콘" class="restaurant-icon">';
                    addHtml += '<div class="restaurant-details">'

                    addHtml += '<h3 class="restaurant-name">' + res.name + '</h3>'
                    // addHtml += '<p class="restaurant-address">' + extractKoreanAddress(res.formatted_address) + '</p>';
                    addHtml += '<p class="restaurant-address">' + res.formatted_address + '</p>';
                    addHtml += '<p class="restaurant-phone">📞 ' + res.formatted_phone_number + '</p>';
                    addHtml += '<p class="restaurant-rating">' + reviewMake(res.rating, res.user_ratings_total) + '</p>'
                    addHtml += '<p class="restaurant-distance">거리 : ' + distance(res.geometry.location.lat, res.geometry.location.lng) + 'm</p>'
                    // addHtml += '<a href="' + res.url + '" target="_blank" class="map-link">지도에서 보기</a>'
                    addHtml += '</div>';

                    addHtml += '<div class="restaurant-images">';
                    addHtml += getPhoto(res.photos);
                    addHtml += '</div>';

                    addHtml += '</div>';
                })
                // console.log(addHtml);
                $('.restaurant-list').html(addHtml);
            })
            .catch(error => {
                console.error(error);
            });
    })

</script>
<script src="/static/js/myPage/myPage_act.js"></script>
</html>