package com.sendwk.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sendwk.app.data.local.dao.ChatMessageDao
import com.sendwk.app.data.local.dao.ClientDao
import com.sendwk.app.data.local.dao.TaskDao
import com.sendwk.app.data.local.dao.UserDao
import com.sendwk.app.data.local.entities.ChatMessageEntity
import com.sendwk.app.data.local.entities.ClientEntity
import com.sendwk.app.data.local.entities.ProjectEntity
import com.sendwk.app.data.local.entities.TaskEntity
import com.sendwk.app.data.local.entities.UserEntity

@Database(
    entities = [
        TaskEntity::class,
        ProjectEntity::class,
        ClientEntity::class,
        UserEntity::class,
        ChatMessageEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class SendWkDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun clientDao(): ClientDao
    abstract fun userDao(): UserDao
    abstract fun chatMessageDao(): ChatMessageDao
}