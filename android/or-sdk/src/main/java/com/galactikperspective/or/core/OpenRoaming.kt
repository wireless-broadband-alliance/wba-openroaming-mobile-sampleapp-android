package com.galactikperspective.or.core

import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.widget.Toast
import com.galactikperspective.or.R
import com.galactikperspective.or.dialogs.TermsDialog

//
//  OpenRoaming.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2024 Galactik Perspective. All rights reserved.
//

class OpenRoaming {

    private fun isTermsAccepted(context: Context): Boolean {
        val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return preferences.getBoolean("terms_acceptance", false)
    }

    private fun showTermsDialog(context: Context) {
        val dialog = TermsDialog(context)

    }

    private fun isPasspointSupported(context: Context): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return isPasspointSupported(context, wifiManager)
    }

    private fun isPasspointSupported(context: Context, wifiManager: WifiManager) = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> wifiManager.isWifiPasspointEnabled
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> context.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_PASSPOINT)
        else -> false
    }

    fun connect(context: Context) {
        // Checks if device supports OpenRoaming
        val isSupported = isPasspointSupported(context)
        if (!isSupported) return Toast.makeText(context, context.getString(R.string.open_roaming_not_supported), Toast.LENGTH_LONG).show()

        // Checks if Terms and Conditions are accepted
        val isTermsAccepted = isTermsAccepted(context)
        if (!isTermsAccepted) return showTermsDialog(context)

        /*
        // Initialize the WifiManager
        val isSupported = isPasspointSupported(context)
        if (!isSupported) return Toast.makeText(context, context.getString(R.string.open_roaming_not_supported), Toast.LENGTH_LONG).show()

        // Set up HomeSp (Home Service Provider) information
        val homeSp = HomeSp()
        homeSp.friendlyName = "TETRAPI S.A."
        homeSp.fqdn = "tetrapi-idp.openroaming.tetrapi.pt"

        val oi1 = "5a03ba0000".toLong(16)
        val oi2 = "004096".toLong(16)
        homeSp.roamingConsortiumOis = longArrayOf(oi1, oi2)

        // Set up User Credentials
        val userCredential = Credential.UserCredential()
        userCredential.username = "dQMCa8mh4H0Se252FVCMV@tetrapi.pt"
        userCredential.password = "UnR1Y0MzWDlVeGxhNHYzU1drY0pl"
        userCredential.eapType = 21
        userCredential.nonEapInnerMethod = "MS-CHAP-V2"

        // Set up Credential for authentication
        val credential = Credential()
        credential.realm = "tetrapi.pt"
        credential.userCredential = userCredential

        // Create a new PasspointConfiguration instance
        val passpointConfig = PasspointConfiguration()
        passpointConfig.homeSp = homeSp
        passpointConfig.credential = credential

        // Add or update the Passpoint configuration
        val suggestions = ArrayList<WifiNetworkSuggestion>()
        suggestions.add(WifiNetworkSuggestion.Builder().setPasspointConfig(passpointConfig).build())

        // Create intent
        val bundle = Bundle()
        bundle.putParcelableArrayList(Settings.EXTRA_WIFI_NETWORK_LIST, suggestions)

        val intent = Intent(Settings.ACTION_WIFI_ADD_NETWORKS)
        intent.putExtras(bundle)

        // Launch intent
        context.startActivity(intent)
        */
    }
}