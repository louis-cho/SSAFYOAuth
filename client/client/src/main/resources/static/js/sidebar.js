// 이 스크립트는 사이드바가 로드된 후 실행될 것입니다.
$(document).ready(function () {
    if ($('#accordionSidebar').length > 0) {
        loadTeams();
    }
});
window.onload = function () {
    console.log("side b1ar");
};
function loadTeams() {
    var token = getCookie("access_token");
    $.ajax({
        url: 'http://localhost:8090/api/team',
        type: 'GET',
        headers: {
            "Authorization": 'Bearer ' + token
        },
        success: function (response) {
            populateSidebar(response);
        },
        error: function (xhr, status, error) {
            console.error('Error loading data:', error);
        }
    });
}

function populateSidebar(data) {
    var container = document.getElementById('teamNavContainer');
    container.innerHTML = '';
    data.forEach(function (team, index) {
        var teamNavItem = document.createElement('li');
        teamNavItem.classList.add('nav-item');
        // 요소 생성 생략
        container.appendChild(teamNavItem);
    });
}

function getCookie(name) {
    let value = `; ${document.cookie}`;
    let parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}
