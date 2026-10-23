package br.com.smarttarget.ai.studio.webview

import br.com.smarttarget.ai.studio.security.SecurityManager
import br.com.smarttarget.ai.studio.security.SecurityState

data class WebViewState(
    val url: String = SecurityManager.DEFAULT_HOME_URL,
    val title: String = "Google AI Studio",
    val progress: Int = 0,
    val isLoading: Boolean = false,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val isDesktopMode: Boolean = false,
    val hasNetworkError: Boolean = false,
    val securityState: SecurityState = SecurityManager.evaluateSecurity(SecurityManager.DEFAULT_HOME_URL)
) {
    companion object {
        const val MOBILE_USER_AGENT =
            "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.102 Mobile Safari/537.36"

        const val DESKTOP_USER_AGENT =
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36"
    }
}
