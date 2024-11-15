<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Add a Map with Markers using HTML</title>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
<div id="googleMap" style="width: 100%;height: 700px;"></div>

<script>
    let map;
    let inputData = null;

    // 주소 검색 함수 호출 (페이지 로드 후 실행)
    searchAddressHandler();

    // searchAddressHandler 함수 호출 후 initMap을 호출
    async function searchAddressHandler() {
        try {
            // 주소 검색 API 호출
            const response = await axios.get(`/getPlaceDetails?placeId=ChIJ70lL5f4iZDURou4DxhPonPA`);
            console.log(response.data);
            inputData = response.data;  // inputData에 값 할당
            initMap(inputData);  // searchAddressHandler 함수 안에서 initMap 호출
        } catch (error) {
            console.log(error);
        }
    }

    // 지도 초기화 함수
    async function initMap(inputData) {
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
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g" async defer></script>

</body>
</html>