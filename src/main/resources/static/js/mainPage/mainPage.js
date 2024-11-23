/*
작성자: 구경림  
작성일: 2024.11.23
작성이유: 캐시를 활용한 데이터 관리 및 효율적 페이지 이동
*/
const currentPath = window.location.search;
console.log("현재 경로 >> " +currentPath);
console.log('테스트233');

let isLoading = false;

window.addEventListener('load', async () => {
	const currentPath = window.location.search;

    if (currentPath === '') {
        console.log('홈 페이지 로드: 초기 데이터 로딩');
        await fetchAllRestaurants(true); 
    } else {
        console.log('캐시된 데이터로 페이지 렌더링');
        syncFavoritesAndLikes();	
    }
	const params = new URLSearchParams(window.location.search);
	const keyword = params.get('keyword') || sessionStorage.getItem('activeTag') || '';

	// 키워드가 없으면 기본적으로 #전체 태그를 활성화
	const tagButton = document.querySelector(`.tag[data-keyword="${keyword}"]`) || document.querySelector(`.tag[data-keyword=""]`);
	if (tagButton) {
	    tagButton.classList.add("active");
	}
});


window.addEventListener('hashchange', async () => {
	console.log('해시 변경 감지, 좋아요/즐겨찾기 동기화 진행');
	await syncFavoritesAndLikes();
});

window.addEventListener('popstate', async () => {
	console.log('페이지 변경 감지 (뒤로 가기/앞으로 가기), 좋아요/즐겨찾기 동기화 진행');
	await syncFavoritesAndLikes();
});

document.addEventListener('visibilitychange', async () => {
	if (document.visibilityState === 'visible') {
		console.log('페이지가 다시 활성화됨, 좋아요/즐겨찾기 동기화 진행');
		await syncFavoritesAndLikes();
	}
});


/**
 * 로딩 상태를 설정하고 로딩 스피너를 표시합니다.
 * - 비동기 작업 전후에 호출됩니다.
 * 
 * 주요 기능:
 * 1. 로딩 상태를 나타내는 `isLoading` 변수를 업데이트합니다.
 * 2. 로딩 스피너의 표시 여부를 DOM에서 제어합니다.
 * 
 * @param {boolean} state - 로딩 상태 (true: 로딩 중, false: 로딩 완료).
 */

function setLoadingState(state) {
    isLoading = state;
    const spinner = document.getElementById('loadingSpinner');
    spinner.style.display = isLoading ? 'flex' : 'none';
}

/**
 * 서버에서 모든 음식점 데이터를 가져옵니다.
 * - 초기 페이지 로드 시 호출되며, 세션 스토리지에 데이터를 저장하고 UI를 업데이트합니다.
 * - 데이터 로드 중 로딩 스피너를 표시합니다.
 * 
 * 주요 기능:
 * 1. 로그인된 사용자의 ID를 세션 스토리지에서 가져옵니다.
 * 2. 서버로부터 음식점 데이터를 가져와 세션 스토리지에 캐싱합니다.
 * 3. 데이터 로드가 완료되면 메인 페이지로 이동합니다.
 * 
 * @async
 * @throws {Error} 서버 요청 실패 시 오류를 throw합니다.
 */
async function fetchAllRestaurants() {
    try {
        setLoadingState(true);

        const memberId = sessionStorage.getItem('memberId');
        const response = await fetch(`/api/restaurants/all?memberId=${memberId}`);
        if (!response.ok) {
            throw new Error('서버 요청 실패: ' + response.statusText);
        }

        const restaurants = await response.json();
        console.log('서버에서 가져온 레스토랑 데이터:', restaurants);

        sessionStorage.setItem('restaurants', JSON.stringify(restaurants));

		 window.location.href = "/?page=1&sortBy=reviewCount&keyword=restaurant";
    } catch (error) {
        console.error('데이터 로드 중 오류 발생:', error);
    } finally {
        setLoadingState(false);
    }
}



/**
 * 서버와 세션 스토리지 간 좋아요/즐겨찾기 상태를 동기화합니다.
 * - 로그인된 사용자만 동기화가 진행됩니다.
 * - 서버 데이터를 기준으로 세션 스토리지 데이터를 업데이트하고, UI를 최신 상태로 유지합니다.
 * 
 * 주요 기능:
 * 1. 서버에서 좋아요 및 즐겨찾기 상태 데이터를 가져옵니다.
 * 2. 서버 상태와 세션 스토리지 상태를 비교 후 변경된 항목만 업데이트합니다.
 * 3. UI 요소들을 동기화된 상태에 맞게 업데이트합니다.
 * 
 * @async
 * @throws {Error} 서버 요청 실패 시 오류를 throw합니다.
 */

async function syncFavoritesAndLikes() {
    const memberId = sessionStorage.getItem('memberId');
    if (!memberId) return;

    try {
        const response = await fetch(`/api/restaurants/status?memberId=${memberId}`);
        if (!response.ok) {
            throw new Error('서버 요청 실패: ' + response.statusText);
        }

        const { likedStores, favoritedStores } = await response.json();
        const localLikedStores = JSON.parse(sessionStorage.getItem('likedStores')) || [];
        const localFavoritedStores = JSON.parse(sessionStorage.getItem('favoritedStores')) || [];

        if (JSON.stringify(likedStores) !== JSON.stringify(localLikedStores)) {
            sessionStorage.setItem('likedStores', JSON.stringify(likedStores));
        }
        if (JSON.stringify(favoritedStores) !== JSON.stringify(localFavoritedStores)) {
            sessionStorage.setItem('favoritedStores', JSON.stringify(favoritedStores));
        }

        updateUIWithNewStates(likedStores, favoritedStores);
        console.log('좋아요/즐겨찾기 상태 동기화 완료');
    } catch (error) {
        console.error('동기화 중 오류 발생:', error);
    }
}

/**
 * 좋아요/즐겨찾기 상태를 기준으로 UI를 업데이트합니다.
 * - 세션 스토리지 또는 서버 데이터를 기반으로 호출됩니다.
 * - 각 음식점 요소의 좋아요 및 즐겨찾기 버튼 상태를 동기화합니다.
 * 
 * 주요 기능:
 * 1. 각 음식점의 ID를 기반으로 버튼 상태를 업데이트합니다.
 * 2. 좋아요 버튼 및 즐겨찾기 버튼의 텍스트와 클래스 상태를 동기화합니다.
 * 
 * @param {Array<string>} likedStores - 좋아요가 활성화된 음식점의 ID 배열.
 * @param {Array<string>} favoritedStores - 즐겨찾기가 활성화된 음식점의 ID 배열.
 */

function updateUIWithNewStates(likedStores, favoritedStores) {
	document.querySelectorAll('.restaurant').forEach(restaurantElement => {
		const storeId = restaurantElement.dataset.storeId;

		console.log('UI 업데이트 - storeId:', storeId);
		console.log('UI 업데이트 	- liked 상태:', likedStores.includes(storeId));
		console.log('UI 업데이트 - favorited 상태:', favoritedStores.includes(storeId));
		const likeButton = restaurantElement.querySelector('.action-btn[data-type="like"]');
		
		if (likeButton) {
			const isLiked = likedStores.includes(storeId);
			likeButton.classList.toggle('liked', isLiked);
			likeButton.querySelector('span').textContent = isLiked ? '좋아요 취소' : '좋아요';
		}

		const favoriteButton = restaurantElement.querySelector('.action-btn[data-type="favorite"]');
		if (favoriteButton) {
			const isFavorited = favoritedStores.includes(storeId);
			favoriteButton.classList.toggle('favorited', isFavorited);
			favoriteButton.querySelector('span').textContent = isFavorited ? '즐겨찾기 취소' : '즐겨찾기';
		}
	});
}

/**
 * 좋아요 또는 즐겨찾기 버튼 클릭 시 호출되는 함수.
 * - 서버에 상태 업데이트 요청을 보내고, UI 및 세션 스토리지 상태를 업데이트합니다.
 * 
 * 주요 기능:
 * 1. 로그인된 사용자만 액션을 수행하도록 제한합니다.
 * 2. 현재 버튼 상태를 확인하고 서버에 업데이트 요청을 보냅니다.
 * 3. 서버 요청 성공 시, 새로운 상태를 세션 스토리지 및 UI에 반영합니다.
 * 4. 실패 시 오류를 콘솔에 출력합니다.
 * 
 * @param {string} storeId - 변경 대상 음식점의 ID.
 * @param {string} actionType - 액션 유형 ('G' for 좋아요, 'F' for 즐겨찾기).
 * @param {HTMLElement} element - 클릭된 버튼 요소.
 */

function toggleAction(storeId, actionType, element) {
	const memberId = sessionStorage.getItem('memberId');
	if (!memberId) {
		alert("로그인된 사용자만 좋아요 또는 즐겨찾기를 할 수 있습니다.");
		window.location.href = `/login`;
		return;
	}

	const isActive = element.classList.contains(actionType === 'G' ? 'liked' : 'favorited');

	fetch('/api/restaurants/toggle', {
		method: 'POST',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		body: new URLSearchParams({ memberId, storeId, gubn: actionType }),
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('서버 요청 실패: ' + response.statusText);
			}

			const newState = !isActive;

			updateLocalStorageWithAction(storeId, actionType, newState);

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


/**
 * 세션 스토리지에서 좋아요/즐겨찾기 상태를 업데이트합니다.
 * - 버튼 클릭 시 호출되며, 새로운 상태를 세션 스토리지에 반영합니다.
 * 
 * 주요 기능:
 * 1. 좋아요(G) 또는 즐겨찾기(F)의 유형에 따라 상태를 업데이트합니다.
 * 2. 상태 변경에 따라 음식점 ID를 추가하거나 제거합니다.
 * 3. 변경된 데이터를 세션 스토리지에 저장합니다.
 * 
 * @param {string} storeId - 변경된 상태를 가진 음식점의 ID.
 * @param {string} actionType - 액션 유형 ('G' for 좋아요, 'F' for 즐겨찾기).
 * @param {boolean} isActive - 변경 후의 활성화 상태 (true: 활성화, false: 비활성화).
 */

function updateLocalStorageWithAction(storeId, actionType, isActive) {
    const key = actionType === 'G' ? 'likedStores' : 'favoritedStores';
    let stores = JSON.parse(sessionStorage.getItem(key)) || [];

    if (isActive) {
        if (!stores.includes(storeId)) stores.push(storeId);
    } else {
        stores = stores.filter(id => id !== storeId);
    }

    sessionStorage.setItem(key, JSON.stringify(stores));
}

/**
 * 정렬 옵션 변경 시 호출되는 함수.
 * - 정렬 옵션을 URL 파라미터에 반영하고 페이지를 새로고침합니다.
 * 
 * 주요 기능:
 * 1. 선택된 정렬 기준을 가져옵니다.
 * 2. 활성화된 태그의 키워드를 기반으로 검색 쿼리를 생성합니다.
 * 3. 새로운 정렬 상태와 검색어를 URL에 반영하여 페이지를 새로고침합니다.
 */

function onSortChange() {
    setLoadingState(true);

    const sortBy = document.getElementById("sortSelect").value;
	const activeTag = document.querySelector(".tag.active"); // 'active' 클래스가 선택된 태그를 나타냄
    const keyword = activeTag ? activeTag.getAttribute("data-keyword") : 'restaurant';
	// 페이지를 1로 고정하여 URL 변경
    window.location.href = `/?page=1&sortBy=${sortBy}`;
}

/**
 * 태그 버튼 클릭 시 호출되는 함수.
 * - 선택된 태그를 활성화 상태로 설정하고 검색 결과를 업데이트합니다.
 * 
 * 주요 기능:
 * 1. 모든 태그 버튼의 활성화 상태를 초기화합니다.
 * 2. 클릭된 태그 버튼을 활성화 상태로 변경합니다.
 * 3. 선택된 태그의 키워드를 세션 스토리지에 저장합니다.
 * 4. 정렬 및 검색 로직과 연동하여 결과를 업데이트합니다.
 * 
 * @param {HTMLElement} buttonElement - 클릭된 태그 버튼 요소.
 */

function onTagClick(buttonElement) {
    const tags = document.querySelectorAll(".tag");
    tags.forEach(tag => tag.classList.remove("active"));

    buttonElement.classList.add("active");

    const keyword = buttonElement.dataset.keyword;
    sessionStorage.setItem('activeTag', keyword);

    onSearchAndSort();
}


/**
 * 검색 및 정렬을 처리하는 함수.
 * - 선택된 정렬 기준과 키워드를 기반으로 데이터를 서버에서 가져옵니다.
 * - 검색 결과를 세션 스토리지에 저장하고 UI를 업데이트합니다.
 * 
 * 주요 기능:
 * 1. 선택된 정렬 기준 및 태그 키워드를 가져옵니다.
 * 2. 서버로 검색 요청을 보내고 데이터를 가져옵니다.
 * 3. 가져온 데이터를 세션 스토리지에 저장하고 UI를 업데이트합니다.
 * 
 * @async
 * @throws {Error} 서버 요청 실패 시 오류를 throw합니다.
 */

async function onSearchAndSort() {

    const sortBy = document.getElementById("sortSelect").value;
	const activeTag = document.querySelector(".tag.active"); // 'active' 클래스가 선택된 태그를 나타냄
	const keyword = activeTag ? activeTag.getAttribute("data-keyword") : 'restaurant';

	try {
	    setLoadingState(true);
	    const memberId = sessionStorage.getItem('memberId');
	    const response = await fetch(`/api/restaurants/all?memberId=${memberId}&keyword=${keyword}`);
	    if (!response.ok) {
	        throw new Error('서버 요청 실패: ' + response.statusText);
	    }

	    const restaurants = await response.json();
	    console.log('서버에서 가져온 레스토랑 데이터:', restaurants);
	    sessionStorage.setItem('restaurants', JSON.stringify(restaurants));
		 window.location.href = `/?page=1`;
		 
	} catch (error) {
	    console.error('데이터 로드 중 오류 발생:', error);
	} finally {
	    setLoadingState(false);
	}
}

/*
async function onSearchAndSort() {
    const sortBy = document.getElementById("sortSelect").value;
    const keyword = document.getElementById("searchKeyword").value || 'restaurant';

    try {
        setLoadingState(true);

        const memberId = sessionStorage.getItem('memberId');
        const response = await fetch(`/api/restaurants/all?sortBy=${sortBy}&keyword=${encodeURIComponent(keyword)}`);
        if (!response.ok) {
            throw new Error('검색 요청 실패: ' + response.statusText);
        }

        const restaurants = await response.json();
        console.log('검색 결과:', restaurants);

        sessionStorage.setItem('restaurants', JSON.stringify(restaurants));
        syncFavoritesAndLikes();
    } catch (error) {
        console.error('검색 및 정렬 중 오류 발생:', error);
    } finally {
        setLoadingState(false);
    }
}
* */