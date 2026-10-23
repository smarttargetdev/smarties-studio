package br.com.smarttarget.ai.studio

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import br.com.smarttarget.ai.studio.ui.SplashScreen
import br.com.smarttarget.ai.studio.auth.AuthManager
import br.com.smarttarget.ai.studio.security.SecurityManager
import br.com.smarttarget.ai.studio.ui.AiStudioBottomNav
import br.com.smarttarget.ai.studio.ui.AiStudioTopBar
import br.com.smarttarget.ai.studio.ui.AiStudioWebView
import br.com.smarttarget.ai.studio.ui.ErrorScreen
import br.com.smarttarget.ai.studio.ui.QuickShortcutsSheet
import br.com.smarttarget.ai.studio.ui.SecurityInfoDialog
import br.com.smarttarget.ai.studio.ui.theme.MyApplicationTheme
import br.com.smarttarget.ai.studio.webview.WebViewState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var webViewInstance: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                var showSplash by remember { mutableStateOf(true) }

                Box(modifier = Modifier.fillMaxSize()) {
                    AiStudioApp(
                        onBindWebView = { webViewInstance = it },
                        onFinishApp = { finish() }
                    )

                    AnimatedVisibility(
                        visible = showSplash,
                        enter = fadeIn(),
                        exit = fadeOut(tween(400))
                    ) {
                        SplashScreen(
                            durationMs = 2000L,
                            onTimeout = { showSplash = false }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        webViewInstance?.destroy()
        webViewInstance = null
        super.onDestroy()
    }
}

@Composable
fun AiStudioApp(
    onBindWebView: (WebView) -> Unit,
    onFinishApp: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val webView = remember {
        WebView(context).apply {
            onBindWebView(this)
            loadUrl(SecurityManager.DEFAULT_HOME_URL)
        }
    }

    var state by remember { mutableStateOf(WebViewState()) }
    var showQuickNav by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }
    var showClearCacheConfirm by remember { mutableStateOf(false) }

    // Custom System Back Button Handler: navigates web history before exiting
    var lastBackPressTime by remember { mutableLongStateOf(0L) }
    BackHandler {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastBackPressTime < 2000L) {
                onFinishApp()
            } else {
                lastBackPressTime = currentTime
                Toast.makeText(
                    context,
                    "Pressione 'Voltar' novamente para sair do AI Studio",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Google Authentication with Device Account
    val handleGoogleAuth: () -> Unit = {
        coroutineScope.launch {
            val result = AuthManager.signInWithDeviceGoogleAccount(context)
            result.onSuccess { email ->
                val targetUrl = AuthManager.buildGoogleChooserUrl(email)
                state = state.copy(hasNetworkError = false)
                webView.loadUrl(targetUrl)
                Toast.makeText(
                    context,
                    if (email != null) "Autenticando conta: $email" else "Abrindo login do Google...",
                    Toast.LENGTH_SHORT
                ).show()
            }.onFailure {
                webView.loadUrl(AuthManager.GOOGLE_SIGNIN_URL)
            }
        }
    }

    // File Chooser Handler for Multimodal AI Studio uploads
    var fileUploadCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uriList = mutableListOf<Uri>()
            result.data?.data?.let { uriList.add(it) }
            val clipData = result.data?.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    uriList.add(clipData.getItemAt(i).uri)
                }
            }
            fileUploadCallback?.onReceiveValue(uriList.toTypedArray())
        } else {
            fileUploadCallback?.onReceiveValue(null)
        }
        fileUploadCallback = null
    }

    // Audio/Microphone Permission Handler for voice prompt input
    var pendingPermissionRequest by remember { mutableStateOf<PermissionRequest?>(null) }
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pendingPermissionRequest?.grant(pendingPermissionRequest?.resources)
        } else {
            pendingPermissionRequest?.deny()
        }
        pendingPermissionRequest = null
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ai_studio_main_scaffold"),
        topBar = {
            AiStudioTopBar(
                state = state,
                onDesktopModeToggle = {
                    val newMode = !state.isDesktopMode
                    state = state.copy(isDesktopMode = newMode)
                    webView.reload()
                },
                onOpenQuickNav = { showQuickNav = true },
                onOpenSecurityInfo = { showSecurityDialog = true },
                onReload = { webView.reload() },
                onClearCache = { showClearCacheConfirm = true },
                onZoomIn = { webView.zoomIn() },
                onZoomOut = { webView.zoomOut() },
                onGoogleSignIn = handleGoogleAuth
            )
        },
        bottomBar = {
            AiStudioBottomNav(
                state = state,
                onBackClick = { if (webView.canGoBack()) webView.goBack() },
                onForwardClick = { if (webView.canGoForward()) webView.goForward() },
                onHomeClick = { webView.loadUrl(SecurityManager.DEFAULT_HOME_URL) },
                onReloadClick = { webView.reload() },
                onDesktopModeToggle = {
                    val newMode = !state.isDesktopMode
                    state = state.copy(isDesktopMode = newMode)
                    webView.reload()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Linear Progress Bar directly at the top of the WebView
            val animatedProgress by animateFloatAsState(
                targetValue = state.progress / 100f,
                animationSpec = tween(durationMillis = 200, easing = LinearEasing),
                label = "webview_progress"
            )

            if (state.isLoading && state.progress < 100) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .testTag("webview_linear_progress"),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                if (state.hasNetworkError) {
                    ErrorScreen(
                        onRetry = {
                            state = state.copy(hasNetworkError = false)
                            webView.reload()
                        },
                        onNavigateHome = {
                            state = state.copy(hasNetworkError = false)
                            webView.loadUrl(SecurityManager.DEFAULT_HOME_URL)
                        }
                    )
                } else {
                    AiStudioWebView(
                        webView = webView,
                        state = state,
                        onStateUpdated = { url, title, canBack, canForward, loading, hasError, sslError ->
                            val currentUrl = url ?: state.url
                            state = state.copy(
                                url = currentUrl,
                                title = title ?: state.title,
                                canGoBack = canBack,
                                canGoForward = canForward,
                                isLoading = loading,
                                hasNetworkError = hasError,
                                securityState = SecurityManager.evaluateSecurity(currentUrl, sslError)
                            )
                        },
                        onProgressUpdate = { progress ->
                            state = state.copy(
                                progress = progress,
                                isLoading = progress < 100
                            )
                        },
                        onTitleUpdate = { title ->
                            state = state.copy(title = title)
                        },
                        onOpenFileChooser = { callback, params ->
                            fileUploadCallback?.onReceiveValue(null)
                            fileUploadCallback = callback
                            val intent = params?.createIntent() ?: Intent(Intent.ACTION_GET_CONTENT).apply {
                                type = "*/*"
                                addCategory(Intent.CATEGORY_OPENABLE)
                            }
                            try {
                                filePickerLauncher.launch(intent)
                                true
                            } catch (e: Exception) {
                                fileUploadCallback?.onReceiveValue(null)
                                fileUploadCallback = null
                                false
                            }
                        },
                        onPermissionRequested = { req ->
                            if (req?.resources?.contains(PermissionRequest.RESOURCE_AUDIO_CAPTURE) == true) {
                                pendingPermissionRequest = req
                                recordAudioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                            } else {
                                req?.deny()
                            }
                        },
                        onRefresh = {
                            webView.reload()
                        }
                    )
                }
            }
        }
    }

    // Quick Navigation Modal Bottom Sheet
    if (showQuickNav) {
        QuickShortcutsSheet(
            onDismiss = { showQuickNav = false },
            onNavigateTo = { targetUrl ->
                state = state.copy(hasNetworkError = false)
                webView.loadUrl(targetUrl)
            }
        )
    }

    // Security Status Dialog
    if (showSecurityDialog) {
        SecurityInfoDialog(
            securityState = state.securityState,
            onDismiss = { showSecurityDialog = false }
        )
    }

    // Clear Cache and Cookies Confirmation Dialog
    if (showClearCacheConfirm) {
        AlertDialog(
            onDismissRequest = { showClearCacheConfirm = false },
            title = { Text("Limpar Dados de Navegação") },
            text = { Text("Deseja limpar o cache, cookies e histórico local do Chromium? Suas sessões ativas serão desconectadas.") },
            confirmButton = {
                Button(
                    onClick = {
                        webView.clearCache(true)
                        webView.clearHistory()
                        CookieManager.getInstance().removeAllCookies {
                            Toast.makeText(context, "Cache e cookies limpos", Toast.LENGTH_SHORT).show()
                        }
                        showClearCacheConfirm = false
                        webView.loadUrl(SecurityManager.DEFAULT_HOME_URL)
                    },
                    modifier = Modifier.testTag("confirm_clear_cache_button")
                ) {
                    Text("Limpar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showClearCacheConfirm = false },
                    modifier = Modifier.testTag("cancel_clear_cache_button")
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
