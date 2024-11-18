/**
 * 선택된 탭 표시
 * @param {string} tabName - 활성화할 탭의 ID
 */
function showTab(tabName) {
    const tabs = document.querySelectorAll('.tab-content');
    const buttons = document.querySelectorAll('.tab-menu button');

    // 모든 탭 숨기기 및 버튼 비활성화
    tabs.forEach(tab => (tab.style.display = 'none'));
    buttons.forEach(button => button.classList.remove('active'));

    // 선택된 탭과 버튼 활성화
    const targetTab = document.getElementById(tabName);
    const targetButton = document.querySelector(`button[data-tab="${tabName}"]`);

    if (targetTab && targetButton) {
        targetTab.style.display = 'block';
        targetButton.classList.add('active');
    }
}

/**
 * 페이지 로드 시 초기화
 * 기본적으로 'user-management' 탭을 활성화하고, 사용자 및 강의실 데이터를 로드합니다.
 */
document.addEventListener('DOMContentLoaded', function () {
    showTab('user-management');
    getMemberList();
    getRoomList();
});
