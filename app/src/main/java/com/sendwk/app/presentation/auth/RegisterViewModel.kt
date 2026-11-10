package com.sendwk.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sendwk.app.data.local.entities.UserEntity
import com.sendwk.app.data.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl
    // private val sessionManager: SessionManager // Раскомментируй, если у тебя есть класс для сохранения токена/сессии
) : ViewModel() {

    val profiles: StateFlow<List<UserEntity>> = userRepository.observeAllUsers()
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())

    private val _showDeleteDialog = MutableStateFlow<UserEntity?>(null)
    val showDeleteDialog: StateFlow<UserEntity?> = _showDeleteDialog.asStateFlow()

    fun registerProfile(
        name: String,
        email: String,
        position: String,
        company: String,
        notes: String
    ) {
        viewModelScope.launch {
            val newUser = UserEntity(
                id = UUID.randomUUID().toString(),
                displayName = name,
                email = email,
                position = position,
                company = company,
                notes = notes,
                isGuest = false,
                lastLoginAt = System.currentTimeMillis()
            )
            userRepository.saveUser(newUser)
            // sessionManager.saveToken(newUser.id) // Раскомментируй для сохранения сессии
        }
    }

    fun loginWithProfile(user: UserEntity) {
        viewModelScope.launch {
            val updatedUser = user.copy(lastLoginAt = System.currentTimeMillis())
            userRepository.saveUser(updatedUser)
            // sessionManager.saveToken(updatedUser.id) // Раскомментируй для сохранения сессии
        }
    }

    fun requestDeleteProfile(user: UserEntity) {
        _showDeleteDialog.value = user
    }

    fun confirmDeleteProfile() {
        val user = _showDeleteDialog.value ?: return
        viewModelScope.launch {
            userRepository.deleteUserById(user.id)
            _showDeleteDialog.value = null
        }
    }

    fun dismissDeleteDialog() {
        _showDeleteDialog.value = null
    }
}