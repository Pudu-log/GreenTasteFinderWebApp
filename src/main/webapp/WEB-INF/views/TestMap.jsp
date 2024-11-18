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
<%
    // TODO  테스트용 세션 나중에 지워야 함
    session.setAttribute("testId", "aaa50");
%>
<jsp:include page="layout/header.jsp"/>
<jsp:include page="layout/nav.jsp"/>


<div class="title-container">
    <h2 id="title">제목</h2>
    <ul>
        <li id="open_now">오픈여부 미확인</li>
        <li id="rating">별점 없음</li>
        <li id="reviewLength">리뷰 없음</li>
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

    <div id="tab-1" class="tab-content current"></div>
    <div id="tab-2" class="tab-content"></div>
    <div id="tab-3" class="tab-content"></div>
</div>


<script type="text/javascript">
    let inputData =
    ${response}.result;
    // let sessionId = "${sessionScope.testId}";
    let sessionId = "${sessionScope.member.id}";
    let placeId = "${placeId}";
</script>
<script>
    axios.get('/api/detailpage/selectTotalAct?id=' + sessionId + '&store_id=' + placeId)
        .then((response) => {
            console.log(response.data);
            $('#like-count').text(response.data.totalG);
            if (response.data.gcnt > 0) {
                $('#like-button').addClass('active');
            }
            if (response.data.fcnt > 0) {
                $('#favorite-button').addClass('active');
            }
            if (response.data.vcnt > 0) {
                $('#visit-button').addClass('active');
            }
        })
        .catch((error) => {
            console.log(error);
        })

    function deleteAct(id, store_id, gubn, inputId) {
        axios.delete('api/detailpage/deleteAct?id=' + id + '&store_id=' + store_id + '&gubn=' + gubn)
            .then((response) => {
                console.log(response.data);
                inputId.removeClass('active');
                likeAllSelect(placeId);
            })
            .catch((error) => {
                console.log(error);
            })
    }

    function insertAct(id, store_id, gubn, inputId) {
        axios.post('/api/detailpage/insertAct', {
            id: id,
            store_id: store_id,
            gubn: gubn
        })
            .then((response) => {
                inputId.addClass('active');
                console.log(response.data);
                likeAllSelect(placeId);
            })
            .catch((error) => {
                console.log(error);
            })
    }

    function likeAllSelect(placeId){
        axios.get('/api/detailpage/likeAllSelect?store_id=' + placeId)
            .then((response) => {
                console.log(response.data);
                $('#like-count').text(response.data);
            })
            .catch((error) => {
                console.log(error);
            })
    }

    $('#like-button').click(function () {
        if ($(this).hasClass('active')) {
            deleteAct(sessionId, placeId, 'G', $(this));
        } else {
            insertAct(sessionId, placeId, 'G', $(this));
        }
    })

    $('#favorite-button').click(function () {
        if ($(this).hasClass('active')) {
            deleteAct(sessionId, placeId, 'F', $(this));
        } else {
            insertAct(sessionId, placeId, 'F', $(this));
        }
    })

    $('#visit-button').click(function () {
        if ($(this).hasClass('active')) {
            deleteAct(sessionId, placeId, 'V', $(this));
        } else {
            insertAct(sessionId, placeId, 'V', $(this));
        }
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