function setSelect(roomCode) {
    axios.get('/api/login/selectBox')
        .then(response => {
            const rooms = response.data.data;
            console.log(response);
            const roomSelect = document.getElementById('roomSelect');
            rooms.forEach(room => {
                const option = document.createElement('option');
                option.value = room.roomCode;
                option.textContent = room.roomName;
                roomSelect.append(option);
            });
            roomSelect.value = roomCode;
        })
        .catch(error => {
            console.error(error);
        });
    //
}