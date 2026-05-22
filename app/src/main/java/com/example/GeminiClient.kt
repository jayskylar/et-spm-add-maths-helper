package com.example

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// --- Data Classes for Moshi ---
data class Part(
    val text: String? = null
)

data class Content(
    val parts: List<Part>
)

data class GenerateContentRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null
)

data class Candidate(
    val content: Content?
)

data class GenerateContentResponse(
    val candidates: List<Candidate>?
)

// --- Retrofit API Service ---
interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    suspend fun solveAndExplain(
        chapterName: String,
        problemDescription: String,
        studentAnswer: String,
        preferMs: Boolean
    ): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey.trim().isEmpty()) {
            return if (preferMs) {
                "⚠️ [RALAT: KUNCI API GEMINI HILANG]\n\n" +
                "Kunci API Gemini tidak ditemui! Ikuti langkah ini untuk mengkonfigurasikannya:\n\n" +
                "1. Klik butang 'Secrets' di bahagian atas panel AI Studio.\n" +
                "2. Tambah rahsia (secret) baharu:\n" +
                "   • Name: GEMINI_API_KEY\n" +
                "   • Value: [Masukkan Kunci API Gemini anda dari Google AI Studio]\n" +
                "3. Klik 'Save' dan mulakan semula aplikasi anda.\n\n" +
                "Sementara itu, anda boleh menggunakan nota interaktif, bank formula, dan soalan latihan SPM tempatan secara luar talian!"
            } else {
                "⚠️ [ERROR: GEMINI API KEY MISSING]\n\n" +
                "The Gemini API Key is missing! Please follow these configuration steps:\n\n" +
                "1. Click the 'Secrets' button on the top panel of Google AI Studio.\n" +
                "2. Add a new secret:\n" +
                "   • Name: GEMINI_API_KEY\n" +
                "   • Value: [Paste your Gemini API Key here]\n" +
                "3. Click 'Save' and restart/recompile your app.\n\n" +
                "In the meantime, feel free to use offline study resources, formula banks, and quiz/practice questions available in the app!"
            }
        }

        // Custom Prompt constructed with the required answer format (Given, Formula, Working steps, Final answer, Common mistake)
        val systemPrompt = """
            You are "SPM Add Maths Helper AI Version 2", an expert Malaysian SPM Additional Mathematics tutor.
            Your task is to review the student's problem, verify their final answer (if provided), and generate a detailed step-by-step solution.
            
            You MUST strictly structure your response in this exact format, with clear markdown headers:
            
            ### 1. GIVEN / DIBERI:
            [Identify and list down each variable, equation, coordinates, or mathematical facts given in the problem]
            
            ### 2. FORMULA / RUMUS:
            [State the relevant mathematical formulas according to KSSM SPM syllabus, e.g., quadratic formula, integration formula]
            
            ### 3. WORKING STEPS / JALAN KERJA:
            [Show the clear, step-by-step calculation. Label each step, e.g., Step 1 / Langkah 1, Step 2 / Langkah 2. Explain clearly in both English and Bahasa Melayu where appropriate]
            
            ### 4. FINAL ANSWER / JAWAPAN AKHIR:
            [State the final verified answer clearly. Use double bold Markdown to highlight it, e.g. **Final Answer: x = 5**]
            
            ### 5. COMMON MISTAKE / KESILAPAN BIASA:
            [Describe typical mistakes Malaysian students make in SPM for this topic, e.g., wrong sign transformation, forgot +c in integration, etc., in both English and Bahasa Melayu]
            
            Follow these extra rules:
            1. Write your response primarily in ${if (preferMs) "Bahasa Melayu, but provide English translations in brackets where necessary" else "English, but provide Bahasa Melayu translations in brackets where necessary"}.
            2. If the student provided a final answer, declare immediately in the beginning sections whether their answer is CORRECT or INCORRECT with encouraging student-friendly feedback.
            3. Present your step-by-step solution mathematically, with clear equations, neat workings, and logical progression, aligning strictly to the Malaysian KSSM Additional Mathematics Form 4 and Form 5 syllabus.
            4. Keep the explanation friendly, encouraging, and clear, breaking down complex calculus, quadratic or trigonometry concepts.
        """.trimIndent()

        val promptBody = """
            Topic/Chapter: $chapterName
            Student's Problem: $problemDescription
            Student's Proposed Final Answer: ${studentAnswer.ifEmpty { "None provided" }}
            
            Please check, guide and explain step-by-step.
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = promptBody)))),
            systemInstruction = Content(parts = listOf(Part(text = systemPrompt)))
        )

        return try {
            val response = service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: (if (preferMs) "Ralat: Respons kosong dari AI." else "Error: Empty response from AI.")
        } catch (e: Exception) {
            val errorMsg = e.localizedMessage ?: e.message ?: "Unknown error"
            val tip = if (preferMs) {
                "\n\n⚙️ Cadangan Penyelesaian:\nPastikan Kunci API Gemini anda diletakkan di panel Secrets dan mempunyai akses kuota percuma."
            } else {
                "\n\n⚙️ Troubleshooting Tip:\nEnsure your Gemini API Key is configured correctly in the Secrets panel and has free quota/billing active."
            }
            if (preferMs) "Ralat semasa menghubungi AI: $errorMsg$tip" else "Error contacting AI: $errorMsg$tip"
        }
    }
}
