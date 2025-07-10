package com.tetrapi.or.fragments

import android.net.wifi.hotspot2.PasspointConfiguration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.tetrapi.or.adapters.NetworksAdapter
import com.tetrapi.or.databinding.FragmentUsageBinding
import com.tetrapi.sdk.core.OpenRoaming

//
//  UsageFragment.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Tetrapi. All rights reserved.
//

class UsageFragment : Fragment() {

    private val binding by lazy { FragmentUsageBinding.inflate(layoutInflater) }
    private val or = OpenRoaming()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        checkNetworks()
    }

    private fun checkNetworks() {
        val networks = or.networks(requireActivity())
        if (networks.isEmpty()) showConnection() else setNetworks(networks)

        binding.connectButton.setOnClickListener {
            or.connect(requireActivity())
        }

        or.onConnectionSuccess = {
            checkNetworks()
        }
    }

    private fun showConnection() {
        binding.recyclerView.visibility = View.GONE
        binding.connectionLayout.visibility = View.VISIBLE
    }

    private fun setNetworks(networks: ArrayList<PasspointConfiguration>) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.connectionLayout.visibility = View.GONE

        binding.recyclerView.adapter.let {
            if (it == null) binding.recyclerView.adapter = NetworksAdapter(this, networks)
            else (it as NetworksAdapter).update(networks)
        }
    }

    fun removeNetwork(network: PasspointConfiguration) {
        or.remove(requireActivity(), network)
        checkNetworks()
    }
}