package com.example.youtubewebview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * MainActivity: The single Activity of the application.
 * Uses Jetpack Compose to host a fullscreen WebView loading mobile YouTube.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YouTubeWebViewScreen(
                url = "https://m.youtube.com",
                onExit = { finish() }
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubeWebViewScreen(
    url: String,
    onExit: () -> Unit
) {
    // Reference to the active WebView instance
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    // REQUIREMENT: Handle the back button.
    // If the WebView can go back, go back in the WebView; otherwise exit the app.
    BackHandler(enabled = true) {
        if (webViewInstance?.canGoBack() == true) {
            webViewInstance?.goBack()
        } else {
            onExit()
        }
    }

    // REQUIREMENT: Do NOT add a toolbar, address bar, or any extra UI.
    // Just the WebView filling the whole screen.
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // REQUIREMENT: Enable JavaScript in the WebView
                settings.javaScriptEnabled = true

                // REQUIREMENT: Enable DOM storage (setDomStorageEnabled(true)) — YouTube needs it
                settings.domStorageEnabled = true

                // Additional optimal settings for smooth video playback & modern web compatibility
                settings.databaseEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                settings.builtInZoomControls = false
                settings.displayZoomControls = false
                settings.cacheMode = WebSettings.LOAD_DEFAULT

                // REQUIREMENT: Set a WebViewClient so any link clicked inside the page
                // stays inside the WebView (do NOT open the system browser).
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val targetUrl = request?.url?.toString() ?: return false
                        view?.loadUrl(targetUrl)
                        // Returning true indicates that the host app handles the URL directly inside the WebView
                        return true
                    }

                    @Deprecated("Deprecated for API < 24")
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        if (url != null) {
                            view?.loadUrl(url)
                        }
                        return true
                    }
                }

                // WebChromeClient supports HTML5 video fullscreen, audio focus & media controls
                webChromeClient = WebChromeClient()

                // REQUIREMENT: On launch, immediately loads https://m.youtube.com
                loadUrl(url)
                webViewInstance = this
            }
        },
        update = { webView ->
            webViewInstance = webView
        }
    )

    // Clean up WebView instance on disposal to prevent activity memory leaks
    DisposableEffect(Unit) {
        onDispose {
            webViewInstance?.apply {
                stopLoading()
                destroy()
            }
        }
    }
}