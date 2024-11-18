<%@ page import="com.fasterxml.jackson.databind.util.JSONPObject" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>상세 페이지</title>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="stylesheet" href="/static/css/detail.css"/>
</head>
<body>
<jsp:include page="layout/header.jsp"/>
<jsp:include page="layout/nav.jsp"/>


<div class="title-container">
    <h2 id="title">제목</h2>
    <ul>
        <li id="open_now">open_now</li>
        <li id="rating">rating</li>
        <li id="reviewLength">review.length</li>
    </ul>
</div>

<div class="button-container">
    <button class="action-button" id="like-button">
        좋아요
        <span id="like-count" class="count">
            0
        </span>
    </button>
    <button class="action-button" id="favorite-button">즐겨찾기</button>
    <button class="action-button" id="visit-button">방문</button>
</div>

<div id="googleMap"></div>

<div class="container">
    <ul class="tabs">
        <li class="tab-link current" data-tab="tab-1">운영시간</li>
        <li class="tab-link" data-tab="tab-2">사진</li>
        <li class="tab-link" data-tab="tab-3">리뷰</li>
    </ul>

    <div id="tab-1" class="tab-content current">
        weekday_text
    </div>
    <div id="tab-2" class="tab-content">
        photo
    </div>
    <div id="tab-3" class="tab-content">
        reviews
    </div>
</div>


<script type="text/javascript">
    let inputData =
    ${response}.result;
</script>
<script>
    //좋아요 전 인원 조회하고
    axios.post('/api/admin/room-list')
        .then((response) => {
            console.log(response);
            console.log(response.data);
        })
        .catch((error) => {
            console.log(error);
        })

    $('#like-count').text();
    //북마크 조회하고
    //방문여부 조회

    //좋아요 추가하기(재클릭 -> 삭제)
    //추가하고 좋아요 수 갱신
    $('#like-button').click(function () {

    })

    //북마크 추가하기(재클릭 -> 삭제)
    //북마크 여부 갱신
    $('#favorite-button').click(function () {

    })

    //방문여부 추가하기(삭제x)
    //방문여부 갱신
    $('#visit-button').click(function () {

    })


</script>
<script src="/static/js/detailPage/detailPage_Tab.js"></script>
<script src="/static/js/detailPage/detailPage_Map.js"></script>
<!-- 구글맵 API 호출 -->
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g&callback=initMap" async
        defer></script>

<jsp:include page="layout/footer.jsp"/>
</body>
</html>