<%@ page import="com.fasterxml.jackson.databind.util.JSONPObject" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>상세 페이지</title>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="stylesheet" href="/static/css/detail.css"/>
    <script src="/static/js/detailTab.js"></script>
</head>
<body>
<jsp:include page="layout/header.jsp" />
<jsp:include page="layout/nav.jsp" />

<h2 id="title">제목</h2>
<div id="sub-title">
    <ul>
        <li id="open_now">open_now</li>
        <li id="rating">rating</li>
        <li id="reviewLength">review.length</li>
    </ul>

</div>

<div id="googleMap" style="width: 70%;height: 700px;"></div>

<div class="container">
    <ul class="tabs">
        <li class="tab-link current" data-tab="tab-1">정보</li>
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


<script>
    let map;
    let inputData = ${response}.result;
    console.log(inputData);

    //제목 텍스트 수정
    $('#title').text(inputData.name);
    $('#open_now').text((inputData.opening_hours.open_now)?"운영중":"운영 중지");
    $('#rating').text("평점:" + inputData.rating);
    $('#reviewLength').text("리뷰:" + inputData.reviews.length);


    let weekDays = inputData.opening_hours.weekday_text;
    $('#tab-1').text("")
    weekDays.forEach((day) => {
        $('#tab-1').append(day + '<br>');
    });

    $('#tab-2').text("")
    inputData.photos.forEach((photo) => {
        $('#tab-2').append("<img src=\"https://maps.googleapis.com/maps/api/place/photo?photoreference=" + photo + "&key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g&maxheight=400&maxwidth=400\" alt=\"없음\">");
    });


    // 지도 초기화 함수
    async function initMap() {

        if (!inputData) {
            console.error("입력 데이터가 없습니다.");
            return;
        }

        // 위치 선언: inputData에서 경도와 위도 가져오기
        const position = {
            lat: inputData.geometry.location.lat,
            lng: inputData.geometry.location.lng
        };

        // Request needed libraries.
        //@ts-ignore
        const {Map} = await google.maps.importLibrary("maps");
        const {AdvancedMarkerElement} = await google.maps.importLibrary("marker");

        // 지도 생성
        map = new Map(document.getElementById("googleMap"), {
            zoom: 14, // 지도 확대
            center: position, // 검색된 위치로 지도 중심 설정
            mapId: "MAP_ID", // 지도 스타일 ID
        });

        // 마커 생성
        const marker = new AdvancedMarkerElement({
            map: map,
            position: position,
            title: inputData.name, // 장소 이름
        });
    }
</script>

<!-- 구글맵 API 호출 -->
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g&callback=initMap" async defer></script>

<jsp:include page="layout/footer.jsp" />
</body>
</html>