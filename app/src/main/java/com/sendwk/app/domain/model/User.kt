package com.sendwk.app.domain.model

data class User(
    val id: String = "",
    val email: String? = null,
    val name: String? = null,
    val position: String? = null,
    val company: String? = null,
    val notes: String? = null,
    val isGuest: Boolean = false,
    val createdAt: Long = 0L,
    val avatarUrl: String? = null
)