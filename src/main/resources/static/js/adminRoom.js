/**
 * 강의실 목록 불러오기
 * 강의실 데이터를 서버에서 가져와 테이블에 업데이트합니다.
 */
function getRoomList() {
    axios.get('/api/admin/room-list')
        .then(response => {
            const rooms = response.data.data;

            if (!rooms) {
                console.error('강의실 데이터가 없습니다.');
                return;
            }

            updateRoomTable('#room-table-body', rooms, createRoomRow);
        })
        .catch(error => console.error(`강의실 목록 가져오는 중 오류: ${error}`));
}

/**
 * 강의실 목록 테이블 업데이트
 * @param {string} tableBodySelector - 테이블 tbody 선택자
 * @param {Array} data - 강의실 데이터 배열
 * @param {Function} rowGenerator - 행 생성 함수
 */
function updateRoomTable(tableBodySelector, data, rowGenerator) {
    const tbody = document.querySelector(tableBodySelector);
    tbody.innerHTML = '';
    data.forEach(item => tbody.appendChild(rowGenerator(item)));
}

/**
 * 강의실 목록 행 생성
 * @param {Object} room - 강의실 데이터 객체
 * @returns {HTMLTableRowElement} 생성된 테이블 행 요소
 */
function createRoomRow(room) {
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${room.roomCode}</td>
        <td>${room.roomName}</td>
        <td><button onclick="deleteRoom('${room.roomCode}')">삭제</button></td>
    `;
    return row;
}

/**
 * 강의실 삭제
 * @param {string} roomCode - 삭제할 강의실 코드
 */
function deleteRoom(roomCode) {
    if (confirm('정말로 삭제하시겠습니까?')) {
        axios.delete(`/api/admin/room/delete/${roomCode}`)
            .then(response => {
                if (response.data.data === '삭제 성공') {
                    console.log('삭제 성공:', response.data.data);
                    getRoomList();
                } else {
                    console.error('삭제 실패:', response.data.data);
                }
            })
            .catch(error => console.error('삭제 요청 중 오류 발생:', error));
    }
}

/**
 * 강의실 추가
 * 새로운 강의실을 서버에 추가하고 테이블을 업데이트합니다.
 */
function insertRoom() {
    const roomCode = document.getElementById('roomCode').value.trim();
    const roomName = document.getElementById('roomName').value.trim();

    if (!roomCode || !roomName) {
        alert('강의실 코드와 강의실 이름을 모두 입력해주세요.');
        return;
    }

    axios.post('/api/admin/room/insert', { roomCode, roomName })
        .then(response => {
            if (response.data.data === '추가 성공') {
                console.log('추가 성공:', response.data.data);
                getRoomList();
                document.getElementById('roomCode').value = '';
                document.getElementById('roomName').value = '';
            } else {
                console.error('추가 실패:', response.data.data);
            }
        })
        .catch(error => console.error('강의실 추가 요청 중 오류 발생:', error));
}
