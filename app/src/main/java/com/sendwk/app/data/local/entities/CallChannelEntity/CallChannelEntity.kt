package com.sendwk.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_channels")
data class CallChannelEntity(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val projectId: String = "",
    val projectName: String = "",
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)