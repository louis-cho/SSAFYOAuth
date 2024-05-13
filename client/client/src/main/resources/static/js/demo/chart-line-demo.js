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
        maintainAspectRatio: false ,
        scales: {
            y: {
                beginAtZero: true // Y축이 0에서 시작
            }
        }
    }
});
