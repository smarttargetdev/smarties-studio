package com.example.webview

import android.net.Uri
import android.os.Message
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

class ChromiumWebChromeClient(
    private val onProgressUpdate: (Int) -> Unit,
    private val onTitleUpdate: (String) -> Unit,
    private val onOpenFileChooser: (ValueCallback<Array<Uri>>?, FileChooserParams?) -> Boolean,
    private val onPermissionRequested: (PermissionRequest?) -> Unit
) : WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        onProgressUpdate(newProgress)
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        title?.let { onTitleUpdate(it) }
    }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        return onOpenFileChooser(filePathCallback, fileChooserParams)
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        onPermissionRequested(request)
    }

    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        // Support Google Sign-In popup windows within the same WebView session
        val transport = resultMsg?.obj as? WebView.WebViewTransport
        transport?.webView = view
        resultMsg?.sendToTarget()
        return true
    }
}
