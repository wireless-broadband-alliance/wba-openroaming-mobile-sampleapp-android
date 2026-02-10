package com.tetrapi.sdk.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tetrapi.sdk.R
import com.tetrapi.sdk.utils.Preferences
import com.tetrapi.sdk.utils.isAppDebuggable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Turnstile {

    companion object {

        fun siteKey(context: Context): String? {
            val preferences = Preferences(context)
            return siteKey(preferences)
        }

        fun siteKey(preferences: Preferences) = preferences.getString("TURNSTILE_SITE_KEY")
    }

    suspend fun getToken(activity: AppCompatActivity, siteKey: String): String = suspendCoroutine {
        // if (activity.isAppDebuggable()) it.resume("openroaming")
        // else
        // TODO
            activity.lifecycleScope.launch(Dispatchers.Main) {
            val webView = WebView(activity)
            webView.setBackgroundColor(Color.TRANSPARENT)
            webView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            val settings = webView.settings
            @SuppressLint("SetJavaScriptEnabled")
            settings.javaScriptEnabled = true

            val jsInterface = object : JSInterface() {

                @JavascriptInterface
                override fun getSiteKey() = siteKey

                @JavascriptInterface
                override fun onTokenReceived(token: String) {
                    it.resume(token)

                    activity.runOnUiThread {
                        webView.visibility = View.GONE
                        webView.destroy()
                    }
                }
            }

            webView.addJavascriptInterface(jsInterface, "JSInterface")

            val url = String.format(activity.getString(R.string.open_roaming_api), "turnstile/android")
            webView.loadUrl(url)

            val container = activity.findViewById<ViewGroup>(android.R.id.content)
            container.addView(webView)
        }
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