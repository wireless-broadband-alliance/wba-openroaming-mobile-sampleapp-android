package com.tetrapi.or.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.tetrapi.or.databinding.ActivityAboutBinding
import com.tetrapi.sdk.core.Config
import kotlin.text.isNotBlank

//
//  AboutActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class AboutActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAboutBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setViews()
    }

    private fun setViews() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        @Suppress("DEPRECATION")
        binding.versionText.text = String.format(getString(com.tetrapi.or.R.string.activity_about_version), if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0)).versionName
        else packageManager.getPackageInfo(packageName, 0).versionName)

        binding.termsCard.setOnClickListener {
            val url = Config.tos(this)
            if (url != null && url.isNotBlank()) openBrowser(url)
        }

        binding.privacyCard.setOnClickListener {
            val url = Config.privacy(this)
            if (url != null && url.isNotBlank()) openBrowser(url)
        }
    }

    private fun openBrowser(url: String){
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}