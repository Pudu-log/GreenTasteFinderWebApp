document.addEventListener("DOMContentLoaded", function() {
    // 모든 탭 요소를 가져옴
    const tabs = document.querySelectorAll('ul.tabs li');

    tabs.forEach(function(tab) {
        tab.addEventListener('click', function() {
            // 클릭된 탭의 data-tab 값을 가져옴
            var tabId = tab.getAttribute('data-tab');

            // 모든 탭과 컨텐츠에서 current 클래스를 제거
            document.querySelectorAll('ul.tabs li').forEach(function(item) {
                item.classList.remove('current');
            });

            document.querySelectorAll('.tab-content').forEach(function(content) {
                content.classList.remove('current');
            });

            // 클릭된 탭에 current 클래스를 추가
            tab.classList.add('current');

            // 해당하는 tab-content에 current 클래스를 추가
            var tabContent = document.getElementById(tabId);
            if (tabContent) {
                tabContent.classList.add('current');
            }
        });
    });
});
