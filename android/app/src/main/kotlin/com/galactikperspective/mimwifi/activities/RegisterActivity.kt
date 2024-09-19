package com.galactikperspective.mimwifi.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.galactikperspective.mimwifi.databinding.ActivityRegisterBinding
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
        setViews()
    }

    private fun setViews() {
        binding.registerButton.setOnClickListener {
            User().register(this)
        }
    }
}