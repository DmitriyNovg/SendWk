package com.sendwk.app.presentation.settings

import androidx.lifecycle.ViewModel
import com.sendwk.app.utils.SecurityManager
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val securityManager: SecurityManager
) : ViewModel() {
    val isDarkTheme: StateFlow<Boolean> = securityManager.isDarkTheme

    fun toggleTheme() {
        securityManager.setDarkTheme(!isDarkTheme.value)
    }
}
