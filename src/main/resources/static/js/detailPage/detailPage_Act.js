//페이지시작시 화면 설정
axios.get('/api/detailpage/selectTotalAct?id=' + sessionId + '&store_id=' + placeId)
    .then((response) => {
        console.log(response.data);
        $('#like-count').text(response.data.totalG);
        if (response.data.gcnt > 0) {
            $('#like-button').addClass('active');
        }
        if (response.data.fcnt > 0) {
            $('#favorite-button').addClass('active');
        }
        if (response.data.vcnt > 0) {
            $('#visit-button').addClass('active');
        }
    })
    .catch((error) => {
        console.log(error);
    })

//삭제 요청
function deleteAct(id, store_id, gubn, inputId) {
    axios.delete('api/detailpage/deleteAct?id=' + id + '&store_id=' + store_id + '&gubn=' + gubn)
        .then((response) => {
            console.log(response.data);
            inputId.removeClass('active');
            likeAllSelect(placeId);
        })
        .catch((error) => {
            console.log(error);
        })
}

//추가 요청
function insertAct(id, store_id, gubn, inputId) {
    axios.post('/api/detailpage/insertAct', {
        id: id,
        store_id: store_id,
        gubn: gubn
    })
        .then((response) => {
            inputId.addClass('active');
            console.log(response.data);
            likeAllSelect(placeId);
        })
        .catch((error) => {
            console.log(error);
        })
}

//현재 상점 좋아요 수 요청
function likeAllSelect(placeId){
    axios.get('/api/detailpage/likeAllSelect?store_id=' + placeId)
        .then((response) => {
            console.log(response.data);
            $('#like-count').text(response.data);
        })
        .catch((error) => {
            console.log(error);
        })
}

$('#like-button').click(function () {
    if ($(this).hasClass('active')) {
        deleteAct(sessionId, placeId, 'G', $(this));
    } else {
        insertAct(sessionId, placeId, 'G', $(this));
    }
})

$('#favorite-button').click(function () {
    if ($(this).hasClass('active')) {
        deleteAct(sessionId, placeId, 'F', $(this));
    } else {
        insertAct(sessionId, placeId, 'F', $(this));
    }
})

$('#visit-button').click(function () {
    if ($(this).hasClass('active')) {
        deleteAct(sessionId, placeId, 'V', $(this));
    } else {
        insertAct(sessionId, placeId, 'V', $(this));
    }
})