package com.galactikperspective.or.core

import android.app.Activity
import android.widget.Toast
import com.galactikperspective.or.utils.Turnstile

//
//  User.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2024 Galactik Perspective. All rights reserved.
//

class User {

    fun register(activity: Activity) {
        val turnstile = Turnstile()
        turnstile.onCaptchaSuccess = {
            Toast.makeText(activity, "Success! Proceed with the register user callback", Toast.LENGTH_LONG).show()
        }

        turnstile.onCaptchaError = { it ->
            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
        }

        turnstile.renderCaptcha(activity)
    }
}