package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tetrapi.or.databinding.ActivityMainBinding
import com.tetrapi.or.R
import com.tetrapi.sdk.core.User

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

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
    }

    private fun user(): Boolean {
        startActivity(Intent(this, UserActivity::class.java))
        return true
    }

    private fun about(): Boolean {
        startActivity(Intent(this, AboutActivity::class.java))
        return true
    }

    private fun logout(): Boolean {
        User().logout(this)

        startActivity(Intent(this, LoginActivity::class.java))
        finish()

        return true
    }
}