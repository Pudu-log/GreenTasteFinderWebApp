<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Add a Map with Markers using HTML</title>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>

<div id="googleMap" style="width: 100%;height: 700px;"></div>

<div class="container">

    <ul class="tabs">
        <li class="tab-link current" data-tab="tab-1">Tab One</li>
        <li class="tab-link" data-tab="tab-2">Tab Two</li>
        <li class="tab-link" data-tab="tab-3">Tab Three</li>
        <li class="tab-link" data-tab="tab-4">Tab Four</li>
    </ul>

    <div id="tab-1" class="tab-content current">

    </div>
    <div id="tab-2" class="tab-content">

    </div>
    <div id="tab-3" class="tab-content">

    </div>
    <div id="tab-4" class="tab-content">

    </div>

</div>

<div id="name">${response.result}</div>

<script>
    let map;

    // 지도 초기화 함수
    async function initMap() {
        let inputData = ${response}
        console.log(inputData);

        if (!inputData) {
            console.error("입력 데이터가 없습니다.");
            return;
        }

        // 위치 선언: inputData에서 경도와 위도 가져오기
        const position = {
            lat: inputData.result.geometry.location.lat,
            lng: inputData.result.geometry.location.lng
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
            title: inputData.result.name, // 장소 이름
        });
    }
</script>

<!-- 구글맵 API 호출 -->
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g&callback=initMap" async defer></script>

</body>
</html>