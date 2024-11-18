/**
 * 선택된 탭 표시 및 히스토리에 상태 저장.
 * @param {string} tabName - 활성화할 탭의 ID
 */
function showTab(tabName) {
    const tabs = document.querySelectorAll('.tab-content');
    const buttons = document.querySelectorAll('.tab-menu button');

    // 모든 탭 숨기기 및 버튼 비활성화
    tabs.forEach(tab => tab.style.display = 'none');
    buttons.forEach(button => button.classList.remove('active'));

    // 선택된 탭과 버튼 활성화
    const targetTab = document.getElementById(tabName);
    const targetButton = document.querySelector(`button[data-tab="${tabName}"]`);

    if (targetTab && targetButton) {
        targetTab.style.display = 'block';
        targetButton.classList.add('active');

        // 히스토리 상태 저장
        pushHistoryState({ tab: tabName });
    }
}

/**
 * 페이지 로드 시 초기화 및 이벤트 설정
 */
document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('searchInput');

    searchInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            searchUsers();
        }
    });

    // URL 매개변수 처리
    const params = new URLSearchParams(window.location.search);
    const keyword = params.get('keyword') || '';
    const page = parseInt(params.get('page'), 10) || 1;

    // 초기화
    searchInput.value = keyword;
    getMemberList(page, keyword);
    getRoomList();
});

/**
 * 히스토리 상태 저장
 * @param {Object} state - 저장할 상태 (탭, 페이지, 검색어 등)
 */
function pushHistoryState(state) {
    const queryParams = new URLSearchParams(state).toString();
    window.history.pushState(state, '', `?${queryParams}`);
}
