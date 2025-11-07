package com.tetrapi.or.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.wifi.hotspot2.PasspointConfiguration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tetrapi.or.R
import com.tetrapi.or.databinding.DialogDeleteBinding

//
//  DeleteDialog.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Tetrapi. All rights reserved.
//

class DeleteDialog(private val network: PasspointConfiguration) : DialogFragment() {

    private val binding by lazy { DialogDeleteBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Remove default background
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileText.text = String.format(getString(R.string.dialog_delete_profile), network.homeSp.friendlyName)

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.deleteButton.setOnClickListener {
            onDelete()
            dismiss()
        }
    }

    var onDelete: () -> Unit = {
        /** Delete Network */
    }
}