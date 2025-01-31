package com.galactikperspective.mimwifi.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.galactikperspective.mimwifi.databinding.ActivityRegisterBinding
import com.galactikperspective.or.core.User
import com.galactikperspective.or.utils.Preferences

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

    private fun setViews() {
        /*
        val user = User()
        user.onLoginSuccess = {
            val s = it
            println(s)
        }

        user.onLoginError = {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        }

        user.login(this, email, password)
        */
    }

    private fun register() {
        val email = binding.emailEdit.text.toString()
        val password = binding.passwordEdit.text.toString()
        val nameFirst = binding.nameFirstEdit.text.toString()
        val nameLast = binding.nameLastEdit.text.toString()

        val user = User()
        user.onRegisterSuccess = {
            //TODO continue here
            val s = it
            println(s)
        }

        user.onRegisterError = {

            runOnUiThread {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        user.register(this, email, password, nameFirst, nameLast)
    }
}