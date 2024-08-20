package com.galactikperspective.mimwifi.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.galactikperspective.mimwifi.R
import com.galactikperspective.mimwifi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setViews()
    }

    private fun setViews() {
        binding.bottomNavigation.setOnItemSelectedListener {
            it.changeMode()
        }
    }

    private fun MenuItem.changeMode() = when (itemId) {
        R.id.user_button -> changeMode(0)
        R.id.usage_button -> changeMode(1)
        else -> changeMode(2)
    }

    private fun changeMode(position: Int): Boolean {
        val currentPosition = binding.viewAnimator.displayedChild
        if (position == currentPosition) return false

        binding.viewAnimator.inAnimation = AnimationUtils.loadAnimation(this, if (position > currentPosition) R.anim.pull_right else R.anim.push_left)
        binding.viewAnimator.outAnimation = AnimationUtils.loadAnimation(this, if (position > currentPosition) R.anim.push_right else R.anim.pull_left)
        binding.viewAnimator.displayedChild = position

        return true
    }
}