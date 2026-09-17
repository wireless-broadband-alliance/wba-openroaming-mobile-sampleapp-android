package com.tetrapi.or.activities

import android.content.Intent
import android.net.wifi.hotspot2.PasspointConfiguration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.tetrapi.or.databinding.ActivityMainBinding
import com.tetrapi.or.R
import com.tetrapi.or.adapters.NetworksAdapter
import com.tetrapi.or.dialogs.DeleteDialog
import com.tetrapi.or.dialogs.LoadingDialog
import com.tetrapi.sdk.core.OpenRoaming
import com.tetrapi.sdk.core.SDK
import com.tetrapi.sdk.utils.allowInfiniteLines

class MainActivity : ORActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val or = OpenRoaming()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.user_button -> user()
                R.id.about_button -> about()
                R.id.logout_button -> logout()
                else -> false
            }
        }

        binding.connectButton.setOnClickListener {
            connect()
        }

        checkNetworks()
    }

    private fun user(): Boolean {
        val intent = Intent(this, UserActivity::class.java)
        userLauncher.launch(intent)

        return true
    }

    private val userLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) result.data?.getBooleanExtra("expired", false)
            ?.takeIf { it }
            ?.let { logout() }
    }

    private fun about(): Boolean {
        startActivity(Intent(this, AboutActivity::class.java))
        return true
    }

    private fun logout(): Boolean {
        SDK.getUser().logout()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()

        return true
    }

    private fun connect() {
        binding.connectButton.isLoading()

        val dialog = LoadingDialog()
        dialog.show(supportFragmentManager, "LOADING_DIALOG")

        or.onConnectionSuccess = {
            checkNetworks()

            binding.connectButton.isReady()
            dialog.dismiss()
        }

        or.onConnectionError = {
            binding.connectButton.isReady()
            dialog.dismiss()

            val message = it ?: getString(com.tetrapi.sdk.R.string.common_error)
            if (message == "JWT Token is expired!") sessionExpired(message)
            else Snackbar.make(binding.connectButton, message, 4000).allowInfiniteLines().show()
        }

        or.connect(this)
    }

    private fun checkNetworks() {
        val networks = or.networks(this)
        if (networks.isNotEmpty()) setNetworks(networks) else binding.viewAnimator.displayedChild = 0
    }

    private fun setNetworks(networks: ArrayList<PasspointConfiguration>) {
        binding.viewAnimator.displayedChild = 1
        binding.recyclerView.adapter.let {
            if (it == null) binding.recyclerView.adapter = NetworksAdapter(this, networks)
            else (it as NetworksAdapter).update(networks)
        }
    }

    fun removeNetwork(network: PasspointConfiguration) {
        val dialog = DeleteDialog(network)
        dialog.show(supportFragmentManager, "DELETE_DIALOG")
        dialog.onDelete = {
            or.remove(this, network)
            checkNetworks()
        }
    }

    private fun sessionExpired(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        logout()
    }
}