<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>같은반 선택 목록</title>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="stylesheet" href="/static/css/groupFavorites.css"/>
</head>
<body>
<jsp:include page="layout/header.jsp"/>
<jsp:include page="layout/nav.jsp"/>
<div class="container">
    <div class="item-list"></div>
    <button id="more-list">더보기</button>
</div>
<jsp:include page="layout/footer.jsp"/>
<script>
    let limit = 5;
    let offset = 0;

    addToFavorites();

    $('#more-list').click(function () {
        addToFavorites();
    })

    function addToFavorites() {
        axios.get('api/group/favorites?gubn=G&limit=' + limit + '&offset=' + offset)
            .then(function (response) {
                console.log(response);
                if (response.data.length !== 0  && response.status === 200) {
                    let html = '';
                    response.data.forEach((item) => {
                        console.log(item.photos.get(0))
                        html += '<div class="item-elements">';
                        html += '<div class="item-name">' + item.name + '</div>';
                        html += '<div class="item-address">' + item.formatted_address + '</div>';
                        html += '<div class="item-phone-number">' + item.formatted_phone_number + '</div>';
                        html += item.hasOwnProperty('rating')?'<div class="item-rating">' + item.rating + '/5</div>':'<div class="item-rating">리뷰 없음</div>';
                        html += '<div class="item-address">학원에서 ' + distance(item.geometry.location.lat, item.geometry.location.lng) + 'm 거리</div>';

                        // html += '<img src=\"https://maps.googleapis.com/maps/api/place/photo?photoreference="';
                        // html += item.photos[0].photo_reference;
                        // html += '"&key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g&maxheight=400&maxwidth=400\" alt=\"없음\">'

                        html += '</div>';

                        //사진 추가하기
                    })
                    $('.item-list').append(html);
                    offset += limit;
                } else{
                    $('#more-list').hide();
                }
            })
            .catch(function (error) {
                console.log(error);
            })
    }


    function distance(lat1, lon1, lat2 = 35.1596124, lon2 = 129.0601826) {
        const EARTH_RADIUS_M = 6371000; // 지구 반지름 (미터 단위)

        // 위도와 경도를 라디안으로 변환하는 함수
        const toRadians = (degrees) => degrees * (Math.PI / 180);

        // 입력값 검증
        if (
            typeof lat1 !== "number" || isNaN(lat1) ||
            typeof lon1 !== "number" || isNaN(lon1) ||
            typeof lat2 !== "number" || isNaN(lat2) ||
            typeof lon2 !== "number" || isNaN(lon2)
        ) {
            throw new Error("위도와 경도 값은 숫자여야 합니다.");
        }

        // 위도와 경도를 라디안으로 변환
        const lat1Rad = toRadians(lat1);
        const lat2Rad = toRadians(lat2);
        const deltaLat = toRadians(lat2 - lat1);
        const deltaLon = toRadians(lon2 - lon1);

        // 하버사인 공식 적용
        const a =
            Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
            Math.cos(lat1Rad) * Math.cos(lat2Rad) *
            Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 거리 계산 (미터 단위로 반환) + 소수점 제거
        return Math.floor(EARTH_RADIUS_M * c);
    }
</script>
</body>
</html>
