package com.sendwk.app.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sendwk.app.presentation.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "splash_glow")
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    var systemBooting by remember { mutableStateOf(false) }
    val bootAlpha by animateFloatAsState(
        targetValue = if (systemBooting) 1f else 0f,
        animationSpec = tween(500),
        label = "bootAlpha"
    )

    LaunchedEffect(Unit) {
        delay(500)
        systemBooting = true
        delay(3000)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
            .drawWithContent {
                drawContent()
                // CRT Scanline effect
                for (i in 0 until size.height.toInt() step 6) {
                    drawLine(
                        color = Color.Black.copy(alpha = 0.2f),
                        start = Offset(0f, i.toFloat()),
                        end = Offset(size.width, i.toFloat()),
                        strokeWidth = 1f
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(bootAlpha)
        ) {
            // Retro Terminal Icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(2.dp, CyanNeon.copy(alpha = glowAlpha), RoundedCornerShape(2.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = null,
                    tint = CyanNeon,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "SEND_WK.OS_V3.0",
                style = MaterialTheme.typography.displayLarge,
                color = CyanNeon,
                fontSize = 28.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "INITIALIZING_KERNEL...",
                style = MaterialTheme.typography.labelMedium,
                color = MagentaNeon,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(64.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(200.dp)
                    .height(2.dp),
                color = CyanNeon,
                trackColor = RetroGray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "ACCESSING_ENCRYPTED_STORAGE",
                fontSize = 10.sp,
                color = RetroGray,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
