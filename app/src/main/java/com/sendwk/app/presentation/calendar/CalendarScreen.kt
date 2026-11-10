package com.sendwk.app.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sendwk.app.data.local.entities.TaskEntity
import com.sendwk.app.presentation.tasks.TaskViewModel
import com.sendwk.app.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onNavigateBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDay by remember { mutableStateOf(Calendar.getInstance()) }

    val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentMonth.time)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Schedule", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Month Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    val newMonth = currentMonth.clone() as Calendar
                    newMonth.add(Calendar.MONTH, -1)
                    currentMonth = newMonth
                }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Prev")
                }
                Text(
                    text = monthName.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = {
                    val newMonth = currentMonth.clone() as Calendar
                    newMonth.add(Calendar.MONTH, 1)
                    currentMonth = newMonth
                }) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Next")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar Grid
            CalendarGrid(currentMonth, selectedDay, tasks) { day ->
                val newSelected = currentMonth.clone() as Calendar
                newSelected.set(Calendar.DAY_OF_MONTH, day)
                selectedDay = newSelected
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "> AGENDA_FOR_${SimpleDateFormat("MMM_dd", Locale.getDefault()).format(selectedDay.time).uppercase()}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            // Selected Day Tasks List
            val selectedDayTasks = tasks.filter { task ->
                isSameDay(task.createdAt, selectedDay) || (task.deadline != null && isSameDay(task.deadline, selectedDay))
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (selectedDayTasks.isEmpty()) {
                    item {
                        Text(
                            "No events logged for this timeframe.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                items(selectedDayTasks) { task ->
                    TaskBriefCard(task, selectedDay)
                }
            }
        }
    }
}

@Composable
fun CalendarGrid(currentMonth: Calendar, selectedDay: Calendar, tasks: List<TaskEntity>, onDayClick: (Int) -> Unit) {
    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val tempCal = currentMonth.clone() as Calendar
    tempCal.set(Calendar.DAY_OF_MONTH, 1)
    val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1
    
    val days = (0 until firstDayOfWeek).map { -1 } + (1..daysInMonth).toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.heightIn(max = 300.dp)
    ) {
        items(days) { day ->
            if (day != -1) {
                val isSelected = selectedDay.get(Calendar.DAY_OF_MONTH) == day && 
                               selectedDay.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH) &&
                               selectedDay.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR)
                
                val dayTasks = tasks.filter { task ->
                    isSameDay(task.createdAt, currentMonth, day) || 
                    (task.deadline != null && isSameDay(task.deadline, currentMonth, day))
                }
                
                DayBox(day, isSelected, dayTasks) {
                    onDayClick(day)
                }
            } else {
                Box(modifier = Modifier.aspectRatio(1f))
            }
        }
    }
}

@Composable
fun DayBox(day: Int, isSelected: Boolean, tasks: List<TaskEntity>, onClick: () -> Unit) {
    val isToday = isSameDay(System.currentTimeMillis(), Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, day) })

    Surface(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) 
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) 
                 else if (isToday) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                 else null
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                color = if (isToday) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
            )
            if (tasks.isNotEmpty()) {
                Row(modifier = Modifier.padding(top = 2.dp), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    val hasDeadline = tasks.any { it.deadline != null && isSameDay(it.deadline, Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, day) }) }
                    
                    Box(modifier = Modifier.size(3.dp).background(CyanNeon, CircleShape))
                    if (hasDeadline) {
                        Box(modifier = Modifier.size(3.dp).background(MagentaNeon, CircleShape))
                    }
                }
            }
        }
    }
}

@Composable
fun TaskBriefCard(task: TaskEntity, selectedDay: Calendar) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            val isDeadline = task.deadline != null && isSameDay(task.deadline, selectedDay)
            
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(if (isDeadline) MagentaNeon else CyanNeon, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(task.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(
                    if (isDeadline) "DEADLINE_PROTOCOL" else "CREATION_LOG",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDeadline) MagentaNeon else CyanNeon
                )
            }
        }
    }
}

private fun isSameDay(millis: Long, calendar: Calendar): Boolean {
    val cal1 = Calendar.getInstance().apply { timeInMillis = millis }
    return cal1.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
           cal1.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
}

private fun isSameDay(millis: Long, monthCal: Calendar, day: Int): Boolean {
    val cal = Calendar.getInstance().apply { timeInMillis = millis }
    return cal.get(Calendar.YEAR) == monthCal.get(Calendar.YEAR) &&
           cal.get(Calendar.MONTH) == monthCal.get(Calendar.MONTH) &&
           cal.get(Calendar.DAY_OF_MONTH) == day
}
