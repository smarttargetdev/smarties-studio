package br.com.smarttarget.ai.studio.security

import android.net.Uri

data class SecurityState(
    val currentUrl: String = "https://aistudio.google.com",
    val host: String = "aistudio.google.com",
    val isHttps: Boolean = true,
    val isAllowedDomain: Boolean = true,
    val isSafeBrowsingActive: Boolean = true,
    val sslErrorDetails: String? = null
)

object SecurityManager {
    const val DEFAULT_HOME_URL = "https://aistudio.google.com"

    private val ALLOWED_DOMAINS = setOf(
        "aistudio.google.com",
        "makersuite.google.com",
        "accounts.google.com",
        "myaccount.google.com",
        "ai.google.dev",
        "developers.google.com",
        "google.com",
        "gstatic.com",
        "googleapis.com",
        "googleusercontent.com",
        "accounts.youtube.com"
    )

    fun isAllowedDomain(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        val uri = try {
            Uri.parse(url)
        } catch (_: Exception) {
            return false
        }
        val host = uri.host?.lowercase() ?: return false

        return ALLOWED_DOMAINS.any { allowed ->
            host == allowed || host.endsWith(".$allowed")
        }
    }

    fun isGoogleStudioDomain(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        val uri = try {
            Uri.parse(url)
        } catch (_: Exception) {
            return false
        }
        val host = uri.host?.lowercase() ?: return false
        return host == "aistudio.google.com" || host == "makersuite.google.com"
    }

    fun isSecureHttps(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        return url.startsWith("https://", ignoreCase = true)
    }

    fun extractDisplayHost(url: String?): String {
        if (url.isNullOrBlank()) return "aistudio.google.com"
        return try {
            Uri.parse(url).host ?: url
        } catch (_: Exception) {
            "aistudio.google.com"
        }
    }

    fun evaluateSecurity(url: String?, sslError: String? = null): SecurityState {
        val safeUrl = url ?: DEFAULT_HOME_URL
        val host = extractDisplayHost(safeUrl)
        val https = isSecureHttps(safeUrl)
        val allowed = isAllowedDomain(safeUrl)
        return SecurityState(
            currentUrl = safeUrl,
            host = host,
            isHttps = https,
            isAllowedDomain = allowed,
            isSafeBrowsingActive = true,
            sslErrorDetails = sslError
        )
    }
}
