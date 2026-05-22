package com.example

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableHighRefreshRate()
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            MyApplicationTheme(darkTheme = isDarkTheme) {
                AppMainScreen(viewModel)
            }
        }
    }

    private fun enableHighRefreshRate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                @Suppress("DEPRECATION")
                val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    display
                } else {
                    windowManager.defaultDisplay
                }
                val modes = display?.supportedModes
                if (!modes.isNullOrEmpty()) {
                    val maxRate = modes.map { it.refreshRate }.maxOrNull() ?: 60f
                    if (maxRate > 60f) {
                        val lp = window.attributes
                        var setViaReflection = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            try {
                                val minField = lp::class.java.getField("preferredMinDisplayRefreshRate")
                                val maxField = lp::class.java.getField("preferredMaxDisplayRefreshRate")
                                minField.set(lp, maxRate)
                                maxField.set(lp, maxRate)
                                setViaReflection = true
                            } catch (re: Throwable) {
                                // ignore reflection fallbacks
                            }
                        }
                        if (!setViaReflection) {
                            val highestMode = modes.maxByOrNull { it.refreshRate }
                            if (highestMode != null) {
                                lp.preferredDisplayModeId = highestMode.modeId
                            }
                        }
                        window.attributes = lp
                    }
                }
            } catch (e: Exception) {
                // Graceful fallback
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppMainScreen(viewModel: MainViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val isMs by viewModel.isMalaySelected.collectAsState()
    val activeChapter by viewModel.activeChapter.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            AppHeader(
                isMs = isMs,
                isDarkTheme = isDarkTheme,
                onLanguageToggle = { viewModel.setMalaySelected(!isMs) },
                onThemeToggle = { viewModel.setDarkTheme(!isDarkTheme) },
                onBackPressed = if (activeChapter != null) { { viewModel.openChapter(null) } } else null
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { 
                    viewModel.selectTab(it)
                    // Automatically clear active chapter view when changing tabs
                    viewModel.openChapter(null) 
                },
                isMs = isMs
            )
        },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        )
                    )
                )
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) with fadeOut(animationSpec = tween(220))
                },
                label = "MainContentTransition"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> SyllabusTab(viewModel, isMs)
                    1 -> AiSolverTab(viewModel, isMs)
                    2 -> QuizTab(viewModel, isMs)
                    3 -> AnalyticsTab(viewModel, isMs)
                }
            }
        }
    }
}

// --- APP TOP BAR HEADER ---
@Composable
fun AppHeader(
    isMs: Boolean,
    isDarkTheme: Boolean,
    onLanguageToggle: () -> Unit,
    onThemeToggle: () -> Unit,
    onBackPressed: (() -> Unit)? = null
) {
    Surface(
        tonalElevation = 6.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onBackPressed != null) {
                        IconButton(onClick = onBackPressed, modifier = Modifier.size(40.dp)) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    } else {
                        // Tiny Math drawing / Logo representation
                        Canvas(modifier = Modifier.size(32.dp)) {
                            drawCircle(
                                color = Color(0xFF0284C7),
                                radius = size.minDimension / 3,
                                center = Offset(size.width / 3, size.height / 3)
                            )
                            drawCircle(
                                color = Color(0xFFEA580C),
                                radius = size.minDimension / 4,
                                center = Offset(size.width * 2 / 3, size.height * 2 / 3)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Column {
                        Text(
                            text = "SPM Add Maths",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isMs) "Rujukan KSSM (v2.0)" else "KSSM Prep Companion v2",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Theme Toggle Button
                    IconButton(
                        onClick = onThemeToggle,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Text(
                            text = if (isDarkTheme) "☀️" else "🌙",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Global Bilingual EN/BM Switcher Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable { onLanguageToggle() }
                            .padding(horizontal = 4.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (!isMs) MaterialTheme.colorScheme.primary else Color.Transparent)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "EN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isMs) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isMs) MaterialTheme.colorScheme.primary else Color.Transparent)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "BM",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isMs) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

data class MenuTabItem(val label: String, val icon: ImageVector, val index: Int)

// --- APP BOTTOM BAR ---
@Composable
fun AppBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    isMs: Boolean
) {
    NavigationBar(
        tonalElevation = 8.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        val menuItems = listOf(
            MenuTabItem(if (isMs) "Silibus" else "Syllabus", Icons.Default.Menu, 0),
            MenuTabItem(if (isMs) "AI Tutor" else "AI Tutor", Icons.Default.Star, 1),
            MenuTabItem(if (isMs) "Kuiz" else "Quiz", Icons.Default.PlayArrow, 2),
            MenuTabItem(if (isMs) "Prestasi" else "Analytics", Icons.Default.Info, 3)
        )

        menuItems.forEach { item ->
            NavigationBarItem(
                selected = selectedTab == item.index,
                onClick = { onTabSelected(item.index) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(text = item.label, fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

// ==========================================
// TAB 1: SYLLABUS DIRECTORY AND PRACTICE
// ==========================================
@Composable
fun SyllabusTab(viewModel: MainViewModel, isMs: Boolean) {
    val activeChapter by viewModel.activeChapter.collectAsState()

    if (activeChapter == null) {
        SyllabusBrowserScreen(viewModel, isMs)
    } else {
        ChapterDetailScreen(viewModel, activeChapter!!, isMs)
    }
}

@Composable
fun SyllabusBrowserScreen(viewModel: MainViewModel, isMs: Boolean) {
    val selectedForm by viewModel.selectedSyllabusForm.collectAsState()
    val progresses by viewModel.topicProgresses.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Form Selector Row (Tingkatan 4 atau 5)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(4, 5).forEach { form ->
                val fLabel = if (isMs) "Tingkatan $form" else "Form $form"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (selectedForm == form) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { viewModel.selectSyllabusForm(form) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = fLabel,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedForm == form) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        // List of Chapters for Selected Form
        val listChapters = SyllabusData.chapters.filter { it.form == selectedForm }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(listChapters, key = { chapter -> "${chapter.form}_${chapter.number}" }) { chapter ->
                val stats = progresses.find { it.chapterNumber == chapter.number && it.form == chapter.form }
                ChapterListCard(chapter = chapter, stats = stats, isMs = isMs) {
                    viewModel.openChapter(chapter)
                }
            }
        }
    }
}

@Composable
fun ChapterListCard(
    chapter: Chapter,
    stats: TopicProgress?,
    isMs: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Chapter Number Label Circle
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${chapter.number}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isMs) "Bab ${chapter.number}" else "Chapter ${chapter.number}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = chapter.name.get(isMs),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Chapter attempt progresses details
                if (stats != null && stats.totalAttempts > 0) {
                    val progressValue = (stats.answeredCorrectly.toFloat() / stats.totalAttempts.toFloat()).coerceIn(0f, 1f)
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progressValue },
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                        color = if (stats.accuracy >= 80) Color(0xFF10B981) else if (stats.accuracy >= 50) Color(0xFFEA580C) else Color(0xFFEF4444),
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (isMs) "Jawapan: ${stats.answeredCorrectly}/${stats.totalAttempts}" else "Answers: ${stats.answeredCorrectly}/${stats.totalAttempts}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "${stats.status(isMs)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (stats.accuracy >= 80) Color(0xFF10B981) else if (stats.accuracy >= 50) Color(0xFFEA580C) else Color(0xFFEF4444)
                        )
                    }
                } else {
                    Text(
                        text = if (isMs) "Belum dicuba" else "No attempts recorded",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun ChapterDetailScreen(
    viewModel: MainViewModel,
    chapter: Chapter,
    isMs: Boolean
) {
    var subTabSelection by remember { mutableStateOf(0) } // 0=Notes, 1=Formulas, 2=Practice

    Column(modifier = Modifier.fillMaxSize()) {
        // Upper Chapter title banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isMs) "Tingkatan ${chapter.form} • Bab ${chapter.number}" else "Form ${chapter.form} • Chapter ${chapter.number}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = chapter.name.get(isMs),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Subtabs: Notes, Formulas, Practice
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                .padding(2.dp)
        ) {
            val tabs = listOf(
                if (isMs) "Nota" else "Notes",
                if (isMs) "Formula" else "Formulas",
                if (isMs) "Latihan" else "Practice"
            )
            tabs.forEachIndexed { idx, tabLabel ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (subTabSelection == idx) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { subTabSelection = idx }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tabLabel,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (subTabSelection == idx) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Content of selected subtab
        when (subTabSelection) {
            0 -> NotesSubTab(chapter.notes, isMs)
            1 -> FormulasSubTab(chapter.formulas, isMs)
            2 -> PracticeSubTab(viewModel, chapter.practiceQuestions, isMs)
        }
    }
}

@Composable
fun NotesSubTab(notes: List<NoteSection>, isMs: Boolean) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(notes) { section ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(1.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = section.title.get(isMs),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    section.bulletPoints.forEach { point ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "•", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                            Text(
                                text = point.get(isMs),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormulasSubTab(formulas: List<Formula>, isMs: Boolean) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredFormulas = if (searchQuery.trim().isEmpty()) {
        formulas
    } else {
        formulas.filter {
            it.title.get(isMs).contains(searchQuery, ignoreCase = true) ||
            it.expression.contains(searchQuery, ignoreCase = true) ||
            it.description.get(isMs).contains(searchQuery, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (formulas.isNotEmpty()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(text = if (isMs) "Cari rumus..." else "Search formulas...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        if (filteredFormulas.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (searchQuery.isNotEmpty()) {
                        if (isMs) "Tiada rumus padan dengan carian." else "No formulas match your search."
                    } else {
                        if (isMs) "Tiada formula khusus untuk bab ini." else "No specific formulas defined for this chapter."
                    },
                    color = MaterialTheme.colorScheme.outline
                )
            }
            return
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(filteredFormulas, key = { formula -> formula.expression }) { formula ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = formula.title.get(isMs),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Stylized mathematically formatted formula card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = formula.expression,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = formula.description.get(isMs),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PracticeSubTab(
    viewModel: MainViewModel,
    questions: List<PracticeQuestion>,
    isMs: Boolean
) {
    var selectedDifficultyFilter by remember { mutableStateOf<Difficulty?>(null) } // null = show all
    val studentAnswers by viewModel.practiceAnswers.collectAsState()
    val expandedWorkings by viewModel.expandedWorkings.collectAsState()
    val focusManager = LocalFocusManager.current

    val filteredQuestions = if (selectedDifficultyFilter == null) {
        questions
    } else {
        questions.filter { it.difficulty == selectedDifficultyFilter }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Difficulty Filter Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // "All" filter chip
            FilterChip(
                selected = selectedDifficultyFilter == null,
                onClick = { selectedDifficultyFilter = null },
                label = { Text(text = if (isMs) "Semua" else "All") }
            )

            listOf(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD).forEach { diff ->
                FilterChip(
                    selected = selectedDifficultyFilter == diff,
                    onClick = { selectedDifficultyFilter = diff },
                    label = { Text(text = diff.label(isMs)) }
                )
            }
        }

        if (filteredQuestions.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isMs) "Tiada soalan bagi tahap ini." else "No questions for this difficulty.",
                    color = MaterialTheme.colorScheme.outline
                )
            }
            return
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(filteredQuestions, key = { q -> q.id }) { q ->
                val ansState = studentAnswers[q.id] ?: Triple("", false, false) // (text, isChecked, isCorrect)
                val isExpanded = expandedWorkings[q.id] ?: false
                
                // Track direct manual toggle states
                var showAnswerDirectly by remember(q.id) { mutableStateOf(false) }
                var showStepsDirectly by remember(q.id) { mutableStateOf(false) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header with difficulty badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when (q.difficulty) {
                                            Difficulty.EASY -> Color(0xFF10B981).copy(alpha = 0.15f)
                                            Difficulty.MEDIUM -> Color(0xFFEA580C).copy(alpha = 0.15f)
                                            Difficulty.HARD -> Color(0xFFEF4444).copy(alpha = 0.15f)
                                        }
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = q.difficulty.label(isMs),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (q.difficulty) {
                                        Difficulty.EASY -> Color(0xFF047857)
                                        Difficulty.MEDIUM -> Color(0xFFC2410C)
                                        Difficulty.HARD -> Color(0xFFB91C1C)
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Question Text
                        Text(
                            text = q.question.get(isMs),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Input field for final answer
                        OutlinedTextField(
                            value = ansState.first,
                            onValueChange = { viewModel.updatePracticeInput(q.id, it) },
                            placeholder = { Text(text = if (isMs) "Taip jawapan anda (cth: 5)" else "Type your answer (e.g. 5)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            ),
                            enabled = !ansState.second // disable after checked
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Actions Row 1 (Check & Hint)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // "Semak / Check" Button
                            Button(
                                onClick = { 
                                    viewModel.checkPracticeAnswer(q) 
                                    focusManager.clearFocus()
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                enabled = ansState.first.isNotEmpty() && !ansState.second
                            ) {
                                Text(text = if (isMs) "Semak" else "Check", fontWeight = FontWeight.Bold)
                            }

                            // "Hint / Petunjuk" Button
                            var showHintDialog by remember { mutableStateOf(false) }
                            OutlinedButton(
                                onClick = { showHintDialog = true },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = if (isMs) "Petunjuk" else "Hint")
                            }

                            if (showHintDialog) {
                                AlertDialog(
                                    onDismissRequest = { showHintDialog = false },
                                    title = { Text(text = if (isMs) "Petunjuk Kunci" else "Secret Hint") },
                                    text = { Text(text = q.hint.get(isMs)) },
                                    confirmButton = {
                                        TextButton(onClick = { showHintDialog = false }) {
                                            Text(text = "OK")
                                        }
                                    }
                                )
                            }
                        }

                        // Actions Row 2 ("Show Answer" / "Show Steps" direct helper buttons)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showAnswerDirectly = !showAnswerDirectly },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (showAnswerDirectly) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else Color.Transparent
                                )
                            ) {
                                Text(
                                    text = if (isMs) (if (showAnswerDirectly) "Sembunyi Jawapan" else "Tunjuk Jawapan") 
                                           else (if (showAnswerDirectly) "Hide Answer" else "Show Answer"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            OutlinedButton(
                                onClick = { showStepsDirectly = !showStepsDirectly },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (showStepsDirectly) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f) else Color.Transparent
                                )
                            ) {
                                Text(
                                    text = if (isMs) (if (showStepsDirectly) "Sembunyi Jalan Kerja" else "Tunjuk Jalan Kerja") 
                                           else (if (showStepsDirectly) "Hide Steps" else "Show Steps"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Show Answer Section
                        if (showAnswerDirectly || ansState.second) {
                            Spacer(modifier = Modifier.height(16.dp))

                            val isUserAnswerCorrect = ansState.second && ansState.third
                            val containerColor = if (isUserAnswerCorrect) Color(0xFF10B981).copy(alpha = 0.12f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            val textColor = if (isUserAnswerCorrect) Color(0xFF047857) else MaterialTheme.colorScheme.onPrimaryContainer
                            val borderColor = if (isUserAnswerCorrect) Color(0xFF10B981).copy(alpha = 0.4f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(containerColor)
                                    .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (isUserAnswerCorrect) Icons.Default.CheckCircle else Icons.Default.Info,
                                            contentDescription = "Status",
                                            tint = textColor,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isMs) "Jawapan Betul:" else "Correct Answer:",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = textColor
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = q.correctAnswer,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = textColor,
                                        modifier = Modifier.padding(start = 26.dp)
                                    )
                                    if (ansState.second) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = if (ansState.third) {
                                                if (isMs) "Cemerlang! Langkah anda tepat." else "Excellent! Your answer is correct."
                                            } else {
                                                if (isMs) "Jawapan anda salah. Rujuk jalan kerja di bawah." else "Incorrect. Review the working steps below."
                                            },
                                            fontSize = 11.sp,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                            color = textColor.copy(alpha = 0.8f),
                                            modifier = Modifier.padding(start = 26.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Show Step-by-Step / Wordings Solution
                        if (showStepsDirectly || isExpanded) {
                            Spacer(modifier = Modifier.height(12.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)),
                                border = borderModifier()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "f",
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isMs) "Jalan Kerja Mengikut Format SPM" else "SPM-Formatted Step-by-Step Working",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Render steps as beautiful linear visual nodes split by newline or =>
                                    val stepsList = q.stepByStep.get(isMs).split("=>", "\n").map { it.trim() }.filter { it.isNotEmpty() }
                                    if (stepsList.isNotEmpty()) {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            stepsList.forEachIndexed { sIdx, stepStr ->
                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                     Column(
                                                         horizontalAlignment = Alignment.CenterHorizontally,
                                                         modifier = Modifier.padding(top = 2.dp)
                                                     ) {
                                                         Box(
                                                             modifier = Modifier
                                                                 .size(18.dp)
                                                                 .clip(CircleShape)
                                                                 .background(MaterialTheme.colorScheme.secondary),
                                                             contentAlignment = Alignment.Center
                                                         ) {
                                                             Text(
                                                                 text = "${sIdx + 1}",
                                                                 fontSize = 9.sp,
                                                                 fontWeight = FontWeight.Bold,
                                                                 color = MaterialTheme.colorScheme.onSecondary
                                                             )
                                                         }
                                                         if (sIdx < stepsList.size - 1) {
                                                             Box(
                                                                 modifier = Modifier
                                                                     .width(2.dp)
                                                                     .height(20.dp)
                                                                     .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                                             )
                                                         }
                                                     }
                                                     Spacer(modifier = Modifier.width(10.dp))
                                                     Text(
                                                         text = stepStr,
                                                         fontSize = 13.sp,
                                                         color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                         lineHeight = 17.sp,
                                                         modifier = Modifier.weight(1f)
                                                     )
                                                }
                                            }
                                        }
                                    } else {
                                        Text(
                                            text = q.stepByStep.get(isMs),
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 17.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun borderModifier(): androidx.compose.foundation.BorderStroke {
    return androidx.compose.foundation.BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
    )
}

// ==========================================
// TAB 2: AI SOLVER AND CHECKER (GEMINI)
// ==========================================
@Composable
fun AiSolverTab(viewModel: MainViewModel, isMs: Boolean) {
    val aiSelectedChapter by viewModel.aiSelectedChapter.collectAsState()
    val aiProblemInput by viewModel.aiProblemInput.collectAsState()
    val aiAnswerInput by viewModel.aiAnswerInput.collectAsState()
    val aiResponseText by viewModel.aiResponseText.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    var showDropdownMenu by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // AI Title Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "AI Assistant",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = if (isMs) "Penyelesai & Semakan AI" else "AI Mathematical Solver",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = if (isMs) "Dapatkan penyelesaian terperinci untuk sebarang masalah!" else "Paste your math question and get custom step-by-step guidance!",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Dropdown Chapter Selector
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (isMs) "Pilih Topik Berkenaan (Bantu ketepatan AI):" else "Select Associated Topic (Helps AI Context):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .clickable { showDropdownMenu = true }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Form ${aiSelectedChapter.form} • Bab ${aiSelectedChapter.number}: ${aiSelectedChapter.name.get(isMs)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }

                DropdownMenu(
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    SyllabusData.chapters.forEach { chapter ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Form ${chapter.form} • Chapter ${chapter.number}: ${chapter.name.get(isMs)}",
                                    fontSize = 12.sp
                                )
                            },
                            onClick = {
                                viewModel.selectAiChapter(chapter)
                                showDropdownMenu = false
                            }
                        )
                    }
                }
            }
        }

        // Main Problem Multi-line input
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (isMs) "Masukkan Soalan Matematik Tambahan:" else "Type/Paste Your Math Problem:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = aiProblemInput,
                onValueChange = { viewModel.updateAiProblem(it) },
                placeholder = {
                    Text(
                        text = if (isMs) "Cth: Diberi g(x) = 3x - 1, cari nilai x apabila g(x) = 8." else "E.g. Intgrate (3x^2 + 5x) dx from bounds 0 to 2.",
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }

        // Student's solved answer check (Optional)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (isMs) "Jawapan Anda (Pilihan - AI akan menyemak jika betul):" else "Your Theoretical Final Answer (Optional - checked by AI):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = aiAnswerInput,
                onValueChange = { viewModel.updateAiAnswer(it) },
                placeholder = { Text(text = if (isMs) "Cth: x = 3" else "E.g. 15", fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }

        // Clear and Solve buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.clearAiSolver() },
                modifier = Modifier.weight(0.4f),
                enabled = !isAiLoading
            ) {
                Text(text = if (isMs) "Padam" else "Reset")
            }

            Button(
                onClick = { 
                    viewModel.runAiSolver()
                    focusManager.clearFocus()
                },
                modifier = Modifier.weight(0.6f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = aiProblemInput.isNotBlank() && !isAiLoading
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isAiLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Solve")
                    }
                    Text(text = if (isMs) "Kira & Semak" else "Analyze & Solve", fontWeight = FontWeight.Bold)
                }
            }
        }

        // response screen
        if (aiResponseText.isNotEmpty()) {
            Text(
                text = if (isMs) "Jalan Kerja Matematik AI:" else "AI Step-by-Step Resolution:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = borderModifier()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (isAiLoading) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            Text(
                                text = aiResponseText,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    } else {
                        // AI Response body (clean text format)
                        Text(
                            text = aiResponseText,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 19.sp
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// TAB 3: QUIZ MODE & STATE CARDS
// ==========================================
@Composable
fun QuizTab(viewModel: MainViewModel, isMs: Boolean) {
    val quizFormChoice by viewModel.quizFormChoice.collectAsState()

    if (quizFormChoice == null) {
        QuizSelectionScreen(viewModel, isMs)
    } else {
        QuizActiveScreen(viewModel, quizFormChoice!!, isMs)
    }
}

@Composable
fun QuizSelectionScreen(viewModel: MainViewModel, isMs: Boolean) {
    var selectedDifficulty by remember { mutableStateOf<Difficulty?>(null) } // null means "All" / "Semua"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Quiz Logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isMs) "Uji Prestasi Anda!" else "Assess Your Real Skills!",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = if (isMs) "Setiap kuiz mengandungi 5 soalan rawak dwibahasa daripada silibus KSSM yang dipilih." else "Continuous testing reduces anxiety. Take a bilingual 5-question mock quiz based on selected Form syllabus.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Difficulty Selector Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isMs) "Pilih Tahap Kesukaran:" else "Select Difficulty Level:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val difficulties = listOf(
                        null,
                        Difficulty.EASY,
                        Difficulty.MEDIUM,
                        Difficulty.HARD
                    )

                    difficulties.forEach { diff ->
                        val isSel = selectedDifficulty == diff
                        val label = diff?.label(isMs) ?: (if (isMs) "Semua" else "All")
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSel) {
                                        when (diff) {
                                            null -> MaterialTheme.colorScheme.primary
                                            Difficulty.EASY -> Color(0xFF10B981)
                                            Difficulty.MEDIUM -> Color(0xFFEA580C)
                                            Difficulty.HARD -> Color(0xFFEF4444)
                                        }
                                    } else {
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                                    }
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSel) Color.Transparent else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedDifficulty = diff }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Form 4 Quiz Button
        Button(
            onClick = { viewModel.startQuiz(4, selectedDifficulty) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isMs) "Mula Kuiz Tingkatan 4" else "Start Form 4 Quiz",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Form 5 Quiz Button
        Button(
            onClick = { viewModel.startQuiz(5, selectedDifficulty) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isMs) "Mula Kuiz Tingkatan 5" else "Start Form 5 Quiz",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun QuizActiveScreen(
    viewModel: MainViewModel,
    formChoice: Int,
    isMs: Boolean
) {
    val activeQuestions by viewModel.activeQuizQuestions.collectAsState()
    val currentIndex by viewModel.currentQuizIndex.collectAsState()
    val selectedAnswers by viewModel.quizSelectedAnswers.collectAsState()
    val isSubmitted by viewModel.isQuizSubmitted.collectAsState()

    if (activeQuestions.isEmpty()) return

    val question = activeQuestions[currentIndex]
    val selectedOptionIndex = selectedAnswers[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Quiz Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isMs) "Kuiz Tingkatan $formChoice" else "Form $formChoice Quiz",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )

            // Progress text
            Text(
                text = if (isMs) "Soalan ${currentIndex + 1} dari ${activeQuestions.size}" else "Question ${currentIndex + 1} of ${activeQuestions.size}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress bar indicator
        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / activeQuestions.size.toFloat() },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Main Question container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = borderModifier()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Difficulty pill badge for active question
                Box(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            when (question.difficulty) {
                                Difficulty.EASY -> Color(0xFF10B981).copy(alpha = 0.15f)
                                Difficulty.MEDIUM -> Color(0xFFEA580C).copy(alpha = 0.15f)
                                Difficulty.HARD -> Color(0xFFEF4444).copy(alpha = 0.15f)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = question.difficulty.label(isMs),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (question.difficulty) {
                            Difficulty.EASY -> Color(0xFF047857)
                            Difficulty.MEDIUM -> Color(0xFFC2410C)
                            Difficulty.HARD -> Color(0xFFB91C1C)
                        }
                    )
                }

                Text(
                    text = question.question.get(isMs),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Render options
                question.options.forEachIndexed { optIdx, optionText ->
                    val isOptionSelected = selectedOptionIndex == optIdx

                    val containerColor = when {
                        // after submitted displays correct/incorrect highlight colors
                        isSubmitted && optIdx == question.correctIndex -> Color(0xFF10B981).copy(alpha = 0.18f)
                        isSubmitted && isOptionSelected && selectedOptionIndex != question.correctIndex -> Color(0xFFEF4444).copy(alpha = 0.15f)
                        isOptionSelected -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val borderColor = when {
                        isSubmitted && optIdx == question.correctIndex -> Color(0xFF10B981)
                        isSubmitted && isOptionSelected && selectedOptionIndex != question.correctIndex -> Color(0xFFEF4444)
                        isOptionSelected -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    }

                    val letter = when(optIdx) {
                        0 -> "A"
                        1 -> "B"
                        2 -> "C"
                        else -> "D"
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(containerColor)
                            .border(1.2.dp, borderColor, RoundedCornerShape(8.dp))
                            .clickable { viewModel.selectQuizOption(currentIndex, optIdx) }
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$letter.",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = if (isOptionSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = optionText,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Show Explanation if submitted
                if (isSubmitted) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f))
                            .border(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Explanation",
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isMs) "Penjelasan:" else "Explanation:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = question.explanation.get(isMs),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Actions Controls section
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous Button
            TextButton(
                onClick = { viewModel.moveQuizIndex(-1) },
                enabled = currentIndex > 0
            ) {
                Text(text = if (isMs) "Sebelum" else "Previous")
            }

            // Submit / Reset / Quit control
            if (!isSubmitted) {
                if (currentIndex == activeQuestions.size - 1) {
                    val allAnswered = selectedAnswers.size >= activeQuestions.size
                    Button(
                        onClick = { viewModel.submitQuiz() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        enabled = allAnswered
                    ) {
                        Text(text = if (isMs) "Hantar Kuiz" else "Submit Quiz", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(onClick = { viewModel.moveQuizIndex(1) }) {
                        Text(text = if (isMs) "Seterusnya" else "Next")
                    }
                }
            } else {
                // If submitted, give Option to exit
                Button(
                    onClick = { viewModel.quitQuiz() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = if (isMs) "Selesai & Keluar" else "Finish & Exit", fontWeight = FontWeight.Bold)
                }
            }

            // Next Button for submitted viewing
            if (isSubmitted) {
                TextButton(
                    onClick = { viewModel.moveQuizIndex(1) },
                    enabled = currentIndex < activeQuestions.size - 1
                ) {
                    Text(text = if (isMs) "Seterusnya" else "Next")
                }
            }
        }
    }
}

// ==========================================
// TAB 4: WEAK TOPIC ANALYTICS & STATS
// ==========================================
@Composable
fun AnalyticsTab(viewModel: MainViewModel, isMs: Boolean) {
    val topicProgresses by viewModel.topicProgresses.collectAsState()
    var showResetConfirmation by remember { mutableStateOf(false) }

    // Aggregate overall statistics
    val totalAttempts = topicProgresses.sumOf { it.totalAttempts }
    val totalCorrect = topicProgresses.sumOf { it.answeredCorrectly }
    val overallAccuracy = if (totalAttempts > 0) (totalCorrect.toFloat() / totalAttempts) * 100 else 0f

    val weakTopics = topicProgresses.filter { it.totalAttempts > 0 && it.accuracy < 50 }
    val masteredTopics = topicProgresses.filter { it.totalAttempts > 0 && it.accuracy >= 80 }
    val inProgressTopics = topicProgresses.filter { it.totalAttempts > 0 && it.accuracy in 50.0..79.9 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Analytics Title Header Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isMs) "Analitis Prestasi Belajar" else "Personal Performance Analytics",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = if (isMs) "Jejak kelemahan anda untuk peningkatan SPM yang berkesan!" else "Target and turn your weakness chapters into top-grade strengths!",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }

        // Summary Circle Rings & Stats Row
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = borderModifier()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circle Score
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(4.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (totalAttempts > 0) "${overallAccuracy.toInt()}%" else "-",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (isMs) "Ketepatan" else "Accuracy",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                // Numeric Counts info column
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.outline)
                        )
                        Text(
                            text = if (isMs) "Jumlah Percubaan: $totalAttempts" else "Total Attempts: $totalAttempts",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF10B981))
                        )
                        Text(
                            text = if (isMs) "Jawapan Betul: $totalCorrect" else "Total Correct: $totalCorrect",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFEF4444))
                        )
                        Text(
                            text = if (isMs) "Salah: ${totalAttempts - totalCorrect}" else "Incorrect: ${totalAttempts - totalCorrect}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // --- SECTION 1: WEAK TOPICS (RED HIGHLIGHTS) ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Weakness Warning",
                    tint = Color(0xFFEF4444)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isMs) "Topik Lemah (Perlu Fokus!)" else "Weak Topics (Focus Required!)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFEF4444)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (weakTopics.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = borderModifier()
                ) {
                    Text(
                        text = if (isMs) "Tiada topik dalam kategori ini! Bagus." else "Amazing! No weak topics detected yet.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = borderModifier()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        weakTopics.forEachIndexed { index, progress ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Form ${progress.form} • Bab ${progress.chapterNumber}: ${progress.chapterName}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(0.7f)
                                )
                                Text(
                                    text = "${progress.accuracy.toInt()}% Right",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFEF4444),
                                    modifier = Modifier.weight(0.3f),
                                    textAlign = TextAlign.End
                                )
                            }
                            if (index < weakTopics.size - 1) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            }
                        }
                    }
                }
            }
        }

        // --- SECTION 2: MASTERED TOPICS (GREEN HIGHLIGHTS) ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Mastered",
                    tint = Color(0xFF10B981)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isMs) "Topik Dikuasai (Syabas!)" else "Mastered Topics (Excellent!)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF10B981)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (masteredTopics.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = borderModifier()
                ) {
                    Text(
                        text = if (isMs) "Cuba kuiz dan latihan untuk mengumpul rekod kecemerlangan!" else "Complete quizzes and practice to register topics here!",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = borderModifier()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        masteredTopics.forEachIndexed { index, progress ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Form ${progress.form} • Bab ${progress.chapterNumber}: ${progress.chapterName}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(0.7f)
                                )
                                Text(
                                    text = "${progress.accuracy.toInt()}% Right",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF10B981),
                                    modifier = Modifier.weight(0.3f),
                                    textAlign = TextAlign.End
                                )
                            }
                            if (index < masteredTopics.size - 1) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            }
                        }
                    }
                }
            }
        }

        // Recommender Tip Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isMs) "💡 Saranan Mentor SPM:" else "💡 SPM Mentor Recommendation:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isMs) {
                        "Gunakan Penyelesai AI Tutor untuk mematangkan pemikiran anda bagi topik-topik lemah. Menjawab kuiz mingguan meningkatkan kelajuan kognitif memori anda!"
                    } else {
                        "Use the AI Solver to investigate steps for complex algebraic methods in your weak topics. Continuous quizzing establishes solid long-term retrieval practice!"
                    },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp
                )
            }
        }

        // Global Reset DB button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = { showResetConfirmation = true },
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF4444))
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset")
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isMs) "Padam Semua Rekod" else "Clear All Progress Stats")
            }
        }

        if (showResetConfirmation) {
            AlertDialog(
                onDismissRequest = { showResetConfirmation = false },
                title = { Text(text = if (isMs) "Sahkan Pemadaman" else "Reset All Statistics") },
                text = { Text(text = if (isMs) "Adakah anda pasti mahu memadam semua jawapan latihan dan skor kuiz anda? Tindakan ini tidak boleh diundurkan." else "Are you sure you want to clear your local answers history, analytics and quiz scores? This cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.resetTracker()
                            showResetConfirmation = false
                        }
                    ) {
                        Text(text = if (isMs) "Mahu Padam" else "Reset Now", color = Color(0xFFEF4444))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetConfirmation = false }) {
                        Text(text = if (isMs) "Batal" else "Cancel")
                    }
                }
            )
        }
    }
}
