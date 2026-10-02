package com.wba.or.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.net.toUri
import com.wba.or.databinding.ActivityAboutBinding
import com.wba.sdk.core.SDK

//
//  AboutActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 WBA. All rights reserved.
//

class AboutActivity : ORActivity() {

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
        binding.versionText.text = String.format(getString(com.wba.or.R.string.activity_about_version), if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0)).versionName
        else packageManager.getPackageInfo(packageName, 0).versionName)

        val config = SDK.getConfig()
        binding.termsCard.setOnClickListener {
            val url = config.termsOfService
            if (!url.isNullOrBlank()) openBrowser(url)
        }

        binding.privacyCard.setOnClickListener {
            val url = config.privacyPolicy
            if (!url.isNullOrBlank()) openBrowser(url)
        }
    }

    private fun openBrowser(url: String){
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}