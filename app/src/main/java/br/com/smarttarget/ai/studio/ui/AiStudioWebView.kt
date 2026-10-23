package br.com.smarttarget.ai.studio.ui

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.smarttarget.ai.studio.security.SecurityManager
import br.com.smarttarget.ai.studio.webview.ChromiumWebChromeClient
import br.com.smarttarget.ai.studio.webview.ChromiumWebViewClient
import br.com.smarttarget.ai.studio.webview.WebViewState
import java.io.File

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AiStudioWebView(
    webView: WebView,
    state: WebViewState,
    onStateUpdated: (url: String?, title: String?, canGoBack: Boolean, canGoForward: Boolean, isLoading: Boolean, hasError: Boolean, sslError: String?) -> Unit,
    onProgressUpdate: (Int) -> Unit,
    onTitleUpdate: (String) -> Unit,
    onOpenFileChooser: (ValueCallback<Array<Uri>>?, WebChromeClient.FileChooserParams?) -> Boolean,
    onPermissionRequested: (PermissionRequest?) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Initialize Chromium WebSettings with DOM Storage, Application Cache and Security
    DisposableEffect(webView) {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        settings.allowFileAccess = false
        settings.allowContentAccess = true

        // Configure application cache directory and network-adaptive cache mode
        val cacheDir = File(context.cacheDir, "webview_cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        @Suppress("DEPRECATION")
        val isConnected = cm?.activeNetworkInfo?.isConnectedOrConnecting == true
        settings.cacheMode = if (isConnected) WebSettings.LOAD_DEFAULT else WebSettings.LOAD_CACHE_ELSE_NETWORK

        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false

        // Enable third-party cookies for seamless Google Accounts authorization
        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
            flush()
        }

        // Setup Download Listener for exporting code, prompts, and assets
        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, _ ->
            try {
                val request = DownloadManager.Request(Uri.parse(url)).apply {
                    setMimeType(mimetype)
                    addRequestHeader("User-Agent", userAgent)
                    setDescription("Baixando arquivo do Google AI Studio...")
                    setTitle(contentDisposition.takeIf { !it.isNullOrBlank() } ?: "ai-studio-export")
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "AIStudio_download")
                }
                val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
                dm?.enqueue(request)
                Toast.makeText(context, "Download iniciado", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Erro ao iniciar download: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }

        onDispose {
            // Lifecycle cleanup handled by Activity
        }
    }

    // Update Clients
    webView.webViewClient = remember(context) {
        ChromiumWebViewClient(context, onStateUpdated)
    }

    webView.webChromeClient = remember {
        ChromiumWebChromeClient(
            onProgressUpdate = onProgressUpdate,
            onTitleUpdate = onTitleUpdate,
            onOpenFileChooser = onOpenFileChooser,
            onPermissionRequested = onPermissionRequested
        )
    }

    // Update User-Agent according to Desktop/Mobile mode toggle
    DisposableEffect(state.isDesktopMode) {
        if (state.isDesktopMode) {
            webView.settings.userAgentString = WebViewState.DESKTOP_USER_AGENT
            webView.settings.useWideViewPort = true
            webView.settings.loadWithOverviewMode = true
        } else {
            // Use dedicated Chrome Mobile UA to ensure Google Accounts OAuth accepts the browser
            webView.settings.userAgentString = WebViewState.MOBILE_USER_AGENT
            webView.settings.useWideViewPort = true
            webView.settings.loadWithOverviewMode = true
        }
        onDispose { }
    }

    // Wrap WebView with SwipeRefreshLayout for smooth pull-to-refresh
    AndroidView(
        factory = { ctx ->
            SwipeRefreshLayout(ctx).apply {
                setColorSchemeColors(
                    Color.parseColor("#1B6EF3"),
                    Color.parseColor("#B588F8"),
                    Color.parseColor("#70D2FA")
                )
                setProgressBackgroundColorSchemeColor(Color.parseColor("#1E1F20"))

                // Scroll detection: pull-to-refresh only triggers when scrolled to the very top
                setOnChildScrollUpCallback { _, _ ->
                    webView.canScrollVertically(-1)
                }

                setOnRefreshListener {
                    onRefresh()
                }

                // Attach WebView safely
                (webView.parent as? ViewGroup)?.removeView(webView)
                addView(
                    webView,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            }
        },
        update = { swipeRefresh ->
            swipeRefresh.isRefreshing = state.isLoading
        },
        modifier = modifier.fillMaxSize()
    )
}
