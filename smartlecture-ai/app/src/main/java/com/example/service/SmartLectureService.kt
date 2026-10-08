package com.example.service

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay

/**
 * Service providing smart lecture operations.
 * Designed with clean architecture so it can be seamlessly connected to
 * a Flask/Python backend, Gemini AI API, and MySQL persistence.
 */
class SmartLectureService {

    private val _lectures = MutableStateFlow<List<Lecture>>(initialLectures())
    val lectures: StateFlow<List<Lecture>> = _lectures.asStateFlow()

    private val _notes = MutableStateFlow<List<NoteDocument>>(initialNotes())
    val notes: StateFlow<List<NoteDocument>> = _notes.asStateFlow()

    private val _quizzes = MutableStateFlow<Map<String, Quiz>>(initialQuizzes())
    val quizzes: StateFlow<Map<String, Quiz>> = _quizzes.asStateFlow()

    private val _userProfile = MutableStateFlow(initialProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _analytics = MutableStateFlow(initialAnalytics())
    val analytics: StateFlow<AnalyticsData> = _analytics.asStateFlow()

    /**
     * Placeholder: uploadLecture()
     * Simulates uploading a lecture file (PDF, DOCX, TXT, MP3, WAV) and creating a record.
     */
    suspend fun uploadLecture(
        fileName: String,
        fileType: String,
        fileSize: String,
        subject: String
    ): Lecture {
        delay(600) // Simulating network handshake
        val id = "lec_${System.currentTimeMillis()}"
        val cleanTitle = fileName.substringBeforeLast(".")
        val newLecture = Lecture(
            id = id,
            title = cleanTitle.ifBlank { "Untitled Lecture" },
            subject = subject.ifBlank { "Computer Science" },
            duration = "45 min",
            date = "Just now",
            fileType = fileType.uppercase(),
            fileSize = fileSize,
            notesGenerated = false,
            quizGenerated = false,
            summary = "AI processing in progress for $cleanTitle...",
            conceptsCount = 14,
            topicsCount = 5,
            progressPercent = 0.2f
        )
        _lectures.value = listOf(newLecture) + _lectures.value
        return newLecture
    }

    /**
     * Placeholder: generateNotes()
     * Simulates AI synthesis of lecture content into structured smart notes.
     */
    suspend fun generateNotes(lectureId: String): NoteDocument {
        delay(800)
        val existing = _notes.value.find { it.lectureId == lectureId }
        if (existing != null) return existing

        val lecture = _lectures.value.find { it.id == lectureId }
        val title = lecture?.title ?: "New Lecture Notes"
        val subject = lecture?.subject ?: "Computer Science"

        val generatedDoc = NoteDocument(
            id = "note_${System.currentTimeMillis()}",
            lectureId = lectureId,
            title = title,
            subject = subject,
            lastEdited = "Just now",
            summary = "$title provides foundational knowledge on core concepts, system interaction, algorithms, and practical implementations.",
            overview = "This lecture establishes fundamental principles, theoretical guarantees, and structural trade-offs essential for mastery and examination prep.",
            keyConcepts = listOf(
                "Core Architecture & Resource Distribution",
                "State Transitions & Synchronization primitives",
                "Fault Tolerance and Error Recovery protocols",
                "Performance optimization & Bottleneck elimination"
            ),
            importantPoints = listOf(
                "Critical section protection requires atomic hardware operations.",
                "Deadlock condition necessitates four simultaneous constraints (Coffman criteria).",
                "Memory hierarchy balances access latency against storage capacity."
            ),
            definitions = listOf(
                DefinitionItem("Kernel Mode", "Privileged execution level permitting direct access to hardware instructions and memory pages."),
                DefinitionItem("Context Switch", "Mechanism of storing the state of an active process so it can be restored and resume execution later."),
                DefinitionItem("Paging", "Memory management scheme by which a computer stores and retrieves data from secondary storage for use in main memory.")
            ),
            examples = listOf(
                "Banker's Algorithm: Resource allocation and deadlock avoidance simulation.",
                "Round Robin Scheduling: Time slice allocation with queue rotation."
            ),
            examTopics = listOf(
                "Process Synchronization using Semaphores",
                "Virtual Memory & Page Replacement Algorithms (LRU vs FIFO)",
                "File Allocation Table vs Inode Structure"
            ),
            faqs = listOf(
                FaqItem("What is the difference between preemptive and non-preemptive scheduling?", "Preemptive scheduling permits interruptible task switching by timer or priority; non-preemptive requires the task to voluntarily yield control."),
                FaqItem("Why is Thrashing dangerous?", "Thrashing occurs when the system spends more time paging than executing instructions, dropping CPU utilization to near zero.")
            ),
            pageCount = 6,
            isFavorite = false
        )

        _notes.value = listOf(generatedDoc) + _notes.value

        // Update lecture state
        _lectures.value = _lectures.value.map {
            if (it.id == lectureId) it.copy(notesGenerated = true, progressPercent = 0.8f) else it
        }

        return generatedDoc
    }

    /**
     * Placeholder: generateQuiz()
     * Simulates generating personalized assessment questions from lecture concepts.
     */
    suspend fun generateQuiz(lectureId: String): Quiz {
        delay(700)
        val existing = _quizzes.value[lectureId]
        if (existing != null) return existing

        val lecture = _lectures.value.find { it.id == lectureId }
        val title = lecture?.title ?: "Assessment Quiz"
        val subject = lecture?.subject ?: "Core Topics"

        val generatedQuiz = Quiz(
            id = "quiz_${System.currentTimeMillis()}",
            lectureId = lectureId,
            title = "$title Mastery Quiz",
            subject = subject,
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    question = "What is the primary role of an Operating System kernel?",
                    options = listOf(
                        "Manage computer hardware and software resources",
                        "Design client-facing responsive web interfaces",
                        "Render 3D graphical vector animations",
                        "Compress audio streams into MP3 format"
                    ),
                    correctAnswerIndex = 0,
                    explanation = "The kernel is the core component that manages system resources and abstracts hardware.",
                    topic = "System Architecture"
                ),
                QuizQuestion(
                    id = "q2",
                    question = "Which condition is NOT one of the Coffman conditions for deadlock?",
                    options = listOf(
                        "Mutual Exclusion",
                        "Hold and Wait",
                        "Preemption Allowed",
                        "Circular Wait"
                    ),
                    correctAnswerIndex = 2,
                    explanation = "'No Preemption' is the required condition; allowing preemption prevents deadlock.",
                    topic = "Deadlock"
                ),
                QuizQuestion(
                    id = "q3",
                    question = "In virtual memory systems, what does the TLB (Translation Lookaside Buffer) cache?",
                    options = listOf(
                        "CPU register values",
                        "Page table translations (Virtual to Physical addresses)",
                        "Unsaved hard disk sector buffers",
                        "Network socket descriptors"
                    ),
                    correctAnswerIndex = 1,
                    explanation = "The TLB caches recent virtual-to-physical address mappings to accelerate memory lookup.",
                    topic = "Memory Management"
                ),
                QuizQuestion(
                    id = "q4",
                    question = "Which scheduling algorithm can potentially cause task starvation without aging?",
                    options = listOf(
                        "Round Robin",
                        "Priority Scheduling",
                        "First-Come, First-Served",
                        "Shortest Job First with Preemption (SRTF)"
                    ),
                    correctAnswerIndex = 1,
                    explanation = "In Priority Scheduling, lower-priority tasks can starve indefinitely if higher-priority tasks keep arriving.",
                    topic = "Process Scheduling"
                ),
                QuizQuestion(
                    id = "q5",
                    question = "What occurs during high page fault frequency leading to thrashing?",
                    options = listOf(
                        "System executes instructions at maximum throughput",
                        "System spends disproportionate time swapping pages rather than computing",
                        "Cache hits approach 100%",
                        "Kernel safely shuts down all processes"
                    ),
                    correctAnswerIndex = 1,
                    explanation = "Thrashing collapses CPU efficiency as constant page swaps overwhelm I/O bandwidth.",
                    topic = "Virtual Memory"
                )
            )
        )

        _quizzes.value = _quizzes.value + (lectureId to generatedQuiz)

        _lectures.value = _lectures.value.map {
            if (it.id == lectureId) it.copy(quizGenerated = true, progressPercent = 1.0f) else it
        }

        return generatedQuiz
    }

    /**
     * Placeholder: submitQuiz()
     * Evaluates answers and generates AI-driven score & recommendations.
     */
    suspend fun submitQuiz(quizId: String, userAnswers: Map<Int, Int>): QuizResult {
        delay(600)
        // Find the quiz
        val quiz = _quizzes.value.values.find { it.id == quizId }
            ?: _quizzes.value["lec_1"]
            ?: initialQuizzes().values.first()

        var correct = 0
        val total = quiz.questions.size
        val understood = mutableListOf<String>()
        val revise = mutableListOf<String>()

        quiz.questions.forEachIndexed { index, q ->
            val chosen = userAnswers[index]
            if (chosen == q.correctAnswerIndex) {
                correct++
                if (!understood.contains(q.topic)) understood.add(q.topic)
            } else {
                if (!revise.contains(q.topic)) revise.add(q.topic)
            }
        }

        val percentage = ((correct.toFloat() / total) * 100).toInt()

        val recommendation = if (percentage >= 80) {
            "Excellent mastery! You have a solid grasp on core concepts. Try exploring advanced concurrency challenges and kernel scheduling."
        } else if (percentage >= 60) {
            "Good effort! We recommend revising Process Management and Virtual Memory before attempting the advanced assessment."
        } else {
            "Review foundational definitions and memory paging mechanisms. Spend 20 minutes with the AI Smart Notes summary."
        }

        return QuizResult(
            quizId = quizId,
            scorePercent = percentage,
            totalQuestions = total,
            correctCount = correct,
            incorrectCount = total - correct,
            timeTaken = "4m 18s",
            accuracy = percentage,
            understoodTopics = if (understood.isEmpty()) listOf("System Fundamentals") else understood,
            topicsToRevise = if (revise.isEmpty()) listOf("Advanced Scheduling") else revise,
            aiRecommendation = recommendation
        )
    }

    /**
     * Placeholder: getAnalytics()
     * Retrieves student progress, performance metrics, and subject breakdowns.
     */
    fun getAnalytics(): AnalyticsData {
        return _analytics.value
    }

    /**
     * Placeholder: askAI()
     * Interactive assistant that answers questions, simplifies topics, or generates questions.
     */
    suspend fun askAI(prompt: String, contextLectureId: String? = null): String {
        delay(800)
        val normalized = prompt.lowercase()
        return when {
            normalized.contains("simply") || normalized.contains("explain") ->
                "💡 **Simplified Explanation:**\nThink of the Operating System like a busy restaurant manager. The CPU is the chef, RAM is the kitchen prep table, and the disk is the storage pantry. The OS ensures orders get cooked without kitchen fights (race conditions) and nobody monopolizes the stove (deadlocks)!"

            normalized.contains("question") || normalized.contains("5 questions") || normalized.contains("quiz") ->
                "📝 **AI Practice Questions:**\n1. What is the fundamental difference between a Process and a Thread?\n2. Why do race conditions occur in multi-threaded programs?\n3. How does virtual memory protect processes from overwriting each other?\n4. What is the Belady's Anomaly in FIFO page replacement?\n5. Explain how semaphores enforce mutual exclusion."

            normalized.contains("summarize") || normalized.contains("summary") ->
                "📌 **Lecture Highlights:**\n• Key Takeaway: Resource synchronization is crucial for stable OS concurrency.\n• High-frequency exam topic: Coffman criteria for deadlock detection.\n• Next revision checkpoint: Page replacement algorithms."

            normalized.contains("study next") || normalized.contains("recommend") ->
                "🎯 **Recommended Next Step:**\nBased on your recent quiz scores, spend 15 minutes reviewing **Linked Lists & Tree Balancing** in Data Structures, followed by a quick 5-question recap."

            else ->
                "SmartLecture AI analyzed your question regarding \"$prompt\". Key takeaways: Review core state transitions, verify your memory allocation boundaries, and run a 5-question flash quiz to solidify retention!"
        }
    }

    /**
     * Placeholder: saveNote()
     * Toggles favorite/bookmark status of a smart note.
     */
    fun saveNote(noteId: String, isSaved: Boolean) {
        _notes.value = _notes.value.map {
            if (it.id == noteId) it.copy(isFavorite = isSaved) else it
        }
        _lectures.value = _lectures.value.map {
            if (it.id == noteId || "note_${it.id}" == noteId) it.copy(isFavorite = isSaved) else it
        }
    }

    /**
     * Global search across lectures, notes, questions, and topics.
     */
    fun searchAll(query: String): List<SearchResultItem> {
        if (query.isBlank()) return emptyList()
        val q = query.trim().lowercase()
        val results = mutableListOf<SearchResultItem>()

        // Search Lectures
        _lectures.value.filter {
            it.title.lowercase().contains(q) || it.subject.lowercase().contains(q)
        }.forEach {
            results.add(
                SearchResultItem(
                    id = "sr_lec_${it.id}",
                    title = it.title,
                    subtitle = "Lecture • ${it.subject} • ${it.duration}",
                    category = SearchCategory.LECTURE,
                    targetId = it.id
                )
            )
        }

        // Search Notes
        _notes.value.filter {
            it.title.lowercase().contains(q) || it.summary.lowercase().contains(q) ||
                    it.keyConcepts.any { c -> c.lowercase().contains(q) }
        }.forEach {
            results.add(
                SearchResultItem(
                    id = "sr_note_${it.id}",
                    title = "${it.title} Notes",
                    subtitle = "Smart Notes • ${it.pageCount} sections • ${it.subject}",
                    category = SearchCategory.NOTE,
                    targetId = it.id
                )
            )
        }

        // Search Topics / Questions
        _quizzes.value.values.forEach { quiz ->
            quiz.questions.filter {
                it.question.lowercase().contains(q) || it.topic.lowercase().contains(q)
            }.forEach {
                results.add(
                    SearchResultItem(
                        id = "sr_q_${it.id}",
                        title = it.question,
                        subtitle = "Quiz Question • Topic: ${it.topic}",
                        category = SearchCategory.QUESTION,
                        targetId = quiz.lectureId
                    )
                )
            }
        }

        return results
    }

    // Default Seed Data
    private fun initialLectures(): List<Lecture> = listOf(
        Lecture(
            id = "lec_1",
            title = "Operating Systems",
            subject = "Computer Science",
            duration = "42 min",
            date = "Today",
            fileType = "PDF",
            fileSize = "4.2 MB",
            notesGenerated = true,
            quizGenerated = true,
            summary = "Operating systems manage computer hardware and software resources, providing common services for computer programs through process scheduling, memory virtualization, and file systems.",
            conceptsCount = 18,
            topicsCount = 7,
            progressPercent = 1.0f,
            isFavorite = true
        ),
        Lecture(
            id = "lec_2",
            title = "Data Structures",
            subject = "Algorithms",
            duration = "35 min",
            date = "Yesterday",
            fileType = "DOCX",
            fileSize = "2.8 MB",
            notesGenerated = true,
            quizGenerated = false,
            summary = "Fundamental data structures including balanced binary search trees, hash tables, and graphs with worst-case asymptotic time complexity comparisons.",
            conceptsCount = 22,
            topicsCount = 9,
            progressPercent = 0.65f,
            isFavorite = true
        ),
        Lecture(
            id = "lec_3",
            title = "Java Programming",
            subject = "Software Eng",
            duration = "50 min",
            date = "3 days ago",
            fileType = "MP3",
            fileSize = "18.4 MB",
            notesGenerated = true,
            quizGenerated = true,
            summary = "Object-oriented design patterns, concurrency with synchronized monitors, generic type erasure, and stream pipelines in modern JVM environments.",
            conceptsCount = 16,
            topicsCount = 6,
            progressPercent = 1.0f,
            isFavorite = false
        ),
        Lecture(
            id = "lec_4",
            title = "Computer Networks",
            subject = "Networking",
            duration = "38 min",
            date = "5 days ago",
            fileType = "PDF",
            fileSize = "5.1 MB",
            notesGenerated = true,
            quizGenerated = true,
            summary = "TCP/IP 4-layer model, congestion control mechanisms (AIMD, Slow Start), BGP routing protocols, and subnet masking calculations.",
            conceptsCount = 20,
            topicsCount = 8,
            progressPercent = 0.9f,
            isFavorite = false
        ),
        Lecture(
            id = "lec_5",
            title = "Database Management",
            subject = "Information Systems",
            duration = "44 min",
            date = "Last week",
            fileType = "TXT",
            fileSize = "1.2 MB",
            notesGenerated = true,
            quizGenerated = true,
            summary = "Relational algebra, ACID transaction semantics, indexing structures (B+ trees), and normalization forms through Boyce-Codd (BCNF).",
            conceptsCount = 25,
            topicsCount = 10,
            progressPercent = 1.0f,
            isFavorite = true
        )
    )

    private fun initialNotes(): List<NoteDocument> = listOf(
        NoteDocument(
            id = "note_lec_1",
            lectureId = "lec_1",
            title = "Operating Systems",
            subject = "Computer Science",
            lastEdited = "Today • 2 hours ago",
            summary = "Operating systems manage computer hardware and software resources, providing common services for computer programs through process scheduling, memory management, and abstract interfaces.",
            overview = "The operating system functions as an intermediary between users and hardware. Primary goals are executing user programs efficiently, simplifying problem solving, and maximizing computing hardware utilization.",
            keyConcepts = listOf(
                "Process Management: Process control blocks, context switches, state queues (Ready, Running, Waiting).",
                "CPU Scheduling: Preemptive vs Non-preemptive algorithms (Round Robin, Multilevel Feedback Queues).",
                "Memory Virtualization: Page tables, TLB caching, segmentation, and demand paging.",
                "Storage & File Systems: Inodes, journaling file systems, disk scheduling (SCAN, C-LOOK)."
            ),
            importantPoints = listOf(
                "A context switch incurs direct CPU overhead where no productive user computation occurs.",
                "Deadlocks require all 4 Coffman conditions: Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait.",
                "Virtual memory allows execution of processes that are not completely in main memory.",
                "Critical sections must guarantee Mutual Exclusion, Progress, and Bounded Waiting."
            ),
            definitions = listOf(
                DefinitionItem("Kernel Mode", "Privileged processor execution mode enabling unrestrained direct hardware instruction access."),
                DefinitionItem("Context Switch", "Storing the register state of an active process to safely restore and resume execution later."),
                DefinitionItem("Thrashing", "A condition where the CPU is continuously occupied swapping virtual pages in and out with near-zero throughput."),
                DefinitionItem("Semaphore", "A synchronization variable providing atomic wait() and signal() operations to manage concurrent access.")
            ),
            examples = listOf(
                "Producer-Consumer Problem: Solved with two counting semaphores (empty, full) and one binary mutex.",
                "Banker's Algorithm: Verifies safe state by testing if maximum remaining request vectors can be satisfied."
            ),
            examTopics = listOf(
                "Page Replacement: LRU vs FIFO (explain Belady's anomaly)",
                "Deadlock Avoidance vs Deadlock Detection algorithms",
                "Process synchronization using Peterson's Algorithm and hardware TestAndSet"
            ),
            faqs = listOf(
                FaqItem(
                    "What is the difference between a process and a thread?",
                    "A process has its own isolated address space and descriptors. A thread shares memory, code, and resources within the parent process with lighter context switching."
                ),
                FaqItem(
                    "Why is Round Robin better for interactive systems?",
                    "Round Robin guarantees a bounded response time by rotating equal time quantum slices among all ready processes."
                )
            ),
            pageCount = 8,
            isFavorite = true
        ),
        NoteDocument(
            id = "note_lec_2",
            lectureId = "lec_2",
            title = "Data Structures",
            subject = "Algorithms",
            lastEdited = "Yesterday",
            summary = "Explores fundamental linear and non-linear data structures with algorithmic efficiency considerations and optimal tree balancing.",
            overview = "Understanding trade-offs between contiguous array representations and linked node structures across insertion, deletion, and search operations.",
            keyConcepts = listOf(
                "Tree Self-Balancing: AVL Tree rotation triggers and Red-Black tree coloring rules.",
                "Hash Collisions: Separate chaining vs Open addressing (Linear probing, Double hashing).",
                "Graph Traversals: Breadth-First Search (Queue) and Depth-First Search (Stack/Recursion)."
            ),
            importantPoints = listOf(
                "Binary search requires sorted arrays with O(1) random access.",
                "Hash tables provide average O(1) lookups, degrading to O(N) during high collision rates.",
                "AVL trees maintain strict balance factor in {-1, 0, 1} with faster lookup than Red-Black trees."
            ),
            definitions = listOf(
                DefinitionItem("Amortized Time", "Average time taken per operation over a worst-case sequence of operations."),
                DefinitionItem("Load Factor", "Ratio of number of stored elements to total bucket array size (alpha = n/k).")
            ),
            examples = listOf(
                "LRU Cache implementation utilizing Doubly-Linked List + Hash Map in O(1) time.",
                "Dijkstra's shortest path with Min-Priority Heap in O((V + E) log V)."
            ),
            examTopics = listOf(
                "AVL Tree rotations: LL, RR, LR, and RL cases",
                "Time complexities across sorting algorithms (Quicksort, Mergesort, Heapsort)"
            ),
            faqs = listOf(
                FaqItem(
                    "When should you prefer a Linked List over an Array?",
                    "When frequent insertions and deletions occur at the beginning or middle without requiring index-based random access."
                )
            ),
            pageCount = 10,
            isFavorite = true
        )
    )

    private fun initialQuizzes(): Map<String, Quiz> = mapOf(
        "lec_1" to Quiz(
            id = "quiz_lec_1",
            lectureId = "lec_1",
            title = "Operating Systems Assessment",
            subject = "Computer Science",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    question = "What is the main purpose of an operating system?",
                    options = listOf(
                        "Manage computer resources",
                        "Design websites",
                        "Edit videos",
                        "Create images"
                    ),
                    correctAnswerIndex = 0,
                    explanation = "An OS manages computer hardware, system calls, and executes application processes.",
                    topic = "OS Fundamentals"
                ),
                QuizQuestion(
                    id = "q2",
                    question = "Which scheduling algorithm assigns fixed time slices to ready processes?",
                    options = listOf(
                        "First-Come First-Served",
                        "Round Robin",
                        "Shortest Job First",
                        "Priority Scheduling"
                    ),
                    correctAnswerIndex = 1,
                    explanation = "Round Robin allocates equal time slices (quanta) cyclically among ready processes.",
                    topic = "CPU Scheduling"
                ),
                QuizQuestion(
                    id = "q3",
                    question = "Which is NOT one of the four Coffman conditions necessary for deadlock?",
                    options = listOf(
                        "Mutual exclusion",
                        "Hold and wait",
                        "Preemption permitted",
                        "Circular wait"
                    ),
                    correctAnswerIndex = 2,
                    explanation = "The condition is 'No preemption'. If preemption is permitted, deadlocks cannot occur.",
                    topic = "Deadlock"
                ),
                QuizQuestion(
                    id = "q4",
                    question = "What happens when a high page-fault rate causes continuous swapping?",
                    options = listOf(
                        "Segmentation fault",
                        "Thrashing",
                        "Pipelining",
                        "Starvation"
                    ),
                    correctAnswerIndex = 1,
                    explanation = "Thrashing occurs when the OS spends more time paging data than computing useful work.",
                    topic = "Virtual Memory"
                ),
                QuizQuestion(
                    id = "q5",
                    question = "Which memory entity maps virtual addresses to physical frame addresses quickly?",
                    options = listOf(
                        "TLB (Translation Lookaside Buffer)",
                        "L2 Cache Buffer",
                        "Accumulator Register",
                        "Instruction Pointer"
                    ),
                    correctAnswerIndex = 0,
                    explanation = "The TLB is an associative memory cache used to speed up virtual-to-physical address translation.",
                    topic = "Memory Management"
                )
            )
        )
    )

    private fun initialProfile(): UserProfile = UserProfile(
        name = "Alex Morgan",
        email = "alex.morgan@university.edu",
        institution = "School of Computing & Engineering",
        streakDays = 7,
        completedLectures = 24,
        quizAverage = 84,
        badges = listOf(
            AchievementBadge("b1", "7 Day Streak", "🔥", "Studied every day for a full week"),
            AchievementBadge("b2", "25 Lectures", "📚", "Analyzed and synthesized over 25 lectures"),
            AchievementBadge("b3", "Quiz Master", "🧠", "Scored above 80% on 15 quizzes in a row"),
            AchievementBadge("b4", "Rapid Learner", "⚡", "Generated notes & completed quiz under 10 minutes")
        )
    )

    private fun initialAnalytics(): AnalyticsData = AnalyticsData(
        totalStudyTime = "38.5 hrs",
        quizAccuracy = 84,
        lecturesCompleted = 24,
        learningStreak = 7,
        weeklyScores = listOf(
            WeeklyScorePoint("Mon", 0.76f),
            WeeklyScorePoint("Tue", 0.82f),
            WeeklyScorePoint("Wed", 0.79f),
            WeeklyScorePoint("Thu", 0.88f),
            WeeklyScorePoint("Fri", 0.85f),
            WeeklyScorePoint("Sat", 0.91f),
            WeeklyScorePoint("Sun", 0.84f)
        ),
        subjectProficiency = listOf(
            SubjectProficiency("Java Programming", 92, 8),
            SubjectProficiency("Data Structures", 85, 6),
            SubjectProficiency("Computer Networks", 80, 4),
            SubjectProficiency("Operating Systems", 78, 6)
        ),
        aiAnalysis = "You are improving in Java and Data Structures. Your quiz accuracy increased by 12% this week. I recommend revising Memory Management and Process Synchronization next.",
        recommendedStudyTime = "30 minutes"
    )

    companion object {
        val instance = SmartLectureService()
    }
}
