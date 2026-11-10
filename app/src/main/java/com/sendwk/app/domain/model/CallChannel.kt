package com.sendwk.app.domain.model

data class CallChannel(
    val id: String = "",
    val name: String = "",
    val projectId: String = "",
    val projectName: String = "",
    val participants: List<String> = emptyList(),
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class CallSession(
    val id: String = "",
    val channelId: String = "",
    val initiatorId: String = "",
    val participants: List<String> = emptyList(),
    val type: CallType = CallType.VOICE,
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val isActive: Boolean = false
)

enum class CallType {
    VOICE,
    VIDEO
}