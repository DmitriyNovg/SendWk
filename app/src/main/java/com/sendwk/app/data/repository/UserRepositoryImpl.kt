package com.sendwk.app.data.repository

import com.sendwk.app.data.local.dao.UserDao
import com.sendwk.app.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun saveUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override fun observeAllUsers(): Flow<List<UserEntity>> {
        return userDao.observeAllUsers()
    }

    override suspend fun getAllUsers(): List<UserEntity> {
        return userDao.getAllUsers()
    }

    override suspend fun deleteUserById(userId: String) {
        userDao.deleteUserById(userId)
    }

    override suspend fun getCurrentUser(): UserEntity? {
        return userDao.getAllUsers().firstOrNull()
    }

    override suspend fun clearUser() {
        // Если нужно очищать таблицу при логауте
        // userDao.clearAllUsers()
    }
}