package com.example

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPrefs: SharedPreferences = application.getSharedPreferences(
        "spm_add_maths_helper_prefs",
        Context.MODE_PRIVATE
    )

    // Global Language configuration: true = BM (Malay), false = EN (English)
    private val _isMalaySelected = MutableStateFlow(true)
    val isMalaySelected: StateFlow<Boolean> = _isMalaySelected.asStateFlow()

    // Global Dark Theme preference: true = Dark, false = Light
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    // Main Tab state: 0 = Syllabus, 1 = AI Solver, 2 = Quiz, 3 = Analytics
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    // Selected Form filter for Syllabus Tab: 4 or 5
    private val _selectedSyllabusForm = MutableStateFlow(4)
    val selectedSyllabusForm: StateFlow<Int> = _selectedSyllabusForm.asStateFlow()

    // Currently viewed Chapter details (null means browsing syllabus list)
    private val _activeChapter = MutableStateFlow<Chapter?>(null)
    val activeChapter: StateFlow<Chapter?> = _activeChapter.asStateFlow()

    // Practice Question stats state
    // Key: question_id, Value: Triple(studentAnswerText, isChecked, isCorrect)
    private val _practiceAnswers = MutableStateFlow<Map<String, Triple<String, Boolean, Boolean>>>(emptyMap())
    val practiceAnswers: StateFlow<Map<String, Triple<String, Boolean, Boolean>>> = _practiceAnswers.asStateFlow()

    // Map to keep track of expanded step-by-step states for Practice questions
    private val _expandedWorkings = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val expandedWorkings: StateFlow<Map<String, Boolean>> = _expandedWorkings.asStateFlow()

    // --- AI SOLVER AND CHECKER ---
    private val _aiSelectedChapter = MutableStateFlow(SyllabusData.chapters[0])
    val aiSelectedChapter: StateFlow<Chapter> = _aiSelectedChapter.asStateFlow()

    private val _aiProblemInput = MutableStateFlow("")
    val aiProblemInput: StateFlow<String> = _aiProblemInput.asStateFlow()

    private val _aiAnswerInput = MutableStateFlow("")
    val aiAnswerInput: StateFlow<String> = _aiAnswerInput.asStateFlow()

    private val _aiResponseText = MutableStateFlow("")
    val aiResponseText: StateFlow<String> = _aiResponseText.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // --- QUIZ STATE MACHINE ---
    private val _quizFormChoice = MutableStateFlow<Int?>(null) // null = selection screen
    val quizFormChoice: StateFlow<Int?> = _quizFormChoice.asStateFlow()

    private val _activeQuizQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val activeQuizQuestions: StateFlow<List<QuizQuestion>> = _activeQuizQuestions.asStateFlow()

    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _quizSelectedAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap()) // map of questionIndex -> optionIndex
    val quizSelectedAnswers: StateFlow<Map<Int, Int>> = _quizSelectedAnswers.asStateFlow()

    private val _isQuizSubmitted = MutableStateFlow(false)
    val isQuizSubmitted: StateFlow<Boolean> = _isQuizSubmitted.asStateFlow()

    // --- PERFORMANCE ANALYTICS STATE ---
    private val _topicProgresses = MutableStateFlow<List<TopicProgress>>(emptyList())
    val topicProgresses: StateFlow<List<TopicProgress>> = _topicProgresses.asStateFlow()

    init {
        // Load language preference (defaults to true = Bahasa Melayu)
        _isMalaySelected.value = sharedPrefs.getBoolean("is_malay_selected", true)
        _isDarkTheme.value = sharedPrefs.getBoolean("is_dark_theme", false)
        loadTopicProgresses()
    }

    fun setMalaySelected(selected: Boolean) {
        _isMalaySelected.value = selected
        sharedPrefs.edit().putBoolean("is_malay_selected", selected).apply()
        loadTopicProgresses()
    }

    fun setDarkTheme(enabled: Boolean) {
        _isDarkTheme.value = enabled
        sharedPrefs.edit().putBoolean("is_dark_theme", enabled).apply()
    }

    fun selectTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
        if (tabIndex == 3) {
            loadTopicProgresses()
        }
    }

    fun selectSyllabusForm(form: Int) {
        _selectedSyllabusForm.value = form
    }

    fun openChapter(chapter: Chapter?) {
        _activeChapter.value = chapter
        if (chapter != null) {
            // clear specific practice draft mappings if desired, or keep them
        }
    }

    fun updatePracticeInput(questionId: String, text: String) {
        val current = _practiceAnswers.value.toMutableMap()
        val triple = current[questionId] ?: Triple("", false, false)
        current[questionId] = Triple(text, false, triple.third)
        _practiceAnswers.value = current
    }

    fun checkPracticeAnswer(question: PracticeQuestion) {
        val current = _practiceAnswers.value.toMutableMap()
        val userInput = current[question.id]?.first?.trim() ?: ""
        
        // Basic mathematical matching: ignore spaces, case sensitivity, extra decimal zeros
        val cleanUser = userInput.replace(" ", "").lowercase()
        val cleanCorrect = question.correctAnswer.replace(" ", "").lowercase()
        
        val isCorrect = cleanUser == cleanCorrect || 
                     (try { cleanUser.toDouble() == cleanCorrect.toDouble() } catch (e: Exception) { false })

        current[question.id] = Triple(userInput, true, isCorrect)
        _practiceAnswers.value = current

        // Track correctness locally for analytics
        recordTopicAttempt(question.id, isCorrect)
    }

    fun toggleWorkingExpanded(questionId: String) {
        val current = _expandedWorkings.value.toMutableMap()
        current[questionId] = !(current[questionId] ?: false)
        _expandedWorkings.value = current
    }

    // --- AI Solver Call ---
    fun selectAiChapter(chapter: Chapter) {
        _aiSelectedChapter.value = chapter
    }

    fun updateAiProblem(input: String) {
        _aiProblemInput.value = input
    }

    fun updateAiAnswer(input: String) {
        _aiAnswerInput.value = input
    }

    fun clearAiSolver() {
        _aiProblemInput.value = ""
        _aiAnswerInput.value = ""
        _aiResponseText.value = ""
        _isAiLoading.value = false
    }

    fun runAiSolver() {
        if (_aiProblemInput.value.trim().isEmpty()) return
        
        _isAiLoading.value = true
        _aiResponseText.value = mLoadingIndicator()

        viewModelScope.launch {
            val response = GeminiClient.solveAndExplain(
                chapterName = _aiSelectedChapter.value.name.get(_isMalaySelected.value),
                problemDescription = _aiProblemInput.value,
                studentAnswer = _aiAnswerInput.value,
                preferMs = _isMalaySelected.value
            )
            _aiResponseText.value = response
            _isAiLoading.value = false
        }
    }

    private fun mLoadingIndicator(): String = if (_isMalaySelected.value) {
        "Menghubungi SPM Math Tutor AI... Sila tunggu sebentar sementara saya mengira dan menyelesaikan masalah langkah-demi-langkah."
    } else {
        "Contacting SPM Math Tutor AI... Please wait while I calculate and solve your question step-by-step."
    }

    // --- QUIZ ENGINE ---
    private val _quizDifficultyChoice = MutableStateFlow<Difficulty?>(null)
    val quizDifficultyChoice: StateFlow<Difficulty?> = _quizDifficultyChoice.asStateFlow()

    fun startQuiz(form: Int, difficulty: Difficulty? = null) {
        _quizFormChoice.value = form
        _quizDifficultyChoice.value = difficulty
        _isQuizSubmitted.value = false
        _quizSelectedAnswers.value = emptyMap()
        _currentQuizIndex.value = 0

        val formChapters = SyllabusData.chapters.filter { it.form == form }
        var pool = formChapters.flatMap { it.quizQuestions }
        if (difficulty != null) {
            pool = pool.filter { it.difficulty == difficulty }
        }
        
        // Safe fallback in case a particular difficulty level has fewer questions than expected
        val selectedPool = if (pool.isNotEmpty()) pool else formChapters.flatMap { it.quizQuestions }
        _activeQuizQuestions.value = selectedPool.shuffled(Random(System.currentTimeMillis())).take(5)
    }

    fun quitQuiz() {
        _quizFormChoice.value = null
        _quizDifficultyChoice.value = null
        _activeQuizQuestions.value = emptyList()
        _quizSelectedAnswers.value = emptyMap()
        _isQuizSubmitted.value = false
    }

    fun selectQuizOption(questionIndex: Int, optionIndex: Int) {
        if (_isQuizSubmitted.value) return // read-only after submission
        val current = _quizSelectedAnswers.value.toMutableMap()
        current[questionIndex] = optionIndex
        _quizSelectedAnswers.value = current
    }

    fun moveQuizIndex(delta: Int) {
        val nextIndex = _currentQuizIndex.value + delta
        if (nextIndex in 0 until _activeQuizQuestions.value.size) {
            _currentQuizIndex.value = nextIndex
        }
    }

    fun submitQuiz() {
        if (_isQuizSubmitted.value) return
        _isQuizSubmitted.value = true

        // Update Topic Progresses / Local Analytics based on inputs
        _activeQuizQuestions.value.forEachIndexed { index, question ->
            val selected = _quizSelectedAnswers.value[index]
            val isCorrect = selected == question.correctIndex
            
            // Find which chapter this question belonged to
            val originalChapter = SyllabusData.chapters.find { ch ->
                ch.quizQuestions.any { q -> q.id == question.id }
            }
            originalChapter?.let { ch ->
                val chapKey = "f${ch.form}_c${ch.number}"
                val attempts = sharedPrefs.getInt("attempts_$chapKey", 0) + 1
                val correct = sharedPrefs.getInt("correct_$chapKey", 0) + (if (isCorrect) 1 else 0)
                
                sharedPrefs.edit()
                    .putInt("attempts_$chapKey", attempts)
                    .putInt("correct_$chapKey", correct)
                    .apply()
            }
        }
        loadTopicProgresses()
    }

    // --- TRACKING AND STORAGE HELPER ---
    private fun recordTopicAttempt(questionId: String, isCorrect: Boolean) {
        // Find which chapter this practice question belonged to
        val findChapter = SyllabusData.chapters.find { ch ->
            ch.practiceQuestions.any { p -> p.id == questionId }
        }
        findChapter?.let { ch ->
            val chapKey = "f${ch.form}_c${ch.number}"
            val attempts = sharedPrefs.getInt("attempts_$chapKey", 0) + 1
            val correct = sharedPrefs.getInt("correct_$chapKey", 0) + (if (isCorrect) 1 else 0)
            
            sharedPrefs.edit()
                .putInt("attempts_$chapKey", attempts)
                .putInt("correct_$chapKey", correct)
                .apply()
        }
    }

    fun loadTopicProgresses() {
        val progresses = SyllabusData.chapters.map { ch ->
            val chapKey = "f${ch.form}_c${ch.number}"
            val attempts = sharedPrefs.getInt("attempts_$chapKey", 0)
            val correct = sharedPrefs.getInt("correct_$chapKey", 0)
            TopicProgress(
                chapterNumber = ch.number,
                form = ch.form,
                chapterName = ch.name.get(_isMalaySelected.value),
                answeredCorrectly = correct,
                totalAttempts = attempts
            )
        }
        _topicProgresses.value = progresses
    }

    fun resetTracker() {
        sharedPrefs.edit().clear().apply()
        // restore english/malay selection
        sharedPrefs.edit().putBoolean("is_malay_selected", _isMalaySelected.value).apply()
        
        _practiceAnswers.value = emptyMap()
        _expandedWorkings.value = emptyMap()
        loadTopicProgresses()
    }
}
