package com.sendwk.app.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sendwk.app.domain.model.User
import com.sendwk.app.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("USER_PROFILE.DAT", color = Primary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                )
            )
        },
        containerColor = Background
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileHeader(uiState.user)

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "> SYSTEM_INFORMATION",
                    style = MaterialTheme.typography.labelMedium,
                    color = MagentaNeon,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth().border(1.dp, RetroGray, RoundedCornerShape(2.dp)),
                    color = DarkNebula.copy(alpha = 0.5f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val currentUser = uiState.user

                        // ✅ ЗАМЕНЕНО: Вместо phone теперь отображаются должность, компания и заметки
                        InfoRow(Icons.Default.Badge, "POSITION", currentUser?.position ?: "UNASSIGNED")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = RetroGray)

                        InfoRow(Icons.Default.Business, "CORP_ENTITY", currentUser?.company ?: "UNKNOWN")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = RetroGray)

                        InfoRow(Icons.Default.Description, "PROJECT_NOTES", currentUser?.notes ?: "NULL")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = RetroGray)

                        if (currentUser?.email != null) {
                            InfoRow(Icons.Default.Email, "DATA_MAIL", currentUser.email)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = RetroGray)
                        }

                        val dateStr = if (currentUser != null && currentUser.createdAt > 0) {
                            SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(Date(currentUser.createdAt))
                        } else {
                            "UNKNOWN"
                        }
                        InfoRow(Icons.Default.CalendarToday, "INIT_DATE", dateStr)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(1.dp, Color.Red.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Red
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("TERMINATE_SESSION", fontWeight = FontWeight.Bold)
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Primary)
            }
        }
    }
}

@Composable
private fun ProfileHeader(user: User?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(120.dp).border(2.dp, CyanNeon, RoundedCornerShape(2.dp)),
            shape = RoundedCornerShape(2.dp),
            color = DarkNebula
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(80.dp),
                    tint = CyanNeon.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = (user?.name ?: "UNKNOWN").uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = GhostWhite
        )

        // ✅ ДОБАВЛЕНО: Индикатор гостя
        if (user?.isGuest == true) {
            Text(
                text = "[GUEST_ACCESS]",
                style = MaterialTheme.typography.labelMedium,
                color = MagentaNeon,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        user?.email?.let { email ->
            Text(
                text = email.lowercase(),
                style = MaterialTheme.typography.labelMedium,
                color = OnSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 16.dp),
            tint = MagentaNeon
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = RetroGray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = GhostWhite,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}