package com.sendwk.app.di

import android.content.Context
import androidx.room.Room
import com.sendwk.app.data.local.SendWkDatabase
import com.sendwk.app.data.local.dao.ChatMessageDao
import com.sendwk.app.data.local.dao.ClientDao
import com.sendwk.app.data.local.dao.TaskDao
import com.sendwk.app.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SendWkDatabase {
        return Room.databaseBuilder(
            context,
            SendWkDatabase::class.java,
            "sendwk_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTaskDao(database: SendWkDatabase): TaskDao = database.taskDao()

    @Provides
    fun provideUserDao(database: SendWkDatabase): UserDao = database.userDao()

    @Provides
    fun provideClientDao(database: SendWkDatabase): ClientDao = database.clientDao()

    @Provides
    fun provideChatMessageDao(database: SendWkDatabase): ChatMessageDao = database.chatMessageDao()
}
