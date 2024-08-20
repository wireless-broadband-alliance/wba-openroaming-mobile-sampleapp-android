package com.galactikperspective.or.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.core.view.isGone

//
//  Turnstile.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2024 Galactik Perspective. All rights reserved.
//

class Turnstile {

    fun renderCaptcha(activity: Activity) {
        val webView = WebView(activity)
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val settings = webView.getSettings()
        @SuppressLint("SetJavaScriptEnabled")
        settings.javaScriptEnabled = true

        val jsInterface = JSInterface(this, activity)
        jsInterface.onVerifySuccess = { it ->
            webView.visibility = View.GONE
            onCaptchaSuccess(it)
        }

        jsInterface.onVerifyError = { it ->
            webView.visibility = View.GONE
            onCaptchaError(it)
        }

        //TODO replace with production key
        val html = "1x00000000000000000000AA".getHTML()
        webView.loadData(html, "text/html", "UTF-8")
        webView.addJavascriptInterface(jsInterface, "JSInterface")

        val container = activity.findViewById<ViewGroup>(android.R.id.content)
        container.addView(webView)
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

    var onCaptchaSuccess: (response: String) -> Unit = {
        /** Captcha succeeded */
    }

    var onCaptchaError: (exception: Exception) -> Unit = {
        /** Captcha failed */
    }

    class JSInterface(private val turnstile: Turnstile, private val activity: Activity) {

        @JavascriptInterface
        fun onTokenReceived(token: String) {
            Web().verifyToken(this, activity, token)
        }

        var onVerifySuccess: (response: String) -> Unit = {
            /** Token verification succeeded */
        }

        var onVerifyError: (exception: Exception) -> Unit = {
            /** Token verification failed */
        }
    }
}