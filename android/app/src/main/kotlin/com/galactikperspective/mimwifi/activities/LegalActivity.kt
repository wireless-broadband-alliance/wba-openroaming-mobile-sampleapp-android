package com.galactikperspective.mimwifi.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.galactikperspective.mimwifi.R
import com.galactikperspective.mimwifi.databinding.ActivityLegalBinding
import com.galactikperspective.mimwifi.fragments.PrivacyFragment
import com.galactikperspective.mimwifi.fragments.TermsFragment
import com.galactikperspective.or.utils.Preferences
import com.google.android.material.tabs.TabLayoutMediator

//
//  LegalActivity.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2024 Galactik Perspective. All rights reserved.
//

class LegalActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLegalBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setViews()
    }

    private fun setViews() {
        val adapter = Adapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(if (position == 0) R.string.activity_legal_terms else R.string.activity_legal_privacy)
        }.attach()

        binding.continueButton.setOnClickListener {
            Preferences(this).saveBoolean("isLoggedIn", true)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    class Adapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

        override fun createFragment(position: Int) = if (position == 0) TermsFragment()
        else PrivacyFragment()

        override fun getItemCount() = 2
    }
}