package com.tetrapi.sdk.core

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.tetrapi.sdk.R
import com.tetrapi.sdk.utils.Preferences
import com.tetrapi.sdk.utils.isAppDebuggable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class Turnstile {

    companion object {

        fun siteKey(context: Context): String? {
            val preferences = Preferences(context)
            return siteKey(preferences)
        }

        fun siteKey(preferences: Preferences) = preferences.getString("TURNSTILE_SITE_KEY")
    }

    suspend fun getToken(activity: AppCompatActivity, siteKey: String): Result<String> = withContext(Dispatchers.Main) {
        if (activity.isAppDebuggable()) Result.success("openroaming")
        else suspendCancellableCoroutine {

            val webView = WebView(activity)
            webView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            webView.settings.apply {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
            }

            webView.webViewClient = object : WebViewClient() {

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    if (it.isActive) {
                        val exception = Exception(error?.description?.toString())
                        it.resume(Result.failure(exception))

                        cleanup(activity, webView)
                    }
                }
            }

            val jsInterface = object : JSInterface() {

                @JavascriptInterface
                override fun getSiteKey() = siteKey

                @JavascriptInterface
                override fun onTokenReceived(token: String) {
                    it.resume(Result.success(token))
                    cleanup(activity, webView)
                }
            }

            webView.addJavascriptInterface(jsInterface, "JSInterface")

            val url = String.format(activity.getString(R.string.open_roaming_api), "turnstile/android")
            webView.loadUrl(url)

            val container = activity.findViewById<ViewGroup>(android.R.id.content)
            container.addView(webView)

            it.invokeOnCancellation { cleanup(activity, webView) }
        }
    }

    private fun cleanup(activity: AppCompatActivity, webView: WebView) = activity.runOnUiThread {
        val container = activity.findViewById<ViewGroup>(android.R.id.content)
        container.removeView(webView)

        webView.visibility = View.GONE
        webView.destroy()
    }

    open class JSInterface {

        @JavascriptInterface
        open fun getSiteKey(): String {
            Log.i("Token", "A site key was settled")
            return "1x00000000000000000000AA"
        }

        @JavascriptInterface
        open fun onTokenReceived(token: String) {
            Log.i("Token", "A token was received")
        }
    }
}