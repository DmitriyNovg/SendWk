package com.sendwk.app.domain.model
data class MessageAttachment(
    val id: String = "",
    val type: AttachmentType = AttachmentType.IMAGE,
    val url: String = "",
    val fileName: String = "",
    val fileSize: Long = 0L,
    val mimeType: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class AttachmentType {
    IMAGE,
    VIDEO,
    AUDIO,
    FILE
}