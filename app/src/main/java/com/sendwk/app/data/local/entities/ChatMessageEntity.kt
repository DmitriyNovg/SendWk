package com.sendwk.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sendwk.app.domain.model.AttachmentType

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String = System.currentTimeMillis().toString(),
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isMe: Boolean = false,
    val attachmentUrl: String? = null,
    val attachmentType: String? = null, // IMAGE, VIDEO, FILE
    val fileName: String? = null,
    val replyToMessageId: String? = null
)