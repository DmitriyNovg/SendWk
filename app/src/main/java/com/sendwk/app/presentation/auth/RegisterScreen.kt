package com.sendwk.app.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sendwk.app.data.local.entities.UserEntity
import com.sendwk.app.presentation.theme.* // Убедись, что здесь импортируются твои цвета (CyanNeon, MagentaNeon и т.д.)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onLoginAsUser: (UserEntity) -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val profiles by viewModel.profiles.collectAsState()
    val userToDelete by viewModel.showDeleteDialog.collectAsState()

    var showForm by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PROFILE_REGISTRY", color = Primary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Primary)
                    }
                },
                actions = {
                    IconButton(onClick = { showForm = !showForm }) {
                        Icon(Icons.Default.Add, "Add Profile", tint = CyanNeon)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {

            // Форма регистрации (появляется при нажатии на +)
            if (showForm) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkNebula),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("FULL_NAME") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("EMAIL") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = position, onValueChange = { position = it }, label = { Text("POSITION") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("COMPANY") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("PROJECT_NOTES") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )
                        Button(
                            onClick = {
                                viewModel.registerProfile(name, email, position, company, notes)
                                name = ""; email = ""; position = ""; company = ""; notes = ""
                                showForm = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = CyanNeon)
                        ) {
                            Text("SAVE_PROFILE", color = Background, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Список профилей
            Text("AVAILABLE_PROFILES", style = MaterialTheme.typography.labelMedium, color = MagentaNeon, modifier = Modifier.padding(bottom = 8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(profiles) { user ->
                    ProfileCard(
                        user = user,
                        onClick = {
                            viewModel.loginWithProfile(user)
                            onLoginAsUser(user)
                        },
                        onDelete = { viewModel.requestDeleteProfile(user) }
                    )
                }
            }
        }
    }

    // Диалог подтверждения удаления
    if (userToDelete != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            title = { Text("CONFIRM_DELETION", color = ErrorNeon) },
            text = { Text("Delete profile '${userToDelete?.displayName}'? This cannot be undone.", color = GhostWhite) },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmDeleteProfile() }) {
                    Text("DELETE", color = ErrorNeon)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                    Text("CANCEL", color = GhostWhite)
                }
            },
            containerColor = DarkNebula
        )
    }
}

@Composable
fun ProfileCard(user: UserEntity, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = DarkNebula.copy(alpha = 0.8f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (user.isGuest) MagentaNeon else CyanNeon)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = user.displayName, fontWeight = FontWeight.Bold, color = GhostWhite, fontSize = 16.sp)
                    if (user.isGuest) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "[GUEST]", color = MagentaNeon, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                }
                if (!user.position.isNullOrBlank()) {
                    Text(text = user.position, color = CyanNeon, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
                if (!user.notes.isNullOrBlank()) {
                    Text(text = "📌 ${user.notes}", color = RetroGray, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorNeon)
            }
        }
    }
}