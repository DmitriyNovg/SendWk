package com.sendwk.app.utils

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OAuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun performLogin(onResult: (Result<String>) -> Unit) {
        onResult(Result.success("mock_oauth_token_123"))
    }

    fun handleResponse(intent: Intent?, onResult: (Result<String>) -> Unit) {
        if (intent != null) {
            onResult(Result.success("mock_oauth_token_from_intent"))
        } else {
            onResult(Result.failure(Exception("Пустой Intent")))
        }
    }
}
