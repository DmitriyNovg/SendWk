package com.sendwk.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val url: String? = null,
    val type: String? = null, // например: "pdf", "docx", "image"
    val size: Long = 0L,
    val projectId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)