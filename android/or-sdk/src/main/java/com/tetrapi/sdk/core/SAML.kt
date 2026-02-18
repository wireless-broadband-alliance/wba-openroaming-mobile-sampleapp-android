package com.tetrapi.sdk.core

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.JavascriptInterface
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.tetrapi.sdk.utils.Preferences
import org.json.JSONObject
import androidx.core.net.toUri

//
//  SAML.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class SAML(private val activity: AppCompatActivity) {

    fun start() {
        val url = Preferences(activity).getString("SAML_START") ?: return onSAMLError("TODO handle empty start!")

        val container = activity.findViewById<ViewGroup>(android.R.id.content)

        val webView = WebView(activity).apply {

            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            @SuppressLint("SetJavaScriptEnabled")
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            addJavascriptInterface(object {

                @JavascriptInterface
                fun onSamlResponse(response: String) {

                    activity.runOnUiThread {
                        val test = response
                        print(test)

                        // TODO: call POST /api/v3/auth/saml with value
                        container.removeView(this@apply)
                        destroy()
                    }
                }
            }, "SAMLBridge")

            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView, pageUrl: String) {
                    // Inject JS to capture SAMLResponse if present
                    view.evaluateJavascript(CAPTURE_SCRIPT, null)
                }
            }
        }

        webView.loadUrl(url)

        container.addView(webView)
    }

    private val CAPTURE_SCRIPT = """
            (function() {
              try {
                function send() {
                  var input = document.querySelector('input[name="SAMLResponse"]');
                  if (input && input.value && input.value.length > 50) {
                    window.SAMLBridge.onSamlResponse(input.value);
                    return true;
                  }
                  return false;
                }

                // Try immediately
                if (send()) return;

                // Intercept submit (SAML POST pages auto-submit)
                document.addEventListener('submit', function(e) {
                  if (send()) { e.preventDefault(); e.stopPropagation(); }
                }, true);

                // Retry a couple times in case it appears later
                setTimeout(send, 200);
                setTimeout(send, 800);
              } catch (e) {}
            })();
        """.trimIndent()

    var onSAMLSuccess: (response: JSONObject) -> Unit = {
        /** Code succeeded */
    }

    var onSAMLError: (message: String?) -> Unit = {
        /** Code failed */
    }
}