// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';
import { metrics } from './dataStore.js';



// Pie Chart Example
var ctx = document.getElementById("myPieChart");
var myPieChart = new Chart(ctx, {
  type: 'doughnut',
  data: {
    labels: ["사용중인 메모리", "남은 메모리"],
    datasets: [{
      data: [0, 0], // 이 부분은 서버로부터 받는 데이터로 동적 업데이트 예정
      backgroundColor: ['#4e73df', '#1cc88a'],
      hoverBackgroundColor: ['#2e59d9', '#17a673'],
      hoverBorderColor: "rgba(234, 236, 244, 1)",
    }],
  },
  options: {
    responsive: true,
    maintainAspectRatio: false,
    tooltips: {
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: true, // 색상을 표시하도록 변경
      caretPadding: 10,
      callbacks: {
        label: function(tooltipItem, data) {
          const label = data.labels[tooltipItem.index];
          const value = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
          return label + ': ' + value + '%'; // 퍼센트 단위 추가
        }
      }
    },
    legend: {
      display: true, // 레전드 표시
      position: 'top', // 레전드 위치 조정
      labels: {
        fontColor: '#858796',
        usePointStyle: true // 더 눈에 띄는 포인트 스타일 사용
      }
    },
    cutoutPercentage: 80,
    animation: {
      animateScale: true, // 차트 스케일 애니메이션 활성화
      animateRotate: true // 회전 애니메이션 활성화
    }
  },
});


function updateUI() {
  // console.log(metrics)
  if (metrics.memoryUsage) {
    const memoryUsed = metrics.memoryUsage;
    const memoryFree = 100 - memoryUsed;
    myPieChart.data.datasets[0].data = [memoryUsed, memoryFree];
    myPieChart.update();
  }
}
updateUI()
setInterval(updateUI, 1000);