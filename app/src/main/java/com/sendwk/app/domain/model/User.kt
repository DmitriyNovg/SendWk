// User.kt
package com.sendwk.app.domain.model

data class User(
    val id: String = "",
    val phone: String? = null,
    val email: String? = null,
    val name: String? = null,
    val createdAt: Long = 0L,
    val avatarUrl: String? = null
)