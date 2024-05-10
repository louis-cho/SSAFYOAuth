// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

var ctx = document.getElementById('logChart');
var logChart = new Chart(ctx, {
type: 'line', // 차트의 유형을 라인 차트로 설정
data: {
labels: Array.from({length: 24}, (_,i) => `${i + 1}:00`),
datasets: [{
label: '시간 별 사용자 요청량', // 데이터셋의 레이블
data: Array(24).fill(0),
backgroundColor: [
'rgba(255, 99, 132, 0.2)'
],
borderColor: [
'rgba(255, 99, 132, 1)'
],
borderWidth: 1
}]
},
options: {
scales: {
y: {
beginAtZero: true // Y축이 0에서 시작
}
}
}
});

// URL에서 teamId 추출
const teamId = new URL(window.location.href).pathname.split('/')[2];

// 함수를 선언하여 3초마다 서버에서 데이터를 받아 차트를 업데이트
function fetchData() {
    fetch('http://43.203.205.138:3000/login_stats/_search')
        .then(response => response.json())
        .then(data => {
            // teamId가 "1"인 데이터만 필터링
            const filteredData = data.hits.hits.filter(d => d._source.teamId === teamId);
            // 시간별로 로그인 수를 집계
            const hourlyCounts = Array(24).fill(0);
            filteredData.forEach(d => {
                const hour = new Date(d._source.createdAt).getHours();
                hourlyCounts[hour]++;
            });

            console.log(hourlyCounts)
            // 차트 데이터 업데이트
            logChart.data.datasets[0].data = hourlyCounts;
            logChart.update();
        })
        .catch(error => console.error('Error fetching data:', error));
}

// 페이지 로드 시 fetchData를 즉시 호출하고, 이후 3초마다 반복
fetchData();
setInterval(fetchData, 3000);