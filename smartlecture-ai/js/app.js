/**
 * SmartLecture AI - Core Application Script
 * Global UI helpers, sidebar drawer, toast system, and API placeholder hooks.
 */

// Global state & sample mock database
window.SmartLectureDB = {
  user: {
    name: "Preetham",
    role: "Student",
    program: "BCA Semester 6",
    college: "Bangalore University",
    status: "Online",
    stats: {
      lecturesProcessed: 24,
      notesGenerated: 86,
      quizzesCompleted: 31,
      averageScore: 84
    }
  },
  lectures: [
    {
      id: "os-01",
      title: "Operating Systems",
      subtitle: "Virtual Memory, Paging & Segmentation",
      date: "Today, 10:30 AM",
      duration: "54 mins",
      subject: "Operating Systems",
      icon: "cpu",
      notesReady: true,
      quizReady: true,
      quizScore: 84,
      saved: true
    },
    {
      id: "dsa-02",
      title: "Data Structures & Algorithms",
      subtitle: "Balanced AVL Trees & Graph Traversals",
      date: "Yesterday, 3:15 PM",
      duration: "62 mins",
      subject: "Data Structures",
      icon: "git-branch",
      notesReady: true,
      quizReady: false,
      saved: true
    },
    {
      id: "java-03",
      title: "Java Programming",
      subtitle: "Multithreading, Synchronization & Locks",
      date: "3 days ago",
      duration: "48 mins",
      subject: "Java OOP",
      icon: "code-2",
      notesReady: true,
      quizReady: true,
      quizScore: 92,
      saved: false
    },
    {
      id: "dbms-04",
      title: "Database Management Systems",
      subtitle: "ACID Properties & B+ Indexing",
      date: "5 days ago",
      duration: "58 mins",
      subject: "DBMS",
      icon: "database",
      notesReady: true,
      quizReady: true,
      quizScore: 78,
      saved: false
    },
    {
      id: "net-05",
      title: "Computer Networks",
      subtitle: "TCP 3-Way Handshake & Congestion Control",
      date: "1 week ago",
      duration: "45 mins",
      subject: "Networks",
      icon: "network",
      notesReady: true,
      quizReady: true,
      quizScore: 88,
      saved: true
    }
  ]
};

// Initialize on DOM ready
document.addEventListener('DOMContentLoaded', () => {
  initLucide();
  initSidebarToggle();
  initActiveNavLink();
  initSearchShortcut();
});

// Initialize Lucide Icons
function initLucide() {
  if (window.lucide && typeof window.lucide.createIcons === 'function') {
    window.lucide.createIcons();
  }
}

// Mobile Sidebar Drawer Toggle
function initSidebarToggle() {
  const toggleBtn = document.getElementById('sidebar-toggle-btn');
  const sidebar = document.getElementById('app-sidebar');
  const overlay = document.getElementById('sidebar-overlay');

  if (toggleBtn && sidebar) {
    toggleBtn.addEventListener('click', () => {
      sidebar.classList.toggle('mobile-open');
      if (overlay) overlay.classList.toggle('active');
    });
  }

  if (overlay && sidebar) {
    overlay.addEventListener('click', () => {
      sidebar.classList.remove('mobile-open');
      overlay.classList.remove('active');
    });
  }
}

// Active Nav Link detection
function initActiveNavLink() {
  const currentPath = window.location.pathname;
  const pageName = currentPath.substring(currentPath.lastIndexOf('/') + 1) || 'index.html';

  const navLinks = document.querySelectorAll('.sidebar-nav .nav-item');
  navLinks.forEach(link => {
    const href = link.getAttribute('href');
    if (href === pageName || (pageName === 'index.html' && href === 'dashboard.html')) {
      link.classList.add('active');
    } else {
      link.classList.remove('active');
    }
  });
}

// Keyboard Search shortcut (Cmd+K / Ctrl+K)
function initSearchShortcut() {
  const searchInput = document.getElementById('global-search-input');
  window.addEventListener('keydown', (e) => {
    if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
      e.preventDefault();
      if (searchInput) {
        searchInput.focus();
        showToast("Quick search activated. Type any topic or lecture.", "info");
      }
    }
  });

  if (searchInput) {
    searchInput.addEventListener('keydown', (e) => {
      if (e.key === 'Enter') {
        const query = searchInput.value.trim();
        if (query) {
          showToast(`Searching for "${query}" across your notes...`, "info");
          setTimeout(() => {
            window.location.href = `notes.html?search=${encodeURIComponent(query)}`;
          }, 450);
        }
      }
    });
  }
}

// Global Toast System
function showToast(message, type = 'info', duration = 3200) {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;

  const iconName = type === 'success' ? 'check-circle' : type === 'warning' ? 'alert-circle' : 'info';
  toast.innerHTML = `
    <i data-lucide="${iconName}" style="width: 18px; height: 18px; color: ${type === 'success' ? '#10b981' : '#38bdf8'};"></i>
    <div style="flex: 1;">${message}</div>
    <button style="background:none;border:none;color:#94a3b8;cursor:pointer;padding:2px;" onclick="this.parentElement.remove()">
      <i data-lucide="x" style="width: 14px; height: 14px;"></i>
    </button>
  `;

  container.appendChild(toast);
  initLucide();

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateY(10px)';
    toast.style.transition = 'all 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, duration);
}

// Copy to clipboard helper
function copyTextToClipboard(text, successMsg = "Copied to clipboard!") {
  if (navigator.clipboard) {
    navigator.clipboard.writeText(text).then(() => {
      showToast(successMsg, "success");
    }).catch(() => {
      fallbackCopy(text, successMsg);
    });
  } else {
    fallbackCopy(text, successMsg);
  }
}

function fallbackCopy(text, successMsg) {
  const textarea = document.createElement('textarea');
  textarea.value = text;
  document.body.appendChild(textarea);
  textarea.select();
  document.execCommand('copy');
  document.body.removeChild(textarea);
  showToast(successMsg, "success");
}

/* ===================================================================
   PLACEHOLDER API FUNCTIONS (FOR FUTURE FLASK / AI / MYSQL INTEGRATION)
   =================================================================== */

/**
 * Upload lecture document or audio file to backend.
 * Future: POST /api/v1/lectures/upload -> Flask -> MySQL
 */
async function uploadLecture(file, onProgress) {
  console.log(`[API placeholder] Uploading lecture: ${file.name} (${file.size} bytes)`);
  return new Promise((resolve) => {
    let progress = 0;
    const interval = setInterval(() => {
      progress += 20;
      if (onProgress) onProgress(progress);
      if (progress >= 100) {
        clearInterval(interval);
        resolve({
          lectureId: "lec-" + Date.now(),
          filename: file.name,
          status: "UPLOADED",
          message: "Lecture received and ready for AI processing"
        });
      }
    }, 280);
  });
}

/**
 * Trigger AI model note generation.
 * Future: POST /api/v1/notes/generate -> Flask -> Gemini/Whisper -> MySQL
 */
async function generateNotes(lectureId, options = {}) {
  console.log(`[API placeholder] Generating AI notes for lecture: ${lectureId}`, options);
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        success: true,
        lectureId: lectureId,
        sections: ["Overview", "Key Concepts", "Important Points", "Definitions", "Examples", "Exam Questions"],
        generatedAt: new Date().toISOString()
      });
    }, 1500);
  });
}

/**
 * Trigger AI MCQ Quiz Generation.
 * Future: POST /api/v1/quizzes/generate -> Flask -> AI Model -> MySQL
 */
async function generateQuiz(lectureId, options = {}) {
  console.log(`[API placeholder] Generating AI Quiz for: ${lectureId}`, options);
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        success: true,
        quizId: "quiz-" + Date.now(),
        totalQuestions: 10,
        timeLimitMinutes: 15
      });
    }, 1200);
  });
}

/**
 * Retrieve Quiz Evaluation & Results.
 * Future: GET /api/v1/quizzes/{quizId}/results -> Flask -> MySQL
 */
async function getQuizResults(quizId) {
  console.log(`[API placeholder] Fetching quiz results for: ${quizId}`);
  const stored = localStorage.getItem('smartlecture_last_result');
  if (stored) {
    try {
      return JSON.parse(stored);
    } catch (e) {
      console.error(e);
    }
  }
  return {
    quizId: quizId || "quiz-os-01",
    score: 84,
    total: 10,
    correct: 8,
    wrong: 2,
    accuracy: 80,
    timeTaken: "04:32",
    subject: "Operating Systems",
    topicsToRevise: ["Process Management", "Memory Management"]
  };
}

/**
 * Fetch Student Analytics.
 * Future: GET /api/v1/students/analytics -> Flask -> MySQL
 */
async function fetchStudentAnalytics() {
  console.log("[API placeholder] Fetching student learning analytics");
  return {
    totalLectures: 24,
    quizAttempts: 31,
    averageScore: 84,
    studyHours: 42.5,
    streakDays: 7
  };
}

/**
 * Save or Star a lecture note.
 * Future: POST /api/v1/notes/{lectureId}/save -> Flask -> MySQL
 */
async function saveLectureNotes(lectureId) {
  console.log(`[API placeholder] Toggling saved state for: ${lectureId}`);
  showToast("Lecture saved to your Starred collection!", "success");
}

// Export for module or global use
window.SmartLectureApp = {
  showToast,
  copyTextToClipboard,
  uploadLecture,
  generateNotes,
  generateQuiz,
  getQuizResults,
  fetchStudentAnalytics,
  saveLectureNotes,
  initLucide
};
