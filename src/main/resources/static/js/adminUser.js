/**
 * 사용자 목록을 서버에서 가져와 테이블을 업데이트하고 히스토리 상태를 저장합니다.
 * @param {number} page - 현재 페이지 번호
 * @param {string} [keyword=''] - 검색어
 */
function getMemberList(page = 1, keyword = '') {
    axios.get(`/api/admin/member-list?page=${page}&keyword=${encodeURIComponent(keyword)}`)
        .then(response => {
            const {members, pagingBtn} = response.data.data;

            if (!members) {
                console.error('회원 데이터가 없습니다.');
                return;
            }

            updateTable('#user-management tbody', members, createMemberRow);
            updatePagingButtons(pagingBtn, (newPage) => {
                getMemberList(newPage, keyword);
                pushHistoryState({tab: 'user-management', page: newPage, keyword});
            });
        })
        .catch(error => console.error(`사용자 목록 오류: ${error}`));
}

/**
 * 사용자 데이터를 기반으로 테이블을 업데이트합니다.
 * @param {string} tableBodySelector - 테이블의 tbody를 선택할 CSS 셀렉터
 * @param {Array} data - 사용자 데이터 배열
 * @param {Function} rowGenerator - 데이터 항목을 기반으로 행을 생성하는 함수
 */
function updateTable(tableBodySelector, data, rowGenerator) {
    const tbody = document.querySelector(tableBodySelector);
    tbody.innerHTML = '';
    data.forEach(item => tbody.appendChild(rowGenerator(item)));
}

/**
 * 단일 사용자 데이터를 기반으로 테이블 행을 생성합니다.
 * @param {Object} member - 사용자 데이터
 */
function createMemberRow(member) {
    const row = document.createElement('tr');

    row.innerHTML = `
        <td>${member.id}</td>
        <td>${member.name}</td>
        <td>${member.roomCode || '-'}</td>
        <td>${member.email}</td>
        <td>${formatDate(member.birth)}</td>
        <td>${formatDate(member.updateDt || member.insertDt)}</td>
        <td>${member.adminYn === 'Y' ? '관리자' : '사용자'}</td>
        <td><button onclick="deleteMember('${member.id}')">삭제</button></td>
    `;

    return row;
}

/**
 *  회원 삭제
 */
function deleteMember(id) {
    if (!confirm('이 사용자를 정말 삭제하시겠습니까?')) return;

    const page = new URLSearchParams(window.location.search).get('page') || 1;
    const keyword = document.getElementById('searchInput').value.trim();

    axios.delete(`/api/admin/delete/${id}`)
        .then(response => {
            if (response.data.data === '삭제 성공') {
                alert('사용자 삭제 성공');
                getMemberList(page, keyword);
            }else{
                alert('삭제 실패');
            }
        })
        .catch(error => {
            console.error(`삭제 실패: ${error}`);
            alert('삭제 실패.');
        });
}


/**
 * 날짜를 형식에 맞게 변환합니다.
 * @param {string} date - 날짜 문자열
 * @returns {string} 변환된 날짜
 */
function formatDate(date) {
    return date ? new Date(date).toISOString().split('T')[0] : '-';
}

/**
 * 페이징 버튼을 생성하고 업데이트합니다.
 * @param {Object} pagingBtn - 페이징 데이터
 * @param {Function} onPageClick - 페이지 버튼 클릭 시 호출할 함수
 */
function updatePagingButtons(pagingBtn, onPageClick) {
    const pagingContainer = document.querySelector('#paging-buttons');
    pagingContainer.innerHTML = '';

    if (!pagingBtn) return;

    if (pagingBtn.firstPageBtn) {
        pagingContainer.appendChild(createPagingButton('<<', () => onPageClick(1)));
    }

    if (pagingBtn.prevBtn) {
        pagingContainer.appendChild(createPagingButton('<', () => onPageClick(pagingBtn.startPage - 1)));
    }

    for (let i = pagingBtn.startPage; i <= pagingBtn.endPage; i++) {
        pagingContainer.appendChild(createPagingButton(i, () => onPageClick(i), i === pagingBtn.currentPage));
    }

    if (pagingBtn.nextBtn) {
        pagingContainer.appendChild(createPagingButton('>', () => onPageClick(pagingBtn.endPage + 1)));
    }

    if (pagingBtn.lastPageBtn) {
        pagingContainer.appendChild(createPagingButton('>>', () => onPageClick(pagingBtn.totalPage)));
    }
}

/**
 * 페이징 버튼을 생성
 * @param {string|number} text - 버튼에 표시할 텍스트
 * @param {Function} onClick - 버튼 클릭 시 호출할 함수
 * @param {boolean} [isActive=false] - 활성화 여부
 */
function createPagingButton(text, onClick, isActive = false) {
    const button = document.createElement('button');
    button.textContent = text;
    button.onclick = onClick;

    if (isActive) {
        button.classList.add('active');
    }

    return button;
}

/**
 * 뒤로가기/앞으로가기 이벤트 발생 시 상태를 복원
 */
window.addEventListener('popstate', (event) => {
    const state = event.state;
    if (state) {
        const {tab, page, keyword} = state;

        if (tab) showTab(tab);

        if (tab === 'user-management') {
            document.getElementById('searchInput').value = keyword || '';
            getMemberList(page || 1, keyword || '');
        }

        if (tab === 'lecture-management') {
            getRoomList();
        }
    }
});

/**
 * 검색창의 값을 기반으로 사용자 목록을 필터링하고 히스토리에 상태를 저장
 */
function searchUsers() {
    const keyword = document.getElementById('searchInput').value.trim();
    const page = 1;
    getMemberList(page, keyword);
    pushHistoryState({tab: 'user-management', page, keyword});
}