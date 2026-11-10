package com.sendwk.app.presentation.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sendwk.app.data.local.entities.UserEntity
import com.sendwk.app.data.repository.UserRepositoryImpl
import com.sendwk.app.utils.OAuthManager
import com.sendwk.app.utils.SecurityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val oAuthManager: OAuthManager,
    private val securityManager: SecurityManager,
    private val userRepository: UserRepositoryImpl
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                if (email.isBlank() || password.isBlank()) {
                    _error.value = "Введите email и пароль"
                    _isLoading.value = false
                    return@launch
                }

                // Имитация успешного входа (замените на реальный API вызов)
                val userId = "user_${System.currentTimeMillis()}"
                val displayName = email.substringBefore("@").replaceFirstChar { it.uppercase() }

                val user = UserEntity(
                    id = userId,
                    email = email,
                    displayName = displayName,
                    company = "SendWk Corp",
                    position = "Разработчик",
                    phone = "+7 (999) 123-45-67",
                    createdAt = System.currentTimeMillis(),
                    lastLoginAt = System.currentTimeMillis()
                )

                userRepository.saveUser(user)
                securityManager.saveToken("token_$userId")

            } catch (e: Exception) {
                _error.value = "Ошибка входа: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun startOAuthLogin() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                oAuthManager.performLogin { result: Result<String> ->
                    result.onSuccess { token ->
                        securityManager.saveToken(token)
                        _isLoading.value = false
                        // Здесь можно вызвать коллбек успеха, если он передан во ViewModel
                    }
                    result.onFailure { exception ->
                        _error.value = "Ошибка OAuth: ${exception.message}"
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun handleOAuthResponse(intent: Intent?) {
        viewModelScope.launch {
            _isLoading.value = true
            oAuthManager.handleResponse(intent) { result: Result<String> ->
                result.onSuccess { authState ->
                    _error.value = null
                    securityManager.saveToken(authState)
                }.onFailure { exception ->
                    _error.value = "Ошибка OAuth: ${exception.message}"
                }
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        securityManager.clearTokens()
        viewModelScope.launch {
            userRepository.clearUser()
        }
    }

    fun isLoggedIn(): Boolean = securityManager.isLoggedIn()
}