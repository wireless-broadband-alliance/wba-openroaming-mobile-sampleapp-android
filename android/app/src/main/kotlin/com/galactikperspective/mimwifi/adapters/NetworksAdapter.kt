package com.galactikperspective.mimwifi.adapters

import android.net.wifi.hotspot2.PasspointConfiguration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.galactikperspective.mimwifi.databinding.ItemNetworkBinding
import com.galactikperspective.mimwifi.fragments.UsageFragment

//
//  NetworksAdapter.java
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Galactik Perspective. All rights reserved.
//

class NetworksAdapter(private val fragment: UsageFragment, private val networks: ArrayList<PasspointConfiguration>) : RecyclerView.Adapter<NetworksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val binding = ItemNetworkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val network = networks[position]

        holder.binding.nameText.text = network.homeSp.friendlyName
        holder.binding.idText.text = network.uniqueId

        holder.binding.deleteButton.setOnClickListener {
            fragment.removeNetwork(network)
        }
    }

    override fun getItemCount() = networks.size

    fun update(networks: List<PasspointConfiguration>) {
        this.networks.clear()
        this.networks.addAll(networks)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemNetworkBinding) : RecyclerView.ViewHolder(binding.root)
}