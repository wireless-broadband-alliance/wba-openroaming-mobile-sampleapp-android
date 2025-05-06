package com.galactikperspective.mimwifi.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.galactikperspective.mimwifi.databinding.FragmentUserBinding
import com.galactikperspective.or.core.User
import org.json.JSONObject
import com.galactikperspective.mimwifi.R
import com.galactikperspective.mimwifi.activities.LoginActivity

//
//  UserFragment.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Galactik Perspective. All rights reserved.
//

class UserFragment : Fragment() {

    private val binding by lazy { FragmentUserBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {
        val user = User()
        user.onInfoSuccess = {

            requireActivity().runOnUiThread {
                setInfo(it)
            }
        }

        user.onInfoError = {

            requireActivity().runOnUiThread {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }

        user.info(requireActivity())
    }

    private fun setInfo(data: JSONObject) {
        binding.viewAnimator.showNext()

        binding.typeText.text = data.getJSONArray("user_external_auths").getJSONObject(0).getString("provider")
        binding.uuidText.text = data.getString("uuid")

        binding.createdText.text = String.format(getString(R.string.fragment_user_created), data.getString("created_at"))
        binding.logoutButton.setOnClickListener {
            User().logout(requireActivity())

            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
}