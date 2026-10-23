package br.com.smarttarget.ai.studio.webview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.webkit.SafeBrowsingResponse
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import br.com.smarttarget.ai.studio.security.SecurityManager

class ChromiumWebViewClient(
    private val context: Context,
    private val onStateUpdated: (url: String?, title: String?, canGoBack: Boolean, canGoForward: Boolean, isLoading: Boolean, hasError: Boolean, sslError: String?) -> Unit
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString() ?: return false
        return handleUrlNavigation(view, url)
    }

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url == null) return false
        return handleUrlNavigation(view, url)
    }

    private fun handleUrlNavigation(view: WebView?, url: String): Boolean {
        if (SecurityManager.isAllowedDomain(url)) {
            // Permitted internal navigation in Google AI Studio
            return false
        }

        // External destination: open in external browser for strict sandbox protection
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        onStateUpdated(
            url,
            view?.title,
            view?.canGoBack() == true,
            view?.canGoForward() == true,
            true,
            false,
            null
        )
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        onStateUpdated(
            url,
            view?.title,
            view?.canGoBack() == true,
            view?.canGoForward() == true,
            false,
            false,
            null
        )
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        if (request?.isForMainFrame == true) {
            onStateUpdated(
                view?.url,
                view?.title,
                view?.canGoBack() == true,
                view?.canGoForward() == true,
                false,
                true,
                null
            )
        }
    }

    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler?,
        error: SslError?
    ) {
        // Enforce strict TLS: cancel insecure connections
        handler?.cancel()
        val errorDesc = error?.let { "SSL Error code: ${it.primaryError}" } ?: "Certificado SSL inválido"
        onStateUpdated(
            view?.url,
            view?.title,
            view?.canGoBack() == true,
            view?.canGoForward() == true,
            false,
            true,
            errorDesc
        )
    }

    override fun onSafeBrowsingHit(
        view: WebView?,
        request: WebResourceRequest?,
        threatType: Int,
        callback: SafeBrowsingResponse?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            callback?.backToSafety(true)
        } else {
            super.onSafeBrowsingHit(view, request, threatType, callback)
        }
    }
}
