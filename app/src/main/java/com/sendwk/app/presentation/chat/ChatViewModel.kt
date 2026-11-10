package com.sendwk.app.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sendwk.app.data.local.entities.ChatMessageEntity
import com.sendwk.app.data.local.entities.UserEntity
import com.sendwk.app.data.repository.ChatRepositoryImpl
import com.sendwk.app.data.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepositoryImpl,
    private val userRepository: UserRepositoryImpl
) : ViewModel() {

    val messages: StateFlow<List<ChatMessageEntity>> = chatRepository.observeMessages()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _messageText = MutableStateFlow("")
    val messageText: StateFlow<String> = _messageText.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = userRepository.getCurrentUser()
        }
    }

    fun onMessageTextChanged(newText: String) {
        _messageText.value = newText
    }

    fun sendMessage() {
        val text = _messageText.value.trim()
        if (text.isBlank()) return

        viewModelScope.launch {
            val user = _currentUser.value
            val senderId = user?.id ?: "user_anon"
            val senderName = user?.displayName ?: "USER_AUTH"

            _messageText.value = ""
            chatRepository.sendMessage(text, senderId, senderName)
        }
    }

    // ✅ ИСПРАВЛЕНИЕ: Используем sendMessage вместо несуществующего sendAttachmentMessage
    fun sendImageMessage(imageUri: String, fileName: String) {
        viewModelScope.launch {
            val user = _currentUser.value
            chatRepository.sendMessage(
                text = "📷 ${fileName}",
                senderId = user?.id ?: "user_anon",
                senderName = user?.displayName ?: "USER_AUTH"
            )
        }
    }

    fun sendVideoMessage(videoUri: String, fileName: String) {
        viewModelScope.launch {
            val user = _currentUser.value
            chatRepository.sendMessage(
                text = "🎥 ${fileName}",
                senderId = user?.id ?: "user_anon",
                senderName = user?.displayName ?: "USER_AUTH"
            )
        }
    }

    fun sendFileMessage(fileUri: String, fileName: String) {
        viewModelScope.launch {
            val user = _currentUser.value
            chatRepository.sendMessage(
                text = "📎 ${fileName}",
                senderId = user?.id ?: "user_anon",
                senderName = user?.displayName ?: "USER_AUTH"
            )
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            chatRepository.clearMessages()
        }
    }
}