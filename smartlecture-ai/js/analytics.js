/**
 * SmartLecture AI - Analytics & Chart.js Visualizer
 * Renders glassmorphic charts for student performance, subjects breakdown,
 * and weekly study activity.
 */

let weeklyPerformanceChart = null;
let subjectsChart = null;
let studyActivityChart = null;
let topicDistributionChart = null;

document.addEventListener('DOMContentLoaded', () => {
  initAnalyticsCharts();
  initTimeFilters();
});

function initAnalyticsCharts() {
  if (typeof Chart === 'undefined') {
    console.warn('Chart.js not yet loaded, retrying in 300ms...');
    setTimeout(initAnalyticsCharts, 300);
    return;
  }

  // Set default Chart.js styling for futuristic dark glass
  Chart.defaults.color = '#94a3b8';
  Chart.defaults.font.family = "'Plus Jakarta Sans', system-ui, sans-serif";
  Chart.defaults.plugins.tooltip.backgroundColor = 'rgba(15, 23, 42, 0.9)';
  Chart.defaults.plugins.tooltip.borderColor = 'rgba(56, 189, 248, 0.3)';
  Chart.defaults.plugins.tooltip.borderWidth = 1;
  Chart.defaults.plugins.tooltip.padding = 10;
  Chart.defaults.plugins.tooltip.cornerRadius = 8;

  renderWeeklyPerformanceChart();
  renderSubjectsChart();
  renderStudyActivityChart();
  renderTopicDistributionChart();
}

function renderWeeklyPerformanceChart() {
  const ctx = document.getElementById('weeklyPerformanceChart');
  if (!ctx) return;

  const gradientBlue = ctx.getContext('2d').createLinearGradient(0, 0, 0, 300);
  gradientBlue.addColorStop(0, 'rgba(56, 189, 248, 0.45)');
  gradientBlue.addColorStop(1, 'rgba(56, 189, 248, 0.0)');

  weeklyPerformanceChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      datasets: [
        {
          label: 'Your Quiz Score (%)',
          data: [72, 78, 80, 84, 86, 92, 88],
          borderColor: '#38bdf8',
          borderWidth: 3,
          backgroundColor: gradientBlue,
          fill: true,
          tension: 0.38,
          pointBackgroundColor: '#06b6d4',
          pointBorderColor: '#ffffff',
          pointHoverRadius: 6
        },
        {
          label: 'Class Average (%)',
          data: [68, 70, 71, 73, 72, 74, 75],
          borderColor: 'rgba(255, 255, 255, 0.25)',
          borderWidth: 2,
          borderDash: [5, 5],
          pointRadius: 0,
          fill: false,
          tension: 0.38
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
          position: 'top',
          labels: { boxWidth: 12, font: { size: 12 } }
        }
      },
      scales: {
        x: {
          grid: { color: 'rgba(255, 255, 255, 0.04)' }
        },
        y: {
          min: 50,
          max: 100,
          grid: { color: 'rgba(255, 255, 255, 0.05)' },
          ticks: { callback: v => v + '%' }
        }
      }
    }
  });
}

function renderSubjectsChart() {
  const ctx = document.getElementById('subjectsChart');
  if (!ctx) return;

  subjectsChart = new Chart(ctx, {
    type: 'radar',
    data: {
      labels: ['Operating Systems', 'Data Structures', 'Java OOP', 'DBMS', 'Networks', 'Software Eng.'],
      datasets: [
        {
          label: 'Mastery Level (%)',
          data: [88, 84, 92, 79, 86, 90],
          backgroundColor: 'rgba(139, 92, 246, 0.22)',
          borderColor: '#a855f7',
          pointBackgroundColor: '#8b5cf6',
          pointBorderColor: '#ffffff',
          borderWidth: 2
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false }
      },
      scales: {
        r: {
          angleLines: { color: 'rgba(255, 255, 255, 0.08)' },
          grid: { color: 'rgba(255, 255, 255, 0.06)' },
          pointLabels: {
            color: '#cbd5e1',
            font: { size: 11, weight: '600' }
          },
          ticks: {
            display: false,
            stepSize: 20
          },
          suggestedMin: 40,
          suggestedMax: 100
        }
      }
    }
  });
}

function renderStudyActivityChart() {
  const ctx = document.getElementById('studyActivityChart');
  if (!ctx) return;

  studyActivityChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      datasets: [
        {
          label: 'Lectures Processed',
          data: [3, 4, 2, 5, 4, 3, 3],
          backgroundColor: 'rgba(56, 189, 248, 0.7)',
          borderRadius: 6
        },
        {
          label: 'Quizzes Completed',
          data: [2, 5, 3, 6, 5, 4, 6],
          backgroundColor: 'rgba(168, 85, 247, 0.7)',
          borderRadius: 6
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
          position: 'top',
          labels: { boxWidth: 12 }
        }
      },
      scales: {
        x: { grid: { display: false } },
        y: {
          grid: { color: 'rgba(255, 255, 255, 0.05)' },
          ticks: { stepSize: 2 }
        }
      }
    }
  });
}

function renderTopicDistributionChart() {
  const ctx = document.getElementById('topicDistributionChart');
  if (!ctx) return;

  topicDistributionChart = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['Mastered Concepts', 'Review Recommended', 'Needs Attention'],
      datasets: [
        {
          data: [72, 20, 8],
          backgroundColor: [
            'rgba(16, 185, 129, 0.8)',
            'rgba(245, 158, 11, 0.8)',
            'rgba(239, 68, 68, 0.8)'
          ],
          borderColor: 'transparent',
          borderWidth: 0,
          hoverOffset: 6
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      cutout: '72%',
      plugins: {
        legend: {
          position: 'bottom',
          labels: { boxWidth: 12, padding: 15 }
        }
      }
    }
  });
}

function initTimeFilters() {
  const filterBtns = document.querySelectorAll('.time-filter-btn');
  filterBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      filterBtns.forEach(b => b.classList.remove('active'));
      btn.classList.add('active');

      const filter = btn.getAttribute('data-filter');
      updateChartsForFilter(filter);
    });
  });
}

function updateChartsForFilter(filter) {
  if (!weeklyPerformanceChart) return;

  if (filter === 'month') {
    weeklyPerformanceChart.data.labels = ['Week 1', 'Week 2', 'Week 3', 'Week 4'];
    weeklyPerformanceChart.data.datasets[0].data = [74, 79, 83, 89];
    weeklyPerformanceChart.data.datasets[1].data = [69, 71, 73, 75];
  } else if (filter === 'semester') {
    weeklyPerformanceChart.data.labels = ['Month 1', 'Month 2', 'Month 3', 'Month 4', 'Month 5'];
    weeklyPerformanceChart.data.datasets[0].data = [68, 75, 81, 84, 88];
    weeklyPerformanceChart.data.datasets[1].data = [65, 69, 72, 73, 75];
  } else {
    weeklyPerformanceChart.data.labels = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
    weeklyPerformanceChart.data.datasets[0].data = [72, 78, 80, 84, 86, 92, 88];
    weeklyPerformanceChart.data.datasets[1].data = [68, 70, 71, 73, 72, 74, 75];
  }
  weeklyPerformanceChart.update();

  if (window.SmartLectureApp) {
    window.SmartLectureApp.showToast(`Analytics updated for: ${filter.toUpperCase()}`, 'info');
  }
}
