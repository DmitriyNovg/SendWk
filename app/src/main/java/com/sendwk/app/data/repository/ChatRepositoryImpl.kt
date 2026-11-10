package com.sendwk.app.data.repository

import com.sendwk.app.data.local.dao.ChatMessageDao
import com.sendwk.app.data.local.entities.ChatMessageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val chatMessageDao: ChatMessageDao
) {

    private val externalScope = CoroutineScope(Dispatchers.IO)

    fun observeMessages(): Flow<List<ChatMessageEntity>> {
        return chatMessageDao.getAllMessages()
            .onStart {
                seedInitialMessagesIfNeeded()
            }
    }

    private suspend fun seedInitialMessagesIfNeeded() {
        if (chatMessageDao.getMessageCount() == 0) {
            val initialMessages = listOf(
                ChatMessageEntity(
                    id = "msg_init_1",
                    senderId = "sys_admin",
                    senderName = "SYS_ADMIN",
                    text = "COMM_LINK_ESTABLISHED. GENERAL_CHANNEL_ONLINE.",
                    timestamp = System.currentTimeMillis() - 60000,
                    isMe = false
                ),
                ChatMessageEntity(
                    id = "msg_init_2",
                    senderId = "sys_bot",
                    senderName = "SENDWK_BOT",
                    text = "Добро пожаловать в общий чат SendWk! Оставьте сообщение для команды.",
                    timestamp = System.currentTimeMillis() - 30000,
                    isMe = false
                )
            )
            chatMessageDao.insertMessages(initialMessages)
        }
    }

    suspend fun sendMessage(text: String, senderId: String, senderName: String) {
        val userMsg = ChatMessageEntity(
            id = "msg_${System.currentTimeMillis()}_${(1000..9999).random()}",
            senderId = senderId,
            senderName = senderName,
            text = text,
            timestamp = System.currentTimeMillis(),
            isMe = true
        )
        chatMessageDao.insertMessage(userMsg)

        // Simulated automated response from SYS_BOT
        externalScope.launch {
            delay(1200)
            val botMsg = ChatMessageEntity(
                id = "msg_reply_${System.currentTimeMillis()}",
                senderId = "sys_bot",
                senderName = "SENDWK_BOT",
                text = "Сообщение принято и зафиксировано в системе: \"$text\"",
                timestamp = System.currentTimeMillis(),
                isMe = false
            )
            chatMessageDao.insertMessage(botMsg)
        }
    }

    suspend fun clearMessages() {
        chatMessageDao.deleteAllMessages()
        seedInitialMessagesIfNeeded()
    }
}
