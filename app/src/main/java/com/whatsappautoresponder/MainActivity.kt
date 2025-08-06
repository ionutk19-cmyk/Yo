package com.whatsappautoresponder

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.whatsappautoresponder.databinding.ActivityMainBinding
import com.whatsappautoresponder.ui.AutoReplyRulesFragment
import com.whatsappautoresponder.ui.ScheduledMessagesFragment
import com.whatsappautoresponder.utils.AccessibilityUtils
import com.whatsappautoresponder.utils.SharedPreferencesManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefsManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefsManager = SharedPreferencesManager(this)
        
        setupToolbar()
        setupViewPager()
        setupFab()
        updateServiceStatus()
        setupAutoReplySwitch()
    }

    override fun onResume() {
        super.onResume()
        updateServiceStatus()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.auto_reply_rules)
                1 -> getString(R.string.scheduled_messages)
                else -> ""
            }
        }.attach()
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            when (binding.viewPager.currentItem) {
                0 -> {
                    // Add auto reply rule
                    val intent = Intent(this, AddAutoReplyRuleActivity::class.java)
                    startActivity(intent)
                }
                1 -> {
                    // Add scheduled message
                    val intent = Intent(this, AddScheduledMessageActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun updateServiceStatus() {
        val isAccessibilityEnabled = AccessibilityUtils.isAccessibilityServiceEnabled(this)
        
        if (isAccessibilityEnabled) {
            binding.tvServiceStatus.text = getString(R.string.auto_reply_enabled)
            binding.tvServiceStatus.setTextColor(getColor(R.color.whatsapp_green))
            binding.switchAutoReply.isEnabled = true
        } else {
            binding.tvServiceStatus.text = getString(R.string.accessibility_not_enabled)
            binding.tvServiceStatus.setTextColor(getColor(R.color.error))
            binding.switchAutoReply.isEnabled = false
            binding.switchAutoReply.isChecked = false
        }
    }

    private fun setupAutoReplySwitch() {
        binding.switchAutoReply.isChecked = prefsManager.isAutoReplyEnabled()
        
        binding.switchAutoReply.setOnCheckedChangeListener { _, isChecked ->
            prefsManager.setAutoReplyEnabled(isChecked)
        }
        
        binding.tvServiceStatus.setOnClickListener {
            if (!AccessibilityUtils.isAccessibilityServiceEnabled(this)) {
                openAccessibilitySettings()
            }
        }
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AutoReplyRulesFragment()
                1 -> ScheduledMessagesFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}