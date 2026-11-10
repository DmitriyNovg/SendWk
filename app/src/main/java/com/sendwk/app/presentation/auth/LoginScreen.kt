package com.sendwk.app.presentation.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sendwk.app.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onOpenRegister: () -> Unit = {},       // ✅ ДОБАВЛЕНО
    onLoginAsGuest: () -> Unit = {}        // ✅ ДОБАВЛЕНО
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
    ) {
        // Futuristic background elements
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MagentaNeon.copy(alpha = 0.15f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Retro-Futuristic Logo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(2.dp, CyanNeon, RoundedCornerShape(2.dp)), // Hard edges for retro feel
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = "Logo",
                    tint = CyanNeon,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SEND_WK.SYS",
                style = MaterialTheme.typography.displayLarge,
                color = CyanNeon,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ESTABLISHED_1984_READY",
                style = MaterialTheme.typography.labelMedium,
                color = MagentaNeon
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Login Terminal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp), // Retro terminal style
                border = androidx.compose.foundation.BorderStroke(1.dp, RetroGray),
                colors = CardDefaults.cardColors(containerColor = DarkNebula.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "> ACCESS_AUTH",
                        color = CyanNeon,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("USER_IDENTIFIER", color = RetroGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp), // ✅ Добавлено для стиля
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanNeon,
                            unfocusedBorderColor = RetroGray,
                            focusedTextColor = CyanNeon,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("PASS_KEY", color = RetroGray) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp), // ✅ Добавлено для стиля
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MagentaNeon,
                            unfocusedBorderColor = RetroGray,
                            focusedTextColor = MagentaNeon,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    if (error != null) {
                        Text(
                            text = "!! ${error?.uppercase()} !!",
                            color = Color.Red,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Основная кнопка входа
                    Button(
                        onClick = {
                            viewModel.login(email.value, password.value)
                            // Примечание: в идеале onLoginSuccess() должен вызываться после успешного ответа от ViewModel,
                            // но оставляю как в твоем исходном коде.
                            onLoginSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
                        enabled = !isLoading
                    ) {
                        Text("INITIATE_SESSION", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    // ✅ НОВЫЕ КНОПКИ (Встроены в стиль терминала)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            viewModel.loginAsGuest()
                            onLoginAsGuest()
                            onLoginSuccess()
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(0.dp), // Острые углы как у терминала
                        border = androidx.compose.foundation.BorderStroke(1.dp, MagentaNeon),
                        enabled = !isLoading
                    ) {
                        Text("ENTER_AS_GUEST", color = MagentaNeon, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = onOpenRegister,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("> REGISTER_NEW_PROFILE", color = CyanNeon, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}