package com.sendwk.app.presentation.calls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sendwk.app.domain.model.CallChannel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor(
    // private val callRepository: CallRepository // добавь позже
) : ViewModel() {

    private val _channels = MutableStateFlow<List<CallChannel>>(emptyList())
    val channels: StateFlow<List<CallChannel>> = _channels.asStateFlow()

    init {
        loadChannels()
    }

    private fun loadChannels() {
        viewModelScope.launch {
            // Загрузка каналов из репозитория
            // _channels.value = callRepository.getChannels()
        }
    }

    // ✅ ИСПРАВЛЕНО: префикс '_' убирает предупреждение UNUSED_PARAMETER
    fun joinChannel(_channelId: String) {
        viewModelScope.launch {
            // Логика присоединения к каналу
        }
    }

    // ✅ ИСПРАВЛЕНО: префикс '_' убирает предупреждение UNUSED_PARAMETER
    fun createChannel(_channel: CallChannel) {
        viewModelScope.launch {
            // Создание нового канала
        }
    }

    fun showCreateChannelDialog() {
        // Показать диалог создания канала
    }
}