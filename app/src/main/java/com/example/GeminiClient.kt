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
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return if (preferMs) {
                "Sila konfigurasikan kunci API GEMINI di panel Secrets AI Studio untuk mendayakan Penyelesai AI!"
            } else {
                "Please configure the GEMINI API key in the AI Studio Secrets panel to enable the AI Solver!"
            }
        }

        // Custom Prompt constructed bilingual or target language
        val systemPrompt = """
            You are "SPM Add Maths Helper AI", an expert Malaysian SPM Additional Mathematics tutor.
            Your task is to review the student's problem, verify their final answer (if provided), and generate a detailed step-by-step solution.
            
            Follow these rules:
            1. Write your response primarily in ${if (preferMs) "Bahasa Melayu, but provide English translations in brackets where necessary" else "English, but provide Bahasa Melayu translations in brackets where necessary"}.
            2. If the student provided a final answer, declare immediately whether their answer is CORRECT or INCORRECT with supportive encouraging feedback.
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
            if (preferMs) "Ralat semasa menghubungi AI: $errorMsg" else "Error contacting AI: $errorMsg"
        }
    }
}
