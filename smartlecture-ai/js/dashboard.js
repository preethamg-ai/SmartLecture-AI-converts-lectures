/**
 * SmartLecture AI - Dashboard Module
 * Handles animated statistics, greeting, upload drag-and-drop on dashboard,
 * quick actions, and recent lecture interactions.
 */

document.addEventListener('DOMContentLoaded', () => {
  initGreeting();
  initAnimatedCounters();
  initDashboardDropzone();
  initQuickActions();
});

// Dynamic Greeting based on current local time
function initGreeting() {
  const greetingEl = document.getElementById('dynamic-greeting');
  if (!greetingEl) return;

  const hour = new Date().getHours();
  let greeting = "Good morning 👋";
  if (hour >= 12 && hour < 17) {
    greeting = "Good afternoon 👋";
  } else if (hour >= 17) {
    greeting = "Good evening 👋";
  }
  greetingEl.innerHTML = `${greeting} <span style="font-weight:400;color:var(--text-muted);margin-left:0.4rem;">· Ready to learn smarter?</span>`;
}

// Animate numbers from 0 up to target values
function initAnimatedCounters() {
  const counters = document.querySelectorAll('.stat-counter');
  counters.forEach(counter => {
    const target = parseInt(counter.getAttribute('data-target') || '0', 10);
    const suffix = counter.getAttribute('data-suffix') || '';
    const duration = 1400;
    const startTime = performance.now();

    function updateCounter(currentTime) {
      const elapsed = currentTime - startTime;
      const progress = Math.min(elapsed / duration, 1);
      // easeOutExpo
      const ease = progress === 1 ? 1 : 1 - Math.pow(2, -10 * progress);
      const currentVal = Math.floor(ease * target);
      counter.innerText = currentVal + suffix;

      if (progress < 1) {
        requestAnimationFrame(updateCounter);
      } else {
        counter.innerText = target + suffix;
      }
    }

    requestAnimationFrame(updateCounter);
  });
}

// Dashboard Upload Dropzone
function initDashboardDropzone() {
  const dropzone = document.getElementById('dashboard-dropzone');
  const fileInput = document.getElementById('dashboard-file-input');

  if (!dropzone) return;

  ['dragenter', 'dragover'].forEach(eventName => {
    dropzone.addEventListener(eventName, (e) => {
      e.preventDefault();
      e.stopPropagation();
      dropzone.classList.add('drag-over');
    }, false);
  });

  ['dragleave', 'drop'].forEach(eventName => {
    dropzone.addEventListener(eventName, (e) => {
      e.preventDefault();
      e.stopPropagation();
      dropzone.classList.remove('drag-over');
    }, false);
  });

  dropzone.addEventListener('drop', (e) => {
    const dt = e.dataTransfer;
    const files = dt.files;
    if (files.length > 0) {
      handleDashboardFileSelect(files[0]);
    }
  });

  dropzone.addEventListener('click', (e) => {
    if (e.target.tagName !== 'BUTTON' && !e.target.closest('button')) {
      if (fileInput) fileInput.click();
    }
  });

  if (fileInput) {
    fileInput.addEventListener('change', (e) => {
      if (e.target.files && e.target.files[0]) {
        handleDashboardFileSelect(e.target.files[0]);
      }
    });
  }
}

function handleDashboardFileSelect(file) {
  sessionStorage.setItem('staged_lecture_name', file.name);
  sessionStorage.setItem('staged_lecture_size', (file.size / (1024 * 1024)).toFixed(2) + ' MB');
  sessionStorage.setItem('staged_lecture_type', file.type || 'Document/Audio');
  
  if (window.SmartLectureApp) {
    window.SmartLectureApp.showToast(`Loading "${file.name}" into AI processor...`, 'info');
  }
  
  setTimeout(() => {
    window.location.href = 'upload.html?autoStart=true';
  }, 450);
}

// AI Quick Actions
function initQuickActions() {
  const actionCards = document.querySelectorAll('.quick-action-card');
  actionCards.forEach(card => {
    card.addEventListener('click', (e) => {
      const actionType = card.getAttribute('data-action');
      if (actionType === 'explain') {
        e.preventDefault();
        triggerExplainModal();
      } else if (actionType === 'transcribe') {
        e.preventDefault();
        window.location.href = 'upload.html?mode=audio';
      }
    });
  });
}

// Topic explanation interactive dialog
function triggerExplainModal() {
  const topic = prompt("Enter the technical topic or concept you want SmartLecture AI to explain (e.g., 'Virtual Memory Paging'):", "Virtual Memory Paging");
  if (topic && topic.trim()) {
    if (window.SmartLectureApp) {
      window.SmartLectureApp.showToast(`AI generating conceptual breakdown for "${topic}"...`, 'info');
    }
    setTimeout(() => {
      window.location.href = `notes.html?search=${encodeURIComponent(topic)}`;
    }, 600);
  }
}
