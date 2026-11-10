package com.sendwk.app.data.local.dao

import androidx.room.*
import com.sendwk.app.data.local.entities.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY priority DESC, id DESC")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY priority DESC")
    suspend fun getTasksByStatus(status: String): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE isSynced = 0")
    suspend fun getUnsyncedTasks(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}