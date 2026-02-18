package com.tetrapi.or.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tetrapi.or.R
import com.tetrapi.or.databinding.ActivityUserBinding
import com.tetrapi.sdk.core.User
import org.json.JSONObject

//
//  UserActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class UserActivity : AppCompatActivity() {

    private val binding by lazy { ActivityUserBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setViews()
    }

    private fun setViews() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val user = User()
        user.onInfoSuccess = {

            runOnUiThread {
                setInfo(it)
            }
        }

        user.onInfoError = {

            runOnUiThread {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        user.info(this)
    }

    private fun setInfo(data: JSONObject) {
        binding.detailsLayout.visibility = View.VISIBLE

        binding.uuidText.text = data.getString("uuid")
        binding.typeText.text = data.getJSONArray("user_external_auths").getJSONObject(0).getString("provider")

        binding.createdText.text = String.format(getString(R.string.activity_user_created), data.getString("created_at"))
    }
}