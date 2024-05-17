// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

var ctx = document.getElementById('logChart');
var logChart = new Chart(ctx, {
    type: 'line', // 차트의 유형을 라인 차트로 설정
    data: {
        labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24'], // X축 레이블
        datasets: [{
            label: 'Monthly Sales Data', // 데이터셋의 레이블
            data: [12, 19, 3, 5, 2, 3, 9, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 10, 10, 5, 5], // 실제 데이터
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
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            y: {
                beginAtZero: true // Y축이 0에서 시작
            }
        }
    },
    type: 'line', // 차트의 유형을 라인 차트로 설정
    data: {
        labels: Array.from({length: 24}, (_, i) => `${i + 1}:00`),
        datasets: [{
            label: '시간 별 전체 사용자 요청량', // 데이터셋의 레이블
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
// const teamId = new URL(window.location.href).pathname.split('/')[2];
var start = new Date();
start.setDate(start.getDate() - 1);

var year = start.getFullYear(); // 년도(네 자리)
var month = start.getMonth() + 1; // 월(0부터 시작하므로 +1 필요)
var day = start.getDate(); // 일
var startTime = year + "_" + month + "_" + day;

const url = "http://localhost:9000/api/ttt/fetch";
// 함수를 선언하여 3초마다 서버에서 데이터를 받아 차트를 업데이트
//function fetchData() {
//    fetch('http://43.203.205.138:3000/login_stats/_search')
//        .then(response => response.json())
//        .then(data => {
//            // teamId가 "1"인 데이터만 필터링
//            const filteredData = data.hits.hits.filter(d => d._source.teamId === teamId);
//            // 시간별로 로그인 수를 집계
//            const hourlyCounts = Array(24).fill(0);
//            filteredData.forEach(d => {
//                const hour = new Date(d._source.createdAt).getHours();
//                hourlyCounts[hour]++;
//            });
//
//            console.log(hourlyCounts)
//            // 차트 데이터 업데이트
//            logChart.data.datasets[0].data = hourlyCounts;
//            logChart.update();
//        })
//        .catch(error => console.error('Error fetching data:', error));
//}

function fetchData() {
    fetch(url, {
         method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ teamId: 0, startTime: startTime })
    })
        .then(response => response.text()) // 응답 데이터를 문자열로 받음
        .then(data => {
            // 줄바꿈 문자(\n)을 기준으로 문자열을 분할하여 각 로그 항목을 배열에 저장
            const logEntries = data.split('\n');

            // 각 로그 항목을 객체로 변환하고 이를 배열에 저장
            const logData = logEntries.map(entry => {
            if (entry.trim() === '') return null;

                // entry를 적절히 가공하여 로그 데이터를 객체로 변환
                const parts = entry.split(',');
                const userId = parts[0].split('=')[1];
                const teamId = parseInt(parts[1].split('=')[1]);
                const createdAt = new Date(parts[2].split('=')[1]);
                const success = parts[3].split('=')[1] === 'true';
                return { userId, teamId, createdAt, success };
            });

            // 이제 logData 배열에는 파싱된 로그 데이터가 저장되어 있음
            console.log(logData);

            // 이하 로직은 logData 배열을 사용하여 그래프를 업데이트하는 것과 동일
            const hourlyCounts = Array(24).fill(0);
            logData.forEach(d => {
                if (d !== null && d.createdAt !== null) {
                       const hour = d.createdAt.getHours();
                       hourlyCounts[hour]++;
                   }
            });

            logChart.data.datasets[0].data = hourlyCounts;
            logChart.update();
        })
        .catch(error => console.error('Error fetching data:', error));
}

// 페이지 로드 시 fetchData를 즉시 호출하고, 이후 3초마다 반복
fetchData();
setInterval(fetchData, 3000);