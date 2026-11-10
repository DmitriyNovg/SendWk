package com.sendwk.app.presentation.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sendwk.app.data.local.entities.TaskEntity
import com.sendwk.app.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    onNavigateToChat: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit, // ✅ ТЕПЕРЬ ИСПОЛЬЗУЕТСЯ
    onNavigateToCalendar: () -> Unit,
    onLogout: () -> Unit,             // ✅ ТЕПЕРЬ ИСПОЛЬЗУЕТСЯ
    onNavigateToCalls: () -> Unit     // ✅ ТЕПЕРЬ ИСПОЛЬЗУЕТСЯ
) {
    val tasks by viewModel.tasks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val showAddTaskDialog = remember { mutableStateOf(false) }
    val selectedTask = remember { mutableStateOf<TaskEntity?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "OPERATIONS_CENTER",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    IconButton(onClick = onNavigateToChat) {
                        Icon(Icons.Default.ChatBubble, contentDescription = "Chat", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onNavigateToCalls) { // ✅ ДОБАВЛЕНО
                        Icon(Icons.Default.VideoCall, contentDescription = "Calls", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onNavigateToCalendar) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onNavigateToSettings) { // ✅ ДОБАВЛЕНО
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onLogout) { // ✅ ДОБАВЛЕНО
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.error)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog.value = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.shadow(8.dp, RoundedCornerShape(16.dp), ambientColor = CyanNeon, spotColor = CyanNeon)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = CyanNeon)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        Text(
                            text = "ACTIVE_TASKS",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(tasks, key = { it.id }) { task ->
                        SoftCyberpunkTaskCard(
                            task = task,
                            onEdit = {
                                selectedTask.value = task
                                showAddTaskDialog.value = true
                            },
                            onDelete = { viewModel.deleteTask(task) }
                        )
                    }
                }
            }
        }
    }

    if (showAddTaskDialog.value) {
        TaskEntryDialog(
            task = selectedTask.value,
            onDismiss = {
                showAddTaskDialog.value = false
                selectedTask.value = null
            },
            onSave = { task ->
                if (selectedTask.value != null) {
                    viewModel.updateTask(task.copy(id = selectedTask.value!!.id))
                } else {
                    viewModel.addTask(task)
                }
                showAddTaskDialog.value = false
                selectedTask.value = null
            }
        )
    }
}

@Composable
fun SoftCyberpunkTaskCard(
    task: TaskEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val glowColor = when (task.priority) {
        3 -> ErrorNeon
        2 -> WarningNeon
        else -> CyanNeon
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, glowColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .clickable { onEdit() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(4.dp, 40.dp)
                    .background(glowColor, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = task.status,
                    style = MaterialTheme.typography.labelSmall,
                    color = glowColor
                )
                task.deadline?.let {
                    Text(
                        text = "DEADLINE: ${SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(it))}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryDialog(
    task: TaskEntity?,
    onDismiss: () -> Unit,
    onSave: (TaskEntity) -> Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var priority by remember { mutableIntStateOf(task?.priority ?: 1) }
    // ✅ ИСПРАВЛЕНО: mutableLongStateOf вместо mutableStateOf для Long
    var deadline by remember { mutableLongStateOf(task?.deadline ?: (System.currentTimeMillis() + 86400000)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                if (task == null) "NEW_TASK_SIGNAL" else "UPDATE_TASK_SIGNAL",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("IDENTIFIER") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Text("PRIORITY_LEVEL", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(1, 2, 3).forEach { p ->
                        val color = when(p) {
                            3 -> ErrorNeon
                            2 -> WarningNeon
                            else -> CyanNeon
                        }
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text("L$p") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = color.copy(alpha = 0.3f),
                                selectedLabelColor = color
                            )
                        )
                    }
                }

                Text("DEADLINE_CONFIG", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(deadline)),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { deadline += 86400000 }) {
                        Text("+1 DAY")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    TaskEntity(
                        title = title,
                        description = description,
                        priority = priority,
                        status = task?.status ?: "PENDING",
                        projectId = task?.projectId,
                        createdAt = task?.createdAt ?: System.currentTimeMillis(),
                        deadline = deadline
                    )
                )
            }) {
                Text("EXECUTE", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = MaterialTheme.colorScheme.outline)
            }
        }
    )
}