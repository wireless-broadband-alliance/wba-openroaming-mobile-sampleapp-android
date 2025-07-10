package com.tetrapi.or.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tetrapi.or.databinding.FragmentAboutBinding
import com.tetrapi.or.R
import androidx.core.net.toUri
import com.tetrapi.sdk.core.Config

//
//  AboutFragment.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Tetrapi. All rights reserved.
//

class AboutFragment : Fragment() {

    private val binding by lazy { FragmentAboutBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {
        @Suppress("DEPRECATION")
        binding.versionText.text = String.format(getString(R.string.fragment_about_version), if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) requireActivity().packageManager.getPackageInfo(requireActivity().packageName, PackageManager.PackageInfoFlags.of(0)).versionName
        else requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0).versionName)

        binding.tosText.setOnClickListener {
            val url = Config.tos(requireActivity())
            if (url != null && url.isNotBlank()) openBrowser(url)
        }

        binding.privacyText.setOnClickListener {
            val url = Config.privacy(requireActivity())
            if (url != null && url.isNotBlank()) openBrowser(url)
        }
    }

    private fun openBrowser(url: String){
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}