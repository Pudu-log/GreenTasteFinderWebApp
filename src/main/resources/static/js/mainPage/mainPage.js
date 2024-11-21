/*
작성자: 구경림  
작성일: 2024.11.20  
작성이유:  
1. 클라이언트 측에서 음식점 데이터를 관리하고 UI에 반영하기 위한 스크립트.  
2. 좋아요/즐겨찾기 상태를 서버와 동기화하고, 이벤트 핸들링으로 동적 사용자 경험 제공.  

리팩토링 포인트:  
- 전부 다...................
- 좋아요/즐겨찾기 누른 후 새로고침이나 페이지이동, 검색 재정렬 등 수행시 ui에 반영이 초기화되는 문제 있음...
- 영어로 검색해야하는 문제 있음...
*/


window.addEventListener('load', async () => {
	const isFirstLoad = sessionStorage.getItem('isFirstLoad') !== 'false';
	let loadCount = parseInt(sessionStorage.getItem('loadCount'));

	// loadCount가 NaN인 경우 초기값 설정
	if (isNaN(loadCount)) {
		loadCount = 0;
	}

	if (loadCount === 0) {
		// 첫 번째 로드 - 모든 데이터를 서버에서 가져옵니다.
		console.log('첫 로드입니다. 모든 음식점 데이터를 서버에서 가져옵니다.');
		await fetchAllRestaurants();
	} else {
		// 첫 로드가 아닌 경우 좋아요/즐겨찾기 동기화 진행
		console.log('첫 로드가 아닙니다. 좋아요/즐겨찾기 동기화 진행.');
		await syncFavoritesAndLikes();
	}

	// 로드 카운트를 증가시키고 세션 스토리지에 저장합니다.
	sessionStorage.setItem('loadCount', loadCount + 1);
});

// << 해시 변경 시 이벤트 처리 >>
// SPA(Single Page Application) 방식에서 유용한 이벤트입니다. 페이지의 해시(#) 부분이 변경될 때 호출됩니다.
window.addEventListener('hashchange', async () => {
	console.log('해시 변경 감지, 좋아요/즐겨찾기 동기화 진행');
	await syncFavoritesAndLikes();
});

// << 뒤로가기/앞으로 가기 시 이벤트 처리 >>
// 사용자가 브라우저의 뒤로가기/앞으로 가기를 사용할 때 상태 동기화를 처리합니다.
window.addEventListener('popstate', async () => {
	console.log('페이지 변경 감지 (뒤로 가기/앞으로 가기), 좋아요/즐겨찾기 동기화 진행');
	await syncFavoritesAndLikes();
});

// << 페이지 활성화 시 이벤트 처리 >>
// 다른 탭으로 이동 후 다시 돌아오는 경우 상태를 동기화합니다.
document.addEventListener('visibilitychange', async () => {
	if (document.visibilityState === 'visible') {
		console.log('페이지가 다시 활성화됨, 좋아요/즐겨찾기 동기화 진행');
		await syncFavoritesAndLikes();
	}
});

// 로딩 상태를 관리하는 변수 (로딩 스피너 표시 여부 제어)
let isLoading = false;

// << 좋아요 토글 함수 >>
function toggleLike(button) {
	button.classList.toggle('liked');
}

// << 즐겨찾기 토글 함수 >>
function toggleFavorite(button) {
	button.classList.toggle('favorited');
}

// << 음식점 데이터를 서버에서 가져오는 함수 >>
async function fetchAllRestaurants() {
	try {
		setLoadingState(true); // 로딩 시작

		// 세션 스토리지에서 사용자 ID를 가져옵니다.
		const memberId = sessionStorage.getItem('memberId');

		// 서버에서 음식점 데이터 가져오기
		const response = await fetch(`/api/restaurants/all?memberId=${memberId}`);
		if (!response.ok) {
			throw new Error('서버 요청 실패: ' + response.statusText);
		}

		const restaurants = await response.json();
		console.log('서버에서 가져온 레스토랑 데이터:', restaurants);

		// 세션 스토리지에 새로운 레스토랑 데이터를 저장합니다.
		sessionStorage.setItem('restaurants', JSON.stringify(restaurants));

		// 세션 스토리지에서 좋아요/즐겨찾기 상태를 가져옵니다.
		const likedStores = JSON.parse(sessionStorage.getItem('likedStores')) || [];
		const favoritedStores = JSON.parse(sessionStorage.getItem('favoritedStores')) || [];

		// 음식점 리스트에 세션 스토리지의 좋아요/즐겨찾기 상태 반영
		restaurants.forEach(restaurant => {
			restaurant.liked = likedStores.includes(restaurant.placeId);
			restaurant.favorited = favoritedStores.includes(restaurant.placeId);
		});

		console.log('세션 스토리지 좋아요 목록:', likedStores);
		console.log('세션 스토리지 즐겨찾기 목록:', favoritedStores);

		// 페이지에 음식점 데이터를 표시합니다.
		displayRestaurants(restaurants);

		console.log('첫 페이지 데이터 로딩 완료');
	} catch (error) {
		console.error('데이터 로드 중 오류 발생:', error);
	} finally {
		setLoadingState(false); // 로딩 종료
		// 로딩이 끝난 후 새로고침
		location.reload();
	}
}


// << 좋아요/즐겨찾기 상태 동기화 함수 >>
function syncFavoritesAndLikes() {
	const memberId = sessionStorage.getItem('memberId');
	if (!memberId) {
		return; // 로그인되지 않은 상태일 경우 동기화 불필요
	}

	// 서버에서 사용자의 좋아요 및 즐겨찾기 상태를 가져오기
	fetch(`/api/restaurants/status?memberId=${memberId}`)
		.then(response => {
			if (!response.ok) {
				throw new Error('서버 요청 실패: ' + response.statusText);
			}
			return response.json();
		})
		.then(({ likedStores, favoritedStores }) => {
			// 세션 스토리지 상태 가져오기
			let localLikedStores = JSON.parse(sessionStorage.getItem('likedStores')) || [];
			let localFavoritedStores = JSON.parse(sessionStorage.getItem('favoritedStores')) || [];

			console.log('서버에서 받아온 좋아요 목록:', likedStores);
			console.log('서버에서 받아온 즐겨찾기 목록:', favoritedStores);

			// 서버 상태와 세션 상태를 비교 후 동기화 진행
			let isLikedChanged = JSON.stringify(likedStores) !== JSON.stringify(localLikedStores);
			let isFavoritedChanged = JSON.stringify(favoritedStores) !== JSON.stringify(localFavoritedStores);

			console.log('좋아요 상태 변경됨:', isLikedChanged);
			console.log('즐겨찾기 상태 변경됨:', isFavoritedChanged);

			// 상태 변경이 있을 경우 세션 스토리지 업데이트
			if (isLikedChanged) {
				sessionStorage.setItem('likedStores', JSON.stringify(likedStores));
				console.log('세션 스토리지 좋아요 목록 업데이트됨:', likedStores);
			}
			if (isFavoritedChanged) {
				sessionStorage.setItem('favoritedStores', JSON.stringify(favoritedStores));
				console.log('세션 스토리지 즐겨찾기 목록 업데이트됨:', favoritedStores);
			}

			// UI 업데이트가 필요한 경우 요소 업데이트
			if (isLikedChanged || isFavoritedChanged) {
				updateUIWithNewStates(likedStores, favoritedStores);
			}

			console.log('페이지 이동 시 세션 스토리지와 서버 데이터 동기화 완료');
		})
		.catch(error => {
			console.error('동기화 중 오류 발생:', error);
		});
}

// << 좋아요/즐겨찾기 버튼 클릭 시 서버에 상태 업데이트 요청하는 함수 >>
function toggleAction(storeId, actionType, element) {
	const memberId = sessionStorage.getItem('memberId');
	if (!memberId) {
		console.error("로그인된 사용자만 좋아요 또는 즐겨찾기를 할 수 있습니다.");
		return;
	}

	// 현재 상태가 좋아요 또는 즐겨찾기인지 확인 (클릭한 버튼 기준)
	const isActive = element.classList.contains(actionType === 'G' ? 'liked' : 'favorited');

	// 서버에 상태 업데이트 요청
	fetch('/api/restaurants/toggle', {
		method: 'POST',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		body: new URLSearchParams({ memberId, storeId, gubn: actionType }),
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('서버 요청 실패: ' + response.statusText);
			}

			// 서버 업데이트 성공 후 새로운 상태 설정
			const newState = !isActive;

			// 세션 스토리지 업데이트
			updateLocalStorageWithAction(storeId, actionType, newState);

			// 클릭한 버튼에 대한 UI 업데이트
			if (actionType === 'G') {
				element.classList.toggle('liked', newState);
				element.querySelector('span').textContent = newState ? "좋아요 취소" : "좋아요";
			} else if (actionType === 'F') {
				element.classList.toggle('favorited', newState);
				element.querySelector('span').textContent = newState ? "즐겨찾기 취소" : "즐겨찾기";
			}

			console.log(`UI 업데이트 완료 - storeId: ${storeId}, liked 상태: ${actionType === 'G' ? newState : element.classList.contains('liked')}, favorited 상태: ${actionType === 'F' ? newState : element.classList.contains('favorited')}`);
		})
		.catch(error => {
			console.error('상태 업데이트 중 오류 발생:', error);
		});
}

// << 세션 스토리지에서 좋아요/즐겨찾기 상태 업데이트 함수 >>
/*
 * 좋아요/즐겨찾기 상태가 변경될 때 세션 스토리지에서 해당 데이터를 업데이트합니다.
 */
function updateLocalStorageWithAction(storeId, actionType, isActive) {
	if (actionType === 'G') {
		// 좋아요 상태 업데이트
		let likedStores = JSON.parse(sessionStorage.getItem('likedStores')) || [];
		if (isActive) {
			if (!likedStores.includes(storeId)) {
				likedStores.push(storeId);
			}
		} else {
			likedStores = likedStores.filter(id => id !== storeId);
		}
		sessionStorage.setItem('likedStores', JSON.stringify(likedStores));
	} else if (actionType === 'F') {
		// 즐겨찾기 상태 업데이트
		let favoritedStores = JSON.parse(sessionStorage.getItem('favoritedStores')) || [];
		if (isActive) {
			if (!favoritedStores.includes(storeId)) {
				favoritedStores.push(storeId);
			}
		} else {
			favoritedStores = favoritedStores.filter(id => id !== storeId);
		}
		sessionStorage.setItem('favoritedStores', JSON.stringify(favoritedStores));
	}
}

// << 좋아요/즐겨찾기 상태에 따라 UI 업데이트 >>
/*
 * 세션 스토리지에 저장된 데이터를 기준으로 UI 요소들을 업데이트합니다.
 */
function updateUIWithNewStates(likedStores, favoritedStores) {
	document.querySelectorAll('.restaurant').forEach(restaurantElement => {
		const storeId = restaurantElement.dataset.storeId;

		console.log('UI 업데이트 - storeId:', storeId);
		console.log('UI 업데이트 	- liked 상태:', likedStores.includes(storeId));
		console.log('UI 업데이트 - favorited 상태:', favoritedStores.includes(storeId));

		// 좋아요 상태 업데이트
		const likeButton = restaurantElement.querySelector('.action-btn[data-type="like"]');
		if (likeButton) {
			const isLiked = likedStores.includes(storeId);
			likeButton.classList.toggle('liked', isLiked);
			likeButton.querySelector('span').textContent = isLiked ? '좋아요 취소' : '좋아요';
		}

		// 즐겨찾기 상태 업데이트
		const favoriteButton = restaurantElement.querySelector('.action-btn[data-type="favorite"]');
		if (favoriteButton) {
			const isFavorited = favoritedStores.includes(storeId);
			favoriteButton.classList.toggle('favorited', isFavorited);
			favoriteButton.querySelector('span').textContent = isFavorited ? '즐겨찾기 취소' : '즐겨찾기';
		}
	});
}

// << 로딩 상태 설정 함수 >>
function setLoadingState(state) {
	isLoading = state; // 로딩 상태 업데이트
	const spinner = document.getElementById('loadingSpinner');
	spinner.style.display = isLoading ? 'flex' : 'none'; // 로딩 스피너 표시 여부 제어
}

// 정렬 옵션 변경 시 호출되는 함수
function onSortChange() {
	const sortBy = document.getElementById("sortSelect").value;
	const keyword = document.getElementById("searchKeyword").value || 'restaurant';

	// loadCount를 1로 설정하여 이후에는 동기화 진행
	sessionStorage.setItem('loadCount', 1);

	// 현재 페이지 URL을 변경하여 새로고침
	window.location.href = `/?sortBy=${sortBy}&keyword=${encodeURIComponent(keyword)}`;
}


