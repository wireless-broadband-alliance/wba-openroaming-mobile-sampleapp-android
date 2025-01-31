package com.galactikperspective.or.utils

import android.content.Context
import com.galactikperspective.or.R
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.json.JSONObject

//
//  Web.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Galactik Perspective. All rights reserved.
//

class Web(private val context: Context) {

    private val client = HttpClient()

    suspend fun getConfig(): JSONObject {
        val url = String.format(context.getString(R.string.open_roaming_api), "config")
        val response = client.get(url).bodyAsText()
        return JSONObject(response)
    }

    suspend fun login(params: JSONObject, type: String): JSONObject {
        val url = String.format(context.getString(R.string.open_roaming_api), "auth/$type")
        val response = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(params.toString().trimIndent())
        }.bodyAsText()

        return JSONObject(response)
    }

    suspend fun register(params: JSONObject, type: String): JSONObject {
        val url = String.format(context.getString(R.string.open_roaming_api), "auth/$type/register")
        val response = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(params.toString().trimIndent())
        }.bodyAsText()

        return JSONObject(response)
    }
}