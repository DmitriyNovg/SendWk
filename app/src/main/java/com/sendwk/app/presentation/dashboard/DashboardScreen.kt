package com.sendwk.app.presentation.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToTasks: () -> Unit,
    onNavigateToChat: () -> Unit,
    onLogout: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "SendWk",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToChat) {
                        Icon(
                            Icons.Default.ChatBubble,
                            contentDescription = "Чат",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Выйти",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Приветствие
            item {
                GreetingSection()
            }

            // KPI Карточки
            item {
                KPICardsSection(
                    onTasksClick = onNavigateToTasks
                )
            }

            // Быстрые действия
            item {
                QuickActionsSection(
                    onTasksClick = onNavigateToTasks,
                    onChatClick = onNavigateToChat
                )
            }

            // Недавняя активность
            item {
                RecentActivitySection()
            }
        }
    }
}

@Composable
fun GreetingSection() {
    val currentTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
    val greeting = when {
        currentTime.substring(0, 2).toInt() < 12 -> "Доброе утро"
        currentTime.substring(0, 2).toInt() < 18 -> "Добрый день"
        else -> "Добрый вечер"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = greeting + ",",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Александр! 👋",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Готовы к продуктивному дню?",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun KPICardsSection(onTasksClick: () -> Unit) {
    Column {
        Text(
            text = "Ваши показатели",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KPICard(
                title = "Задачи",
                value = "12",
                subtitle = "активных",
                icon = Icons.Default.TaskAlt,
                gradientColors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
                modifier = Modifier.weight(1f),
                onClick = onTasksClick
            )
            KPICard(
                title = "Выполнено",
                value = "8",
                subtitle = "на этой неделе",
                icon = Icons.Default.CheckCircle,
                gradientColors = listOf(Color(0xFF10B981), Color(0xFF34D399)),
                modifier = Modifier.weight(1f),
                onClick = onTasksClick
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KPICard(
                title = "Просрочено",
                value = "2",
                subtitle = "требуют внимания",
                icon = Icons.Default.Warning,
                gradientColors = listOf(Color(0xFFEF4444), Color(0xFFF87171)),
                modifier = Modifier.weight(1f),
                onClick = onTasksClick
            )
            KPICard(
                title = "В работе",
                value = "5",
                subtitle = "сейчас",
                icon = Icons.Default.Favorite,
                gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFFBBF24)),
                modifier = Modifier.weight(1f),
                onClick = onTasksClick
            )
        }
    }
}

@Composable
fun KPICard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = gradientColors.first(),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = value,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = gradientColors.map { it.copy(alpha = 0.2f) }
                        )
                    )
            )
        }
    }
}

@Composable
fun QuickActionsSection(
    onTasksClick: () -> Unit,
    onChatClick: () -> Unit
) {
    Column {
        Text(
            text = "Быстрые действия",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                QuickActionButton(
                    title = "Задачи",
                    icon = Icons.Default.TaskAlt,
                    gradientColors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
                    onClick = onTasksClick
                )
            }
            item {
                QuickActionButton(
                    title = "Чат",
                    icon = Icons.Default.ChatBubble,
                    gradientColors = listOf(Color(0xFF10B981), Color(0xFF34D399)),
                    onClick = onChatClick
                )
            }
            item {
                QuickActionButton(
                    title = "Календарь",
                    icon = Icons.Default.CalendarToday,
                    gradientColors = listOf(Color(0xFFEF4444), Color(0xFFF87171)),
                    onClick = { }
                )
            }
            item {
                QuickActionButton(
                    title = "Документы",
                    icon = Icons.Default.Description,
                    gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFFBBF24)),
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun RecentActivitySection() {
    Column {
        Text(
            text = "Недавняя активность",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        val activities = listOf(
            ActivityItem("Создана новая задача", "Дизайн главной страницы", "2 мин назад"),
            ActivityItem("Задача выполнена", "Подготовить отчет", "1 час назад"),
            ActivityItem("Новое сообщение", "От Александра", "3 часа назад"),
            ActivityItem("Дедлайн приближается", "Презентация проекта", "5 часов назад")
        )

        activities.forEach { activity ->
            ActivityCard(activity)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

data class ActivityItem(
    val type: String,
    val description: String,
    val time: String
)

@Composable
fun ActivityCard(activity: ActivityItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.type,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = activity.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = activity.time,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}