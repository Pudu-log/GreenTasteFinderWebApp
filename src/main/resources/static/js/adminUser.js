/**
 * 사용자 목록 불러오기
 * @param {number} page - 현재 페이지 번호 (기본값: 1)
 */
function getMemberList(page = 1) {
    axios.get(`/api/admin/member-list?page=${page}`)
        .then(response => {
            const {members, pagingBtn} = response.data.data;

            if (!members) {
                console.error('회원 데이터가 없습니다.');
                return;
            }
            updateTable('#user-management tbody', members, createMemberRow);
            updatePagingButtons(pagingBtn, getMemberList);
        })
        .catch(error => console.error(`사용자 목록 가져오는 중 오류: ${error}`));
}

/**
 * 사용자 목록 테이블 업데이트
 * @param {string} tableBodySelector - 테이블 tbody
 * @param {Array} data - 사용자 데이터 배열
 * @param {Function} rowGenerator - 행 생성 함수
 */
function updateTable(tableBodySelector, data, rowGenerator) {
    const tbody = document.querySelector(tableBodySelector);
    tbody.innerHTML = '';
    data.forEach(item => tbody.appendChild(rowGenerator(item)));
}

/**
 * 사용자 목록 생성
 * @param {Object} member - 사용자 데이터
 * @returns {HTMLTableRowElement} 생성된 테이블 행
 */
function createMemberRow(member) {
    const row = document.createElement('tr');

    function formatDate(date) {
        return date ? new Date(date).toISOString().split('T')[0] : '-';
    }

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
 * 페이징 버튼 생성 및 업데이트
 * @param {Object} pagingBtn - 페이징 버튼 데이터
 */
function updatePagingButtons(pagingBtn, onPageClick) {
    const pagingContainer = document.querySelector('#paging-buttons');
    pagingContainer.innerHTML = '';

    if (!pagingBtn) return;

    // 맨처음 버튼
    if (pagingBtn.firstPageBtn) {
        const firstButton = createPagingButton('<<', () => onPageClick(1));
        pagingContainer.appendChild(firstButton);
    }

    // 이전 버튼
    if (pagingBtn.prevBtn) {
        const prevButton = createPagingButton('<', () => onPageClick(pagingBtn.startPage - 1));
        pagingContainer.appendChild(prevButton);
    }

    // 페이지 번호 버튼
    for (let i = pagingBtn.startPage; i <= pagingBtn.endPage; i++) {
        const pageButton = createPagingButton(i, () => onPageClick(i), i === pagingBtn.currentPage);
        pagingContainer.appendChild(pageButton);
    }

    // 다음 버튼
    if (pagingBtn.nextBtn) {
        const nextButton = createPagingButton('>', () => onPageClick(pagingBtn.endPage + 1));
        pagingContainer.appendChild(nextButton);
    }

    // 맨끝 버튼
    if (pagingBtn.lastPageBtn) {
        const lastButton = createPagingButton('>>', () => onPageClick(pagingBtn.totalPage));
        pagingContainer.appendChild(lastButton);
    }
}

/**
 * 페이징 버튼 생성
 * @param {string|number} text - 버튼 텍스트
 * @param {boolean} [isActive=false] - 활성화 여부
 * @returns {HTMLButtonElement} 생성된 버튼
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
