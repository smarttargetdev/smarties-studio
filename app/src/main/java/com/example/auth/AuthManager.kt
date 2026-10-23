package com.example.auth

import android.content.Context
import android.util.Log
import android.webkit.CookieManager
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

object AuthManager {
    private const val TAG = "AuthManager"

    const val GOOGLE_SIGNIN_URL =
        "https://accounts.google.com/ServiceLogin?service=wise&passive=1209600&continue=https://aistudio.google.com"

    fun buildGoogleChooserUrl(email: String?): String {
        return if (!email.isNullOrBlank()) {
            "https://accounts.google.com/AccountChooser?Email=$email&continue=https://aistudio.google.com"
        } else {
            GOOGLE_SIGNIN_URL
        }
    }

    suspend fun signInWithDeviceGoogleAccount(
        context: Context,
        serverClientId: String? = null
    ): Result<String?> {
        val credentialManager = CredentialManager.create(context)
        return try {
            val clientId = serverClientId?.takeIf { it.isNotBlank() }
                ?: "dummy-client-id.apps.googleusercontent.com"

            val googleIdOption = GetSignInWithGoogleOption.Builder(clientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val response = credentialManager.getCredential(context, request)
            val credential = response.credential

            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val userEmail = googleIdTokenCredential.id
                Log.d(TAG, "Conta Google selecionada: $userEmail")
                Result.success(userEmail)
            } else {
                Result.success(null)
            }
        } catch (e: GetCredentialException) {
            Log.w(TAG, "Fluxo de credenciais cancelado ou sem chave: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Erro na autenticação Google: ${e.message}", e)
            Result.failure(e)
        }
    }

    fun syncCookies() {
        CookieManager.getInstance().flush()
    }
}
