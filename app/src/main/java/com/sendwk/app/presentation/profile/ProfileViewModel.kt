package com.sendwk.app.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sendwk.app.data.repository.UserRepositoryImpl
import com.sendwk.app.domain.model.User
import com.sendwk.app.utils.SecurityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val inProgressTasks: Int = 0,
    val daysInSystem: Int = 0
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl,
    private val securityManager: SecurityManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val entity = userRepository.getCurrentUser()
                if (entity != null) {
                    val days = TimeUnit.MILLISECONDS.toDays(
                        System.currentTimeMillis() - entity.createdAt
                    ).toInt().coerceAtLeast(1)

                    // ✅ ИСПРАВЛЕНО: Убрано phone, добавлены position, company, notes, isGuest
                    val domainUser = User(
                        id = entity.id,
                        email = entity.email,
                        name = entity.displayName,
                        position = entity.position,
                        company = entity.company,
                        notes = entity.notes,
                        isGuest = entity.isGuest,
                        createdAt = entity.createdAt,
                        avatarUrl = null
                    )

                    _uiState.value = _uiState.value.copy(
                        user = domainUser,
                        daysInSystem = days,
                        totalTasks = 12,
                        completedTasks = 8,
                        inProgressTasks = 3,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "User not found")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun logout() {
        securityManager.clearTokens()
        viewModelScope.launch {
            userRepository.clearUser()
        }
    }
}