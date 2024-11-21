let changePass = '';

const passForm = document.passwordChangeForm;
const modal = $('#passwordModal');
const closeModal = $('.close');

// Open Modal
$('#changePw-btn').click(function (e) {
    e.preventDefault();
    modal.fadeIn();
});

// Close Modal
closeModal.click(function () {
    modal.fadeOut();
});

// Submit Form
$("[name='passwordChangeForm']").submit(function (e) {
    e.preventDefault();

    const newPassword = $('#newPassword').val();
    const confirmPassword = $('#confirmPassword').val();

    console.log(passForm);
    if (!/^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{1,20}$/.test(passForm.newPassword.value)) {
        return showError("비밀번호는 영문, 숫자, 특수문자를 포함하여 최대 20자로 입력해 주세요", passForm.newPassword);
    } else if (newPassword !== confirmPassword) {
        return showError('비밀번호가 일치하지 않습니다.', passForm.newPassword);
    }
    changePass = newPassword;
    modal.fadeOut();
});

/**
 * 에러 메시지 표시 및 입력 필드에 포커스
 * @param {string} message - 에러 메시지
 * @param {HTMLElement} input - 포커스할 입력 필드
 * @returns {boolean} 항상 false 반환
 */
function showError(message, input) {
    alert(message);
    input.focus();
    return false;
}

function setSelect(roomCode) {
    axios.get('/api/members/login/select-box')
        .then(response => {
            const rooms = response.data.data;
            // console.log(response);
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
}

function setUpdateDiv(updateDt) {
    document.querySelector('#update-div').style.display = !updateDt ? 'none' : '';
    console.log(!updateDt);
    if (!updateDt == false) {
        $('#update-div').html
        (
            '<label>수정일시</label>'
            + '<span>' + updateDt + '</span>'
        );
    }
}

function validateForm() {
    const form = document.forms["update-form"];
    const today = new Date();

    const fields = [
        {name: "name", message: "이름을 입력해 주세요"},
        {name: "roomCode", message: "강의실을 선택해 주세요"},
        {name: "email", message: "이메일을 입력해 주세요"},
        {name: "birth", message: "생년월일을 입력해 주세요"}
    ];

    for (let field of fields) {
        const input = form[field.name];
        if (!input.value) return showError(field.message, input);
        if (field.maxLength && input.value.length > field.maxLength) {
            return showError(`'${field.name}'는 ${field.maxLength}자 이내로 입력해 주세요`, input);
        }
    }

    if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(form.email.value)) {
        return showError("올바른 이메일 형식을 입력해 주세요", form.email);
    }

    if (new Date(form.birth.value) > today) {
        return showError("생년월일은 오늘 날짜보다 이전으로 입력해 주세요", form.birth);
    }

    return true;
}

document.forms["update-form"].addEventListener("submit", function (event) {
    event.preventDefault();

    if (!validateForm()) return;

    const form = document.forms["update-form"];
    const formData = {
        id: form["id"].value,
        pw: !changePass ? form["pw"].value : changePass,
        name: form["name"].value,
        roomCode: form["roomCode"].value,
        email: form["email"].value,
        birth: form["birth"].value
    };

    axios.post('/api/my/memberUpdate', formData)
        .then(() => {
            alert("수정 완료");
            location.href = "/myPage-userInfo";
        })
        .catch(error => {
            console.error(error);
            alert("수정 실패");
        });
});