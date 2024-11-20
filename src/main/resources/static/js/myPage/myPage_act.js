
function reviewMake(rate, totalReviewCnt) {
    return rate == 0 ? '리뷰없음' : '⭐ ' + rate + ' / 5 (' + totalReviewCnt + ' 리뷰)';
}

function extractKoreanAddress(address) {
    return address.replace(/[^\uAC00-\uD7AF\s\d-]/g, "");
}

function getPhoto(photos) {
    let returnHtml = ''
    const maxPhoto = 8; // 몇 개 보여줄건지
    for (let i = 0; i < photos?.length; i++) {
        if (i === maxPhoto) break;
        let src = "https://maps.googleapis.com/maps/api/place/photo?photoreference=" + photos[i].photo_reference + "&key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g&maxheight=200&maxwidth=200";
        returnHtml += '<img src="' + src + '">';
    }
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

function setTitle(gubn) {
    switch (gubn) {
        case 'F' :
            document.title = '즐겨찾기 목록'
            break;
        case 'V' :
            document.title = '방문기록 목록'
            break;
        case 'G' :
            document.title = '좋아요 목록'
            break;
    }
}