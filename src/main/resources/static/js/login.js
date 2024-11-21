/**
 * 로그인 폼 유효성 검사
 * @returns {boolean} 폼이 유효한 경우 true, 그렇지 않으면 false
 */
function validateForm() {
    const form = document.forms["loginForm"];

    const fields = [
        {name: "id", message: "아이디를 입력해 주세요", maxLength: 20},
        {name: "pw", message: "비밀번호를 입력해 주세요"},
    ];

    for (let field of fields) {
        const input = form[field.name];
        if (!input.value) return showError(field.message, input);
        if (field.maxLength && input.value.length > field.maxLength) {
            return showError(`'${field.name}'는 ${field.maxLength}자 이내로 입력해 주세요`, input);
        }
    }
    return true;
}

/**
 * 로그인 폼 제출 이벤트 처리
 * 폼 유효성 검사를 통과한 경우 서버에 데이터를 전송합니다.
 */
document.forms["loginForm"].addEventListener("submit", function (event) {
    event.preventDefault();

    if (!validateForm()) return;

    const form = document.forms["loginForm"];
    const formData = {
        id: form["id"].value,
        pw: form["pw"].value,
    };

    axios.post('/api/members/login', formData)
        .then(response => {
            if (response.data.data === '/admin') {
                location.href = "/admin";
            } else if (response.data.data === '/') {
                location.href = "/";
            } else {
                alert("로그인 실패");
            }
        })
        .catch(error => {
            console.error(error);
            alert("로그인 실패");
        });
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
