
loadTeams();

function getCookie(name) {
    var cookies = document.cookie.split(';');
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        while (cookie.charAt(0) === ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(name + "=") === 0) {
            return cookie.substring(name.length + 1, cookie.length);
        }
    }
    // 해당 이름의 쿠키가 없으면 undefined 반환
    return undefined;
}

function loadTeams() {
    var token = getCookie("access_token");

    $.ajax({
        url: 'https://ssafyauth-resource.duckdns.org/api/team',
        type: 'GET',
        headers: {
            "Authorization": 'Bearer ' + token
        },
        success: function (response) {
            populateSidebar(response.list);
        },
        error: function (xhr, status, error) {
            console.error('Error loading data:', error);
        }
    });
}

function populateSidebar(data) {
    var container = document.getElementById('teamNavContainer');
    container.innerHTML = ''; // Clear previous entries
    // console.log(data)
    data.forEach(function (team, index) {
        var teamNavItem = document.createElement('li');
        teamNavItem.classList.add('nav-item');
        teamNavItem.innerHTML = `
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapse${index}"
                   aria-expanded="true" aria-controls="collapse${index}">
                    <i class="fas fa-fw fa-cog"></i>
                    <span>${team.teamName}</span>
                </a>
                <div id="collapse${index}" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">관리</h6>
                          <a class="collapse-item" href="/teams/${team.teamSeq}/summary">요약 정보</a>
                            <a class="collapse-item" href="/teams/${team.teamSeq}/dashboard">대시보드</a>
                            <a class="collapse-item" href="/teams/${team.teamSeq}/country-ip">국가 아이피 관리</a>
                            <a class="collapse-item" href="/teams/${team.teamSeq}/management">팀 관리</a>
                        </div>
                </div>`;
        container.appendChild(teamNavItem);
    });
}

