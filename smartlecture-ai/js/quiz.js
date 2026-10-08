/**
 * SmartLecture AI - Interactive Quiz Engine
 * High-quality college-level questions, timed assessment, liquid glass option cards,
 * answer persistence without revealing until submission, score calculation and routing to result.html.
 */

const quizData = [
  {
    id: 1,
    topic: "Core Fundamentals",
    question: "What is the primary purpose of an operating system?",
    options: [
      { id: "A", text: "Manage computer hardware and software resources efficiently" },
      { id: "B", text: "Design websites and frontend user interfaces" },
      { id: "C", text: "Directly edit high-resolution videos and render 3D scenes" },
      { id: "D", text: "Compile high-level source code into bytecode" }
    ],
    correctAnswer: "A",
    explanation: "An Operating System (OS) is system software that manages computer hardware, software resources, and provides common services for computer programs."
  },
  {
    id: 2,
    topic: "Process Management",
    question: "Which CPU scheduling algorithm may lead to process starvation if long bursts arrive continuously?",
    options: [
      { id: "A", text: "Round Robin (RR) with fixed time quantum" },
      { id: "B", text: "Shortest Job Next / Shortest Job First (SJF)" },
      { id: "C", text: "First-Come, First-Served (FCFS)" },
      { id: "D", text: "Lottery Scheduling with aging" }
    ],
    correctAnswer: "B",
    explanation: "In Shortest Job First (SJF), if shorter processes keep arriving, longer processes may suffer indefinite delay, known as starvation."
  },
  {
    id: 3,
    topic: "Concurrency & Deadlocks",
    question: "Which of the following is NOT one of Coffman's four conditions required for a deadlock to occur?",
    options: [
      { id: "A", text: "Mutual Exclusion" },
      { id: "B", text: "Hold and Wait" },
      { id: "C", text: "Preemption Allowed" },
      { id: "D", text: "Circular Wait" }
    ],
    correctAnswer: "C",
    explanation: "The condition is 'No Preemption' (resources cannot be forcibly taken from a process holding them). Preemption prevents deadlocks."
  },
  {
    id: 4,
    topic: "Memory Management",
    question: "What is the phenomenon called when memory is allocated in fixed-size blocks and some portion remains unused within a block?",
    options: [
      { id: "A", text: "Internal Fragmentation" },
      { id: "B", text: "External Fragmentation" },
      { id: "C", text: "Thrashing" },
      { id: "D", text: "Page Faulting" }
    ],
    correctAnswer: "A",
    explanation: "Internal fragmentation occurs when allocated storage is larger than requested storage, leaving wasted space inside the assigned block."
  },
  {
    id: 5,
    topic: "Virtual Memory",
    question: "What hardware component accelerates virtual-to-physical address translation by caching recent page table mappings?",
    options: [
      { id: "A", text: "Arithmetic Logic Unit (ALU)" },
      { id: "B", text: "Translation Lookaside Buffer (TLB)" },
      { id: "C", text: "Direct Memory Access (DMA) controller" },
      { id: "D", text: "Instruction Register (IR)" }
    ],
    correctAnswer: "B",
    explanation: "A TLB is a high-speed hardware memory cache that stores recent mappings of virtual memory to physical memory addresses."
  },
  {
    id: 6,
    topic: "Virtual Memory",
    question: "Which page replacement algorithm is known as Belady's Anomaly susceptible?",
    options: [
      { id: "A", text: "Least Recently Used (LRU)" },
      { id: "B", text: "First-In, First-Out (FIFO)" },
      { id: "C", text: "Optimal Page Replacement (OPT)" },
      { id: "D", text: "Least Frequently Used (LFU)" }
    ],
    correctAnswer: "B",
    explanation: "FIFO can exhibit Belady's Anomaly, where increasing the number of page frames results in an increase in the number of page faults."
  },
  {
    id: 7,
    topic: "Process Synchronization",
    question: "What synchronization primitive uses two atomic operations: wait() [P] and signal() [V]?",
    options: [
      { id: "A", text: "Semaphore" },
      { id: "B", text: "Spinlock only" },
      { id: "C", text: "Socket" },
      { id: "D", text: "Pipe" }
    ],
    correctAnswer: "A",
    explanation: "Dijkstra introduced semaphores with atomic operations P (proberen/wait) and V (verhogen/signal) to manage concurrent access."
  },
  {
    id: 8,
    topic: "File Systems",
    question: "In UNIX-like operating systems, what data structure stores all metadata about a file (except its name and actual content)?",
    options: [
      { id: "A", text: "Superblock" },
      { id: "B", text: "Inode (Index Node)" },
      { id: "C", text: "File Allocation Table (FAT)" },
      { id: "D", text: "Directory Entry (Dentry) only" }
    ],
    correctAnswer: "B",
    explanation: "An inode contains attributes like permissions, ownership, size, timestamps, and pointers to disk blocks containing the data."
  },
  {
    id: 9,
    topic: "Virtual Memory",
    question: "What term describes the severe performance degradation when the OS spends more time paging data than executing instructions?",
    options: [
      { id: "A", text: "Aging" },
      { id: "B", text: "Thrashing" },
      { id: "C", text: "Starvation" },
      { id: "D", text: "Spooling" }
    ],
    correctAnswer: "B",
    explanation: "Thrashing occurs when the system's working set exceeds physical RAM, causing continuous page faults and near-zero CPU throughput."
  },
  {
    id: 10,
    topic: "Security & Protection",
    question: "What is the primary role of the CPU 'dual-mode' operation (User Mode vs Kernel Mode)?",
    options: [
      { id: "A", text: "To protect hardware and critical OS structures from rogue or buggy user programs" },
      { id: "B", text: "To double CPU clock frequency during computationally heavy calculations" },
      { id: "C", text: "To run 32-bit and 64-bit software at the exact same instant" },
      { id: "D", text: "To allow browser JavaScript to directly access RAM hardware registers" }
    ],
    correctAnswer: "A",
    explanation: "Dual-mode execution ensures that privileged instructions (like direct I/O or disabling interrupts) can only run in Kernel/Supervisor mode."
  }
];

let currentQuestionIndex = 0;
let userAnswers = {}; // { 0: 'A', 1: 'B', ... }
let timeRemainingSeconds = 15 * 60; // 15 mins
let timerInterval = null;

document.addEventListener('DOMContentLoaded', () => {
  initQuiz();
});

function initQuiz() {
  renderQuestion(currentQuestionIndex);
  renderPillNavigation();
  startTimer();

  const prevBtn = document.getElementById('quiz-prev-btn');
  const nextBtn = document.getElementById('quiz-next-btn');
  const submitBtn = document.getElementById('quiz-submit-btn');

  if (prevBtn) {
    prevBtn.addEventListener('click', () => {
      if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        renderQuestion(currentQuestionIndex);
        updateProgressUI();
      }
    });
  }

  if (nextBtn) {
    nextBtn.addEventListener('click', () => {
      if (currentQuestionIndex < quizData.length - 1) {
        currentQuestionIndex++;
        renderQuestion(currentQuestionIndex);
        updateProgressUI();
      }
    });
  }

  if (submitBtn) {
    submitBtn.addEventListener('click', () => {
      confirmAndSubmitQuiz();
    });
  }
}

function startTimer() {
  const timerEl = document.getElementById('quiz-timer');
  if (!timerEl) return;

  timerInterval = setInterval(() => {
    timeRemainingSeconds--;
    if (timeRemainingSeconds <= 0) {
      clearInterval(timerInterval);
      submitQuizResults();
      return;
    }

    const minutes = Math.floor(timeRemainingSeconds / 60);
    const seconds = timeRemainingSeconds % 60;
    timerEl.innerText = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
  }, 1000);
}

function renderQuestion(index) {
  const q = quizData[index];
  const questionNumEl = document.getElementById('quiz-question-number');
  const questionTextEl = document.getElementById('quiz-question-text');
  const questionTopicEl = document.getElementById('quiz-question-topic');
  const optionsContainer = document.getElementById('quiz-options-container');

  if (questionNumEl) questionNumEl.innerText = `Question ${index + 1} of ${quizData.length}`;
  if (questionTopicEl) questionTopicEl.innerText = q.topic;
  if (questionTextEl) questionTextEl.innerText = q.question;

  if (optionsContainer) {
    optionsContainer.innerHTML = '';
    q.options.forEach(opt => {
      const isSelected = userAnswers[index] === opt.id;
      const card = document.createElement('div');
      card.className = `option-card ${isSelected ? 'selected' : ''}`;
      card.innerHTML = `
        <div class="option-badge">${opt.id}</div>
        <div class="option-label">${opt.text}</div>
      `;

      card.addEventListener('click', () => {
        userAnswers[index] = opt.id;
        // Re-render options to update selected state without revealing correctness!
        renderQuestion(index);
        renderPillNavigation();
        updateProgressUI();
      });

      optionsContainer.appendChild(card);
    });
  }

  // Update button visibility
  const prevBtn = document.getElementById('quiz-prev-btn');
  const nextBtn = document.getElementById('quiz-next-btn');
  const submitBtn = document.getElementById('quiz-submit-btn');

  if (prevBtn) prevBtn.style.visibility = index === 0 ? 'hidden' : 'visible';
  if (nextBtn) nextBtn.style.display = index === quizData.length - 1 ? 'none' : 'inline-flex';
  if (submitBtn) submitBtn.style.display = index === quizData.length - 1 ? 'inline-flex' : 'none';

  updateProgressUI();
}

function renderPillNavigation() {
  const container = document.getElementById('quiz-pills-row');
  if (!container) return;

  container.innerHTML = '';
  quizData.forEach((_, idx) => {
    const btn = document.createElement('button');
    btn.className = `q-dot-btn ${idx === currentQuestionIndex ? 'active' : ''} ${userAnswers[idx] ? 'answered' : ''}`;
    btn.innerText = idx + 1;
    btn.addEventListener('click', () => {
      currentQuestionIndex = idx;
      renderQuestion(idx);
    });
    container.appendChild(btn);
  });
}

function updateProgressUI() {
  const progressBar = document.getElementById('quiz-progress-bar');
  if (progressBar) {
    const answeredCount = Object.keys(userAnswers).length;
    const pct = Math.round((answeredCount / quizData.length) * 100);
    progressBar.style.width = `${pct}%`;
  }
}

function confirmAndSubmitQuiz() {
  const answeredCount = Object.keys(userAnswers).length;
  const unansweredCount = quizData.length - answeredCount;

  if (unansweredCount > 0) {
    const proceed = confirm(`You have ${unansweredCount} unanswered question(s). Are you sure you want to submit your quiz?`);
    if (!proceed) return;
  }

  submitQuizResults();
}

function submitQuizResults() {
  clearInterval(timerInterval);

  let correctCount = 0;
  const detailedBreakdown = [];
  const topicMissedMap = {};

  quizData.forEach((q, idx) => {
    const selected = userAnswers[idx] || null;
    const isCorrect = selected === q.correctAnswer;
    if (isCorrect) {
      correctCount++;
    } else {
      topicMissedMap[q.topic] = (topicMissedMap[q.topic] || 0) + 1;
    }

    detailedBreakdown.push({
      questionId: q.id,
      question: q.question,
      topic: q.topic,
      selectedAnswer: selected,
      correctAnswer: q.correctAnswer,
      isCorrect: isCorrect,
      explanation: q.explanation,
      options: q.options
    });
  });

  const total = quizData.length;
  const wrongCount = total - correctCount;
  const accuracy = Math.round((correctCount / total) * 100);
  const totalSecondsElapsed = (15 * 60) - timeRemainingSeconds;
  const m = Math.floor(totalSecondsElapsed / 60);
  const s = totalSecondsElapsed % 60;
  const timeTakenStr = `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`;

  const topicsToRevise = Object.keys(topicMissedMap);
  if (topicsToRevise.length === 0) {
    topicsToRevise.push("Excellent work! All topics mastered.");
  }

  const resultPayload = {
    quizTitle: "Operating Systems: Virtual Memory & CPU Scheduling",
    subject: "Operating Systems",
    score: accuracy,
    accuracy: accuracy,
    total: total,
    correct: correctCount,
    wrong: wrongCount,
    timeTaken: timeTakenStr,
    topicsToRevise: topicsToRevise,
    detailedBreakdown: detailedBreakdown,
    submittedAt: new Date().toISOString()
  };

  localStorage.setItem('smartlecture_last_result', JSON.stringify(resultPayload));

  if (window.SmartLectureApp) {
    window.SmartLectureApp.showToast("Quiz submitted! Generating performance analytics...", "success");
  }

  setTimeout(() => {
    window.location.href = 'result.html';
  }, 600);
}
