package com.sendwk.app.data.repository

import com.sendwk.app.data.local.dao.UserDao
import com.sendwk.app.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun saveUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getCurrentUser(): UserEntity? {
        return userDao.getCurrentUser()
    }

    fun observeCurrentUser(userId: String): Flow<UserEntity?> {
        return userDao.observeUserById(userId)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    suspend fun clearUser() {
        userDao.deleteAllUsers()
    }
}