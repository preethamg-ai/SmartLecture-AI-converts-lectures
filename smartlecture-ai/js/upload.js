/**
 * SmartLecture AI - Upload Module
 * Handles file drop, validation, progress simulation, sample lectures,
 * and dispatching to notes or quiz generation.
 */

document.addEventListener('DOMContentLoaded', () => {
  initUploadPage();
});

let currentSelectedFile = null;

function initUploadPage() {
  const dropzone = document.getElementById('upload-page-dropzone');
  const fileInput = document.getElementById('lecture-file-input');
  const samplePills = document.querySelectorAll('.sample-file-btn');

  // Check if file was staged from dashboard
  const stagedName = sessionStorage.getItem('staged_lecture_name');
  if (stagedName) {
    const stagedSize = sessionStorage.getItem('staged_lecture_size') || '8.4 MB';
    const stagedType = sessionStorage.getItem('staged_lecture_type') || 'application/pdf';
    sessionStorage.removeItem('staged_lecture_name');
    sessionStorage.removeItem('staged_lecture_size');
    sessionStorage.removeItem('staged_lecture_type');
    
    startFileUploadSimulation({
      name: stagedName,
      sizeStr: stagedSize,
      type: stagedType
    });
  }

  if (dropzone) {
    ['dragenter', 'dragover'].forEach(name => {
      dropzone.addEventListener(name, (e) => {
        e.preventDefault();
        dropzone.classList.add('drag-over');
      });
    });

    ['dragleave', 'drop'].forEach(name => {
      dropzone.addEventListener(name, (e) => {
        e.preventDefault();
        dropzone.classList.remove('drag-over');
      });
    });

    dropzone.addEventListener('drop', (e) => {
      if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
        processSelectedFile(e.dataTransfer.files[0]);
      }
    });

    dropzone.addEventListener('click', (e) => {
      if (e.target.tagName !== 'BUTTON' && !e.target.closest('button')) {
        if (fileInput) fileInput.click();
      }
    });
  }

  if (fileInput) {
    fileInput.addEventListener('change', (e) => {
      if (e.target.files && e.target.files[0]) {
        processSelectedFile(e.target.files[0]);
      }
    });
  }

  samplePills.forEach(pill => {
    pill.addEventListener('click', (e) => {
      e.stopPropagation();
      const filename = pill.getAttribute('data-filename');
      const sizeStr = pill.getAttribute('data-size');
      const type = pill.getAttribute('data-type');
      startFileUploadSimulation({
        name: filename,
        sizeStr: sizeStr,
        type: type
      });
    });
  });

  const btnNotes = document.getElementById('btn-generate-notes');
  const btnNotesQuiz = document.getElementById('btn-generate-notes-quiz');

  if (btnNotes) {
    btnNotes.addEventListener('click', () => {
      triggerNoteGeneration(false);
    });
  }

  if (btnNotesQuiz) {
    btnNotesQuiz.addEventListener('click', () => {
      triggerNoteGeneration(true);
    });
  }
}

function processSelectedFile(file) {
  const allowedExtensions = ['.pdf', '.docx', '.txt', '.mp3', '.wav'];
  const ext = file.name.substring(file.name.lastIndexOf('.')).toLowerCase();
  
  if (!allowedExtensions.includes(ext)) {
    if (window.SmartLectureApp) {
      window.SmartLectureApp.showToast(`Unsupported format ${ext}. Please upload PDF, DOCX, TXT, MP3, or WAV.`, 'warning');
    }
    return;
  }

  const sizeStr = (file.size / (1024 * 1024)).toFixed(2) + ' MB';
  startFileUploadSimulation({
    name: file.name,
    sizeStr: sizeStr,
    type: file.type || ext.toUpperCase().replace('.', '') + ' File'
  });
}

function startFileUploadSimulation(fileData) {
  currentSelectedFile = fileData;
  const progressCard = document.getElementById('upload-progress-card');
  const fileNameEl = document.getElementById('selected-file-name');
  const fileSizeEl = document.getElementById('selected-file-size');
  const fileTypeEl = document.getElementById('selected-file-type');
  const progressTrackFill = document.getElementById('upload-progress-fill');
  const progressPercentEl = document.getElementById('upload-progress-percent');
  const progressStatusEl = document.getElementById('upload-status-text');
  const actionsWrap = document.getElementById('upload-actions-wrap');

  if (!progressCard) return;

  progressCard.classList.add('active');
  fileNameEl.innerText = fileData.name;
  fileSizeEl.innerText = fileData.sizeStr;
  fileTypeEl.innerText = fileData.type.toUpperCase();
  actionsWrap.style.display = 'none';

  let progress = 0;
  progressTrackFill.style.width = '0%';
  progressPercentEl.innerText = '0%';
  progressStatusEl.innerText = 'Uploading lecture file to AI engine...';

  const phases = [
    { threshold: 30, text: 'Uploading & verifying checksum...' },
    { threshold: 60, text: 'Extracting audio & semantic text chunks...' },
    { threshold: 85, text: 'Generating high-accuracy transcription & structure...' },
    { threshold: 100, text: 'Ready! Choose your AI generation mode.' }
  ];

  const timer = setInterval(() => {
    progress += Math.floor(Math.random() * 12) + 6;
    if (progress > 100) progress = 100;

    progressTrackFill.style.width = `${progress}%`;
    progressPercentEl.innerText = `${progress}%`;

    const currentPhase = phases.find(p => progress <= p.threshold) || phases[phases.length - 1];
    progressStatusEl.innerText = currentPhase.text;

    if (progress >= 100) {
      clearInterval(timer);
      actionsWrap.style.display = 'flex';
      if (window.SmartLectureApp) {
        window.SmartLectureApp.showToast('Lecture uploaded & indexed successfully!', 'success');
      }
    }
  }, 180);
}

function triggerNoteGeneration(includeQuiz) {
  if (window.SmartLectureApp) {
    window.SmartLectureApp.showToast(
      includeQuiz ? 'Synthesizing lecture notes and compiling MCQ quiz...' : 'Synthesizing comprehensive AI lecture notes...',
      'info'
    );
  }

  // Placeholder API call integration
  if (window.SmartLectureApp && window.SmartLectureApp.generateNotes) {
    window.SmartLectureApp.generateNotes("lecture-uploaded", { includeQuiz });
  }

  setTimeout(() => {
    if (includeQuiz) {
      window.location.href = 'quiz.html?fromUpload=true';
    } else {
      window.location.href = 'notes.html?newLecture=true';
    }
  }, 900);
}
