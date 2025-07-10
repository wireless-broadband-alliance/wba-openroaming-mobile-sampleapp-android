package com.tetrapi.sdk.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.tetrapi.sdk.R
import com.google.android.material.button.MaterialButton

class GoogleSignInButton : MaterialButton {

    constructor(context: Context) : super(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        text = context.getString(R.string.google_sign_in_button)
        setTextColor(Color.BLACK)

        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_google)
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        gravity = Gravity.CENTER

        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, context.resources.displayMetrics).toInt()
        strokeColor = ColorStateList.valueOf(Color.BLACK)

        val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics).toInt()
        setPadding(padding, padding, padding, padding)
        compoundDrawablePadding = padding

        val corners = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25f, context.resources.displayMetrics)
        shapeAppearanceModel = shapeAppearanceModel
            .toBuilder()
            .setTopLeftCornerSize(corners)
            .setTopRightCornerSize(corners)
            .setBottomRightCornerSize(corners)
            .setBottomLeftCornerSize(corners).build()

        setOnClickListener {
            startGoogleSignIn()
        }
    }

    private fun startGoogleSignIn() {
        /*
        val turnstile = Turnstile()
        turnstile.onCaptchaSuccess = {
            Toast.makeText(context, "Success! Proceed to google account signin!", Toast.LENGTH_LONG).show()
        }

        turnstile.onCaptchaError = { it ->
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }

        val activity = context as Activity
        turnstile.renderCaptcha(activity)
         */
    }
}