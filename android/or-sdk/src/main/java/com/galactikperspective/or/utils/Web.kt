package com.galactikperspective.or.utils

import android.app.Activity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.UUID

//
//  Web.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2024 Galactik Perspective. All rights reserved.
//

class Web {

    //region GLOBAL
    private val client = OkHttpClient()
    //endregion

    fun verifyToken(jsInterface: Turnstile.JSInterface, activity: Activity, token: String) {
        //TODO replace with production secret
        val body = FormBody.Builder()
            .add("secret", "1x0000000000000000000000000000000AA")
            .add("response", token)
            .add("idempotency_key", UUID.randomUUID().toString())
            .build()

        val url = "https://challenges.cloudflare.com/turnstile/v0/siteverify"
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) = activity.runOnUiThread {
                if (response.isSuccessful) jsInterface.onVerifySuccess(response.body.string())
                else onFailure(call, IOException("Invalid response exception!"))
            }

            override fun onFailure(call: Call, e: IOException) = activity.runOnUiThread {
                jsInterface.onVerifyError(e)
            }
        })
    }
}