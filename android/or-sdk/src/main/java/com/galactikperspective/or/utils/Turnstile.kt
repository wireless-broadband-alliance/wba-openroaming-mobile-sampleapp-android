package com.galactikperspective.or.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//
//  Turnstile.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2024 Galactik Perspective. All rights reserved.
//

class Turnstile {

    suspend fun getToken(activity: AppCompatActivity): String = suspendCoroutine {
        //TODO replace with production key
        val html = "1x00000000000000000000AA".getHTML()
        activity.lifecycleScope.launch(Dispatchers.Main) {
            val webView = WebView(activity)
            webView.setBackgroundColor(Color.TRANSPARENT)
            webView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            val settings = webView.getSettings()
            @SuppressLint("SetJavaScriptEnabled")
            settings.javaScriptEnabled = true

            val jsInterface = object : JSInterface() {

                @JavascriptInterface
                override fun onTokenReceived(token: String) {
                    it.resume(token)

                    webView.visibility = View.GONE
                    webView.destroy()
                }
            }

            webView.loadData(html, "text/html", "UTF-8")
            webView.addJavascriptInterface(jsInterface, "JSInterface")

            val container = activity.findViewById<ViewGroup>(android.R.id.content)
            container.addView(webView)
        }
    }

    private fun String.getHTML() = ("<html><head>"
            + "<style>"
            + "body, html {"
            + "  margin: 0;"
            + "  padding: 0;"
            + "  height: 100%;"
            + "  width: 100%;"
            + "  display: flex;"
            + "  justify-content: center;"
            + "  align-items: center;"
            + "  background-color: rgba(0, 0, 0, 0.5);"
            + "}"
            + ".cf-turnstile {"
            + "  display: flex;"
            + "  justify-content: center;"
            + "  align-items: center;"
            + "}"
            + "</style>"
            + "</head><body>"
            + "<script src='https://challenges.cloudflare.com/turnstile/v0/api.js'></script>"
            + "<div class='cf-turnstile' data-sitekey='$this' data-callback='onSuccess'></div>"
            + "<script>"
            + "function onSuccess(token) {"
            + "    window.JSInterface.onTokenReceived(token);"
            + "}"
            + "</script>"
            + "</body></html>")

    open class JSInterface() {

        @JavascriptInterface
        open fun onTokenReceived(token: String) {
            Log.i("Token", "A token was received")
        }
    }
}