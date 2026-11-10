package com.sendwk.app.presentation.calls

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sendwk.app.domain.model.CallChannel
import com.sendwk.app.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallsScreen(
    onNavigateBack: () -> Unit,
    viewModel: CallsViewModel = hiltViewModel()
) {
    val channels by viewModel.channels.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "PROJECT_CALL_CHANNELS",
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(channels) { channel ->
                CallChannelCard(
                    channel = channel,
                    onJoinCall = { viewModel.joinChannel(channel.id) },
                    onCreateCall = { viewModel.createChannel(channel) }
                )
            }

            item {
                Button(
                    onClick = { viewModel.showCreateChannelDialog() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("СОЗДАТЬ КАНАЛ")
                }
            }
        }
    }
}

@Composable
fun CallChannelCard(
    channel: CallChannel,
    onJoinCall: () -> Unit,
    onCreateCall: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = DarkNebula.copy(alpha = 0.9f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = channel.name,
                        fontWeight = FontWeight.Bold,
                        color = GhostWhite,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Project: ${channel.projectName}",
                        color = MagentaNeon,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${channel.participants.size} participants",
                        color = RetroGray,
                        fontSize = 11.sp
                    )
                }

                Row {
                    IconButton(onClick = onCreateCall) {
                        Icon(
                            imageVector = Icons.Default.VideoCall,
                            contentDescription = "Видео звонок",
                            tint = YellowNeon
                        )
                    }
                    IconButton(onClick = onJoinCall) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Голосовой звонок",
                            tint = SuccessNeon
                        )
                    }
                }
            }

            if (channel.isActive) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = SuccessNeon.copy(alpha = 0.2f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "● LIVE NOW",
                        color = SuccessNeon,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}