package com.tetrapi.sdk.core

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.tetrapi.sdk.core.Turnstile.JSInterface
import com.tetrapi.sdk.utils.Preferences

//
//  SAML.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class SAML {

    fun start(activity: AppCompatActivity) {
        val webView = WebView(activity)
        webView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        webView.settings.apply {
            @SuppressLint("SetJavaScriptEnabled")
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                onSAMLError(error?.description?.toString())
                cleanup(activity, webView)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.evaluateJavascript(script, null)
            }
        }

        val jsInterface = object : JSInterface() {

            @JavascriptInterface
            fun onSamlResponse(response: String) {
                onSAMLSuccess(response)
                cleanup(activity, webView)
            }
        }

        webView.addJavascriptInterface(jsInterface, "JSInterface")

        val url = Preferences(activity).getString("SAML_START") ?: return onSAMLError("TODO handle empty start!")
        webView.loadUrl(url)

        val container = activity.findViewById<ViewGroup>(android.R.id.content)
        container.addView(webView)
    }

    private val script = """
        (function() {
            function capture() {
                var samlInput = document.querySelector('input[name="SAMLResponse"]');
                if (samlInput && samlInput.value) {
                    JSInterface.onSamlResponse(samlInput.value);
                }
            }
            capture(); 
            
            var originalSubmit = HTMLFormElement.prototype.submit;
            HTMLFormElement.prototype.submit = function() {
                capture();
                originalSubmit.call(this);
            };
        })();
    """.trimIndent()

    private fun cleanup(activity: AppCompatActivity, webView: WebView) = activity.runOnUiThread {
        val container = activity.findViewById<ViewGroup>(android.R.id.content)
        container.removeView(webView)

        webView.visibility = View.GONE
        webView.destroy()
    }

    var onSAMLSuccess: (response: String) -> Unit = {
        /** Code succeeded */
    }

    var onSAMLError: (message: String?) -> Unit = {
        /** Code failed */
    }
}