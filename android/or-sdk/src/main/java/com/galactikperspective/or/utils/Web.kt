package com.galactikperspective.or.utils

import androidx.appcompat.app.AppCompatActivity
import com.galactikperspective.or.R
import com.galactikperspective.or.core.User
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

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

    fun login(activity: AppCompatActivity, user: User, params: JSONObject, type: String) {
        val url = String.format(activity.getString(R.string.open_roaming_api), "auth/$type")
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .post(params.toBody())
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) = activity.runOnUiThread {
                if (response.isSuccessful) user.onLoginSuccess(response.body.string())
                else onFailure(call, IOException("Invalid response exception!"))
            }

            override fun onFailure(call: Call, e: IOException) = activity.runOnUiThread {
                user.onLoginError(e)
            }
        })
    }

    private fun JSONObject.toBody(): RequestBody {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        return toString().toRequestBody(mediaType)
    }
}