package com.sendwk.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val email: String? = null,
    val position: String? = null,      // Должность
    val company: String? = null,       // Компания
    val notes: String? = null,         // Пометки: за какой проект отвечает
    val isGuest: Boolean = false,      // Профиль гостя
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
)