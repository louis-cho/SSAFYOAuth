// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

import { Client } from 'https://cdn.skypack.dev/@stomp/stompjs';
import { updateMetrics } from './dataStore.js';
// WebSocket 연결 생성
const client = new Client({
  brokerURL: 'wss://ssafyauth-authorization.duckdns.org/ws',
  reconnectDelay: 5000,
  debug: function (str) {
    // console.log(str);
  }
});

client.onConnect = function(frame) {
  console.log('Connected to WebSocket!');

  // 서버로부터 메시지를 수신하면 웹 페이지에 표시
  client.subscribe('/topic/metrics', function (message) {
    const metrics = JSON.parse(message.body);
    // console.log('Received metrics:', metrics);
    updateMetrics(metrics);

    updateChartData(metrics.cpuLoad);
    // document.getElementById('metricsDisplay').innerHTML = 'CPU Load: ' + metrics.cpuLoad + ', Memory Load: ' + metrics.memoryLoad;
  });
};

// WebSocket 연결 오류 발생 시 처리
client.onWebSocketError = function (event) {
  console.error('WebSocket Error:', event);
};

client.onStompError = function (frame) {
  console.error('Broker reported error: ' + frame.headers['message']);
  console.error('Additional details: ' + frame.body);
};

// WebSocket에 연결
client.activate();




var myLineChart = new Chart(document.getElementById("myAreaChart"), {
  type: 'line',
  data: {
    labels: new Array(10).fill(''), //
    datasets: [{
      label: "CPU Load",
      lineTension: 0.3,
      backgroundColor: "rgba(78, 115, 223, 0.05)",
      borderColor: "rgba(78, 115, 223, 1)",
      pointRadius: 3,
      pointBackgroundColor: "rgba(78, 115, 223, 1)",
      pointBorderColor: "rgba(78, 115, 223, 1)",
      pointHoverRadius: 3,
      pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
      pointHoverBorderColor: "rgba(78, 115, 223, 1)",
      pointHitRadius: 10,
      pointBorderWidth: 2,
      data: [],
    }],
  },
  options: {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      xAxes: [{
        time: {
          unit: 'number'
        },
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 10
        }
      }],
      yAxes: [{
        ticks: {
          min: 0, // 최소값 설정
          max: 100, // 최대값 설정
          maxTicksLimit: 5,
          padding: 10,
          callback: function(value, index, values) {
            return value + '%';
          }
        },
        gridLines: {
          color: "rgb(234, 236, 244)",
          zeroLineColor: "rgb(234, 236, 244)",
          drawBorder: false,
          borderDash: [2],
          zeroLineBorderDash: [2]
        }
      }],
    },
    legend: {
      display: false
    },
    tooltips: {
      mode: 'index',
      intersect: false,
      callbacks: {
        label: function(tooltipItem, chart) {
          return chart.datasets[tooltipItem.datasetIndex].label + ': ' + tooltipItem.yLabel + '%';
        }
      }
    }
  }
});


function updateChartData(cpuLoad) {
  if (myLineChart.data.datasets[0].data.length >= 10) {
    myLineChart.data.datasets[0].data.shift(); // 첫 번째 데이터 제거
  }
  myLineChart.data.datasets[0].data.push(cpuLoad); // 데이터 추가
  myLineChart.update();
}