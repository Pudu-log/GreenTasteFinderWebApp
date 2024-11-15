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

document.forms["loginForm"].addEventListener("submit", function (event) {
    event.preventDefault();

    if (!validateForm()) return;

    const form = document.forms["loginForm"];
    const formData = {
        id: form["id"].value,
        pw: form["pw"].value,
    };

    axios.post('/api/login', formData)
        .then(response => {
            if (response.data.data === '로그인 성공') {
                alert("로그인 성공")
                location.href = "/";
            } else {
                alert("로그인 실패")
            }
        })
        .catch(error => {
            console.error(error);
            alert("로그인 실패")
        });
});

function showError(message, input) {
    alert(message);
    input.focus();
    return false;
}