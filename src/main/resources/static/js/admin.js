// 탭 전환 함수
function showTab(tabName) {
    const tabs = document.querySelectorAll('.tab-content');
    const buttons = document.querySelectorAll('.tab-menu button');

    tabs.forEach(tab => tab.style.display = 'none');
    buttons.forEach(button => button.classList.remove('active'));

    const targetTab = document.getElementById(tabName);
    const targetButton = document.querySelector('button[data-tab="' + tabName + '"]');

    if (targetTab && targetButton) {
        targetTab.style.display = 'block';
        targetButton.classList.add('active');

        if (tabName === 'lecture-management') {
            getRoomList();
        }
    }
}

// 사용자 목록 불러오기
function getMemberList() {
    axios.get('/api/admin/member-list')
        .then(response => {
            const members = response.data.data;
            const tbody = document.querySelector('#user-management tbody');
            tbody.innerHTML = '';
            members.forEach(member => {
                const row = '<tr>' +
                    '<td>' + member.id + '</td>' +
                    '<td>' + member.name + '</td>' +
                    '<td>' + member.roomCode + '</td>' +
                    '<td>' + member.email + '</td>' +
                    '<td>' + new Date(member.birth).toISOString().split('T')[0] + '</td>' +
                    '<td>' + (member.updateDt || member.insertDt) + '</td>' +
                    '<td>' + (member.adminYn === 'Y' ? '관리자' : '사용자') + '</td>' +
                    '<td><button onclick="deleteMember(\'' + member.id + '\')">삭제</button></td>' +
                    '</tr>';
                tbody.innerHTML += row;
            });
        })
        .catch(error => console.error('데이터를 불러오는 중 오류 발생:', error));
}

// 강의실 목록 불러오기
function getRoomList() {
    axios.get('/api/admin/room-list')
        .then(response => {
            const rooms = response.data.data;
            const tbody = document.querySelector('#room-table-body');
            tbody.innerHTML = '';
            rooms.forEach(room => {
                const row = '<tr>' +
                    '<td>' + room.roomCode + '</td>' +
                    '<td>' + room.roomName + '</td>' +
                    '<td><button onclick="deleteRoom(\'' + room.roomCode + '\')">삭제</button></td>' +
                    '</tr>';
                tbody.innerHTML += row;
            });
        })
        .catch(error => console.error('강의실 목록을 불러오는 중 오류 발생:', error));
}

// 강의실 삭제
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

function insertRoom() {
    const roomCode = document.getElementById('roomCode').value.trim();
    const roomName = document.getElementById('roomName').value.trim();

    if (!roomCode || !roomName) {
        alert('강의실 코드와 강의실 이름을 모두 입력해주세요.');
        return;
    }

    axios.post('/api/admin/room/insert', {
        roomCode: roomCode,
        roomName: roomName
    }, {
        headers: {
            'Content-Type': 'application/json'
        }
    })
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

document.addEventListener('DOMContentLoaded', function () {
    showTab('user-management');
    getMemberList();
    getRoomList();
});