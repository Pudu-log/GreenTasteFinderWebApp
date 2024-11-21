let isDuplicateChecked = false;

document.addEventListener("DOMContentLoaded", function () {
    /**
     * 강의실 선택 박스 데이터 로드
     * 서버에서 강의실 목록을 가져와 select 박스에 추가합니다.
     */
    axios.get('/api/members/login/select-box')
        .then(response => {
            const rooms = response.data.data;
            const roomSelect = document.getElementById('roomSelect');
            rooms.forEach(room => {
                const option = document.createElement('option');
                option.value = room.roomCode;
                option.textContent = room.roomName;
                roomSelect.append(option);
            });
        })
        .catch(error => console.error(error));

    /**
     * 비밀번호 확인 필드 실시간 유효성 검사
     * 비밀번호와 확인 비밀번호가 일치하지 않을 경우 경고 메시지를 표시합니다.
     */
    const passwordConfirmField = document.getElementById("pwdConfirm");
    const passwordError = document.getElementById("passwordError");

    passwordError.style.display = "none";

    passwordConfirmField.addEventListener("input", function () {
        const passwordField = document.getElementById("pwd");
        passwordError.style.display = (passwordField.value !== passwordConfirmField.value) ? "block" : "none";
    });
});

/**
 * 회원가입 폼 유효성 검사
 * @returns {boolean} 폼이 유효한 경우 true, 그렇지 않으면 false
 */
function validateForm() {
    if (!isDuplicateChecked) {
        alert("중복 체크를 먼저 수행해 주세요.");
        return false;
    }

    const form = document.forms["signupForm"];
    const today = new Date();

    const fields = [
        {name: "id", message: "아이디를 입력해 주세요", maxLength: 20},
        {name: "pw", message: "비밀번호를 입력해 주세요"},
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

    if (!/^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{1,20}$/.test(form.pw.value)) {
        return showError("비밀번호는 영문, 숫자, 특수문자를 포함하여 최대 20자로 입력해 주세요", form.pw);
    }

    if (form.pw.value !== form.pwConfirm.value) {
        return showError("비밀번호가 일치하지 않습니다.", form.pwConfirm);
    }
    return true;
}

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

/**
 * 아이디 중복 체크
 * 서버에 중복 여부를 확인하고 결과에 따라 알림을 표시합니다.
 */
function idCheck() {
    const form = document.forms["signupForm"];
    const idValue = form["id"].value;

    if (!idValue) {
        alert("아이디를 입력해 주세요.");
        return;
    }

    axios.get('/api/members/login/id-check?id=' + idValue)
        .then(response => {
            if (response.data.data === '아이디 사용가능') {
                isDuplicateChecked = true;
                alert("중복 체크 통과");
            } else {
                alert("중복된 아이디 입니다.");
                isDuplicateChecked = false;
            }
        })
        .catch(error => {
            console.error(error);
            alert("중복 체크 중 오류가 발생했습니다.");
        });
}

/**
 * 회원가입 폼 제출
 * 폼 유효성 검사를 통과한 경우 서버에 데이터를 전송합니다.
 */
document.forms["signupForm"].addEventListener("submit", function (event) {
    event.preventDefault();

    if (!validateForm()) return;

    const form = document.forms["signupForm"];
    const formData = {
        id: form["id"].value,
        pw: form["pw"].value,
        name: form["name"].value,
        roomCode: form["roomCode"].value,
        email: form["email"].value,
        birth: form["birth"].value
    };

    axios.post('/api/members/join', formData)
        .then(() => {
            location.href = "/login";
        })
        .catch(error => {
            console.error(error);
            alert("회원가입 실패");
        });
});
