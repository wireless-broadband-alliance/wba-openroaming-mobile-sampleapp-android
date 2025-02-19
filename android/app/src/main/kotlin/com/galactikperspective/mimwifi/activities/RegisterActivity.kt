package com.galactikperspective.mimwifi.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.galactikperspective.mimwifi.databinding.ActivityRegisterBinding
import com.galactikperspective.or.R
import com.galactikperspective.or.core.User

//
//  RegisterActivity.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2024 Galactik Perspective. All rights reserved.
//

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val user = User()
        user.onRegisterSuccess = {
            val message = it.getJSONObject("data").getString("message")
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }

            finish()
        }

        user.onRegisterError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        val email = binding.emailEdit.text.toString()
        val password = binding.passwordEdit.text.toString()
        user.register(this, email, password)
    }
}