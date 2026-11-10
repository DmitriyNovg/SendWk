package com.sendwk.app.data.local.dao

import androidx.room.*
import com.sendwk.app.data.local.entities.CallChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CallChannelDao {
    @Query("SELECT * FROM call_channels ORDER BY createdAt DESC")
    fun observeChannels(): Flow<List<CallChannelEntity>>

    @Query("SELECT * FROM call_channels WHERE projectId = :projectId")
    fun observeChannelsByProject(projectId: String): Flow<List<CallChannelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(channel: CallChannelEntity)

    @Delete
    suspend fun deleteChannel(channel: CallChannelEntity)

    @Query("UPDATE call_channels SET isActive = :isActive WHERE id = :channelId")
    suspend fun updateChannelActiveStatus(channelId: String, isActive: Boolean)
}