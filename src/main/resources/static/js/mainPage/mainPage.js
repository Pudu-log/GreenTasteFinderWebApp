// 페이지 로드 시 데이터 가져오기
window.onload = function() {
  fetchAllRestaurants();
};
// 좋아요 토글
function toggleLike(button) {
	button.classList.toggle('liked');
}
// 즐겨찾기 토글
function toggleFavorite(button) {
	button.classList.toggle('favorited');
}
async function fetchAllRestaurants() {
  try {
    // 비동기로 서버에서 음식점 리스트 가져오기
    const response = await fetch('/api/restaurants/all'); // keyword를 필요에 따라 설정
    if (!response.ok) {
      throw new Error('서버 요청 실패: ' + response.statusText);
    }

    // JSON 데이터를 파싱
    const restaurants = await response.json();

    // 음식점 리스트를 콘솔에 출력
    console.log("서버에서 받아온 모든 음식점 리스트:");
    restaurants.forEach((restaurant, index) => {
      console.log(`#${index + 1}:`, restaurant);
    });
  } catch (error) {
    console.error('음식점 데이터를 가져오는 중 오류 발생:', error);
  }
}

