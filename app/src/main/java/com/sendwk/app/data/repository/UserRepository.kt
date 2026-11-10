package com.sendwk.app.data.repository

import com.sendwk.app.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: UserEntity)

    fun observeAllUsers(): Flow<List<UserEntity>>
    suspend fun getAllUsers(): List<UserEntity>
    suspend fun deleteUserById(userId: String)
    suspend fun getCurrentUser(): UserEntity?
    suspend fun clearUser()
}