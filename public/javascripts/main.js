function toLogin() {
    window.location.href = "/login";
}

function toRegister() {
    window.location.href = "/register";
}

function toMyTask(id) {
    window.location.href = "/getTasks/" + id
}

function toMyNote(id) {
    window.location.href = "/getNotes/" + id
}