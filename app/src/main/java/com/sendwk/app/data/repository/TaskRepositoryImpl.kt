package com.sendwk.app.data.repository

import com.sendwk.app.data.local.dao.TaskDao
import com.sendwk.app.data.local.entities.TaskEntity
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) {
    suspend fun getAllTasks(): List<TaskEntity> {
        return taskDao.getAllTasks()
    }

    suspend fun saveTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun getUnsyncedTasks(): List<TaskEntity> {
        return taskDao.getUnsyncedTasks()
    }
}