package com.example

data class TranslatedText(
    val en: String,
    val ms: String
) {
    fun get(isMs: Boolean): String = if (isMs) ms else en
}

data class Formula(
    val title: TranslatedText,
    val expression: String,
    val description: TranslatedText
)

data class NoteSection(
    val title: TranslatedText,
    val bulletPoints: List<TranslatedText>
)

data class PracticeQuestion(
    val id: String,
    val difficulty: Difficulty,
    val question: TranslatedText,
    val correctAnswer: String, // String representation of the answer (e.g. "x = 3", "2x + 1")
    val hint: TranslatedText,
    val stepByStep: TranslatedText
)

enum class Difficulty {
    EASY, MEDIUM, HARD;
    
    fun label(isMs: Boolean): String = when(this) {
        EASY -> if (isMs) "Senang" else "Easy"
        MEDIUM -> if (isMs) "Sederhana" else "Medium"
        HARD -> if (isMs) "Sukar" else "Hard"
    }
}

data class QuizQuestion(
    val id: String,
    val question: TranslatedText,
    val options: List<String>, // multiple choice options
    val correctIndex: Int,
    val explanation: TranslatedText,
    val difficulty: Difficulty = Difficulty.MEDIUM
)

data class Chapter(
    val number: Int,
    val form: Int, // 4 or 5
    val name: TranslatedText,
    val notes: List<NoteSection>,
    val formulas: List<Formula>,
    val practiceQuestions: List<PracticeQuestion>,
    val quizQuestions: List<QuizQuestion>
)

data class TopicProgress(
    val chapterNumber: Int,
    val form: Int,
    val chapterName: String,
    val answeredCorrectly: Int,
    val totalAttempts: Int
) {
    val accuracy: Float
        get() = if (totalAttempts == 0) 0f else (answeredCorrectly.toFloat() / totalAttempts) * 100f

    fun status(isMs: Boolean): String = when {
        totalAttempts == 0 -> if (isMs) "Belum Dicuba" else "Not Attempted"
        accuracy >= 80 -> if (isMs) "Cemerlang" else "Mastered"
        accuracy >= 50 -> if (isMs) "Sederhana" else "Improving"
        else -> if (isMs) "Perlu Fokus!" else "Weak Topic!"
    }
}
