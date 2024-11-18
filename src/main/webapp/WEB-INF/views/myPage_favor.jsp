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
    <style>
        .restaurant-list {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .restaurant-item {
            display: flex;
            align-items: center;
            background: #fff;
            border-radius: 10px;
            padding: 15px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .restaurant-icon {
            width: 60px;
            height: 60px;
            margin-right: 15px;
            border-radius: 8px;
        }

        .restaurant-details {
            flex: 1;
        }

        .restaurant-name {
            margin: 0 0 5px;
            font-size: 1.2em;
            color: #FF6347;
        }

        .restaurant-address,
        .restaurant-phone,
        .restaurant-rating,
        .restaurant-distance {
            margin: 5px 0;
            font-size: 0.9em;
            color: #555;
        }

        .map-link {
            display: inline-block;
            margin-top: 10px;
            padding: 5px 10px;
            font-size: 0.9em;
            text-decoration: none;
            color: #fff;
            background-color: #FF6347;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        }

        .map-link:hover {
            background-color: #FF4500;
        }

        .restaurant-images {
            display: flex;
            justify-content: space-around;
            width: 80%;
        }
    </style>
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
        let memberId = "${sessionScope.member.id}";
        let gubn = "${requestScope.gubn}";
        console.log(memberId);
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
                    addHtml += '<a href="' + res.url + '" target="_blank" class="map-link">지도에서 보기</a>'
                    addHtml += '</div>';

                    addHtml += '<div class="restaurant-images">';
                    addHtml += getPhoto(res.photos);
                    addHtml += '</div>';

                    addHtml += '</div>';
                })
                console.log(addHtml);
                $('.restaurant-list').html(addHtml);
            })
            .catch(error => {
                console.error(error);
            });
    })

    function reviewMake(rate, totalReviewCnt) {
        return rate == 0 ? '리뷰없음' : '⭐ ' + rate + ' / 5 (' + totalReviewCnt + ' 리뷰)';
    }

    function extractKoreanAddress(address) {
        return address.replace(/[^\uAC00-\uD7AF\s\d-]/g, "");
    }

    function getPhoto(photos) {
        let returnHtml = ''
        const maxPhoto = 8; // 몇 개 보여줄건지
        for (let i = 0; i < photos.length; i++) {
            if (i === maxPhoto) break;
            let src = "https://maps.googleapis.com/maps/api/place/photo?photoreference=" + photos[i].photo_reference + "&key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g&maxheight=200&maxwidth=200";
            returnHtml += '<img src="' + src + '">';
        }
        // photos.forEach(photo => {
        //     let src = "https://maps.googleapis.com/maps/api/place/photo?photoreference=" + photo.photo_reference + "&key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g&maxheight=200&maxwidth=200";
        //     returnHtml += '<img src="' + src + '">';
        // })
        return returnHtml;
    }

    // let lat2 = 35.1596124;
    // let lng2 = 129.0601826;

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
</html>