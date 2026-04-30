package com.tetrapi.or.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

//
//  StateMaterialButton
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class StateMaterialButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {

    fun isLoading() {
        isEnabled = false
        alpha = .5f
    }

    fun isReady() {
        isEnabled = true
        alpha = 1f
    }
}