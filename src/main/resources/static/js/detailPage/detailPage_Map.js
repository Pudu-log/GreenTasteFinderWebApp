let map;
console.log(inputData);

//제목 텍스트 수정
$('#title').text(inputData.name);
if("open_hours" in inputData){
    $('#open_now').text((inputData.opening_hours.open_now)?"운영중":"운영 중지");
}

$('#rating').text("평점 " + inputData.rating);
$('#reviewLength').text("리뷰 " + inputData.reviews.length);

if("open_hours" in inputData) {
    let weekDays = inputData.opening_hours.weekday_text;
    weekDays.forEach((day) => {
        $('#tab-1').append(day + '<br>');
    });
}

inputData.photos.forEach((photo) => {
    $('#tab-2').append("<img src=\"https://maps.googleapis.com/maps/api/place/photo?photoreference=" + photo.photo_reference + "&key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g&maxheight=400&maxwidth=400\" alt=\"없음\">");
});

let reviewIndex = 0;
inputData.reviews.forEach((review) => {
    $('#tab-3').append("<div class=\"tab-item\" id = \"reviewer"+ reviewIndex +"\"></div>")
    $('#reviewer' + reviewIndex).append("<p>" + review.author_name + "</p>" +
        "<p>" + review.relative_time_description + "</p>" +
        "<p>평점 " + review.rating + "/5</p>" +
        "<p>" + review.text + "</p>");
    reviewIndex++;
})


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