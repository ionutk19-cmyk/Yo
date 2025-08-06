package com.whatsapp.auto.responder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.whatsapp.auto.responder.adapter.ScheduledMessageAdapter
import com.whatsapp.auto.responder.databinding.ActivityMainBinding
import com.whatsapp.auto.responder.dialog.AddScheduledMessageDialog
import com.whatsapp.auto.responder.service.WhatsAppAccessibilityService
import com.whatsapp.auto.responder.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var scheduledMessageAdapter: ScheduledMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        
        setupUI()
        setupObservers()
        checkPermissions()
    }

    private fun setupUI() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar)

        // Setup RecyclerView
        scheduledMessageAdapter = ScheduledMessageAdapter(
            onEditClick = { scheduledMessage ->
                showAddScheduledMessageDialog(scheduledMessage)
            },
            onDeleteClick = { scheduledMessage ->
                viewModel.deleteScheduledMessage(scheduledMessage)
            }
        )

        binding.rvScheduledMessages.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = scheduledMessageAdapter
        }

        // Setup click listeners
        binding.btnEnableAccessibility.setOnClickListener {
            openAccessibilitySettings()
        }

        binding.switchAutoReply.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setAutoReplyEnabled(isChecked)
        }

        binding.btnSaveAutoReply.setOnClickListener {
            saveAutoReplySettings()
        }

        binding.fabAddSchedule.setOnClickListener {
            showAddScheduledMessageDialog()
        }

        // Load saved settings
        loadSavedSettings()
    }

    private fun setupObservers() {
        viewModel.accessibilityServiceEnabled.observe(this) { isEnabled ->
            updateAccessibilityStatus(isEnabled)
        }

        viewModel.autoReplyEnabled.observe(this) { isEnabled ->
            binding.switchAutoReply.isChecked = isEnabled
            updateAutoReplyStatus(isEnabled)
        }

        viewModel.scheduledMessages.observe(this) { messages ->
            scheduledMessageAdapter.submitList(messages)
        }

        viewModel.message.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissions() {
        if (!isAccessibilityServiceEnabled()) {
            showPermissionDialog()
        }
    }

    private fun showPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.accessibility_permission_required))
            .setPositiveButton("Enable") { _, _ ->
                openAccessibilitySettings()
            }
            .setNegativeButton("Later", null)
            .show()
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    private fun updateAccessibilityStatus(isEnabled: Boolean) {
        binding.accessibilityStatus.text = if (isEnabled) {
            getString(R.string.accessibility_enabled)
        } else {
            getString(R.string.accessibility_disabled)
        }
        binding.accessibilityStatus.setTextColor(
            if (isEnabled) getColor(R.color.success_green) else getColor(R.color.error_red)
        )
    }

    private fun updateAutoReplyStatus(isEnabled: Boolean) {
        binding.autoReplyStatus.text = if (isEnabled) {
            getString(R.string.auto_reply_enabled)
        } else {
            getString(R.string.auto_reply_disabled)
        }
        binding.autoReplyStatus.setTextColor(
            if (isEnabled) getColor(R.color.success_green) else getColor(R.color.error_red)
        )
    }

    private fun loadSavedSettings() {
        val autoReplyMessage = viewModel.getAutoReplyMessage()
        binding.etAutoReplyMessage.setText(autoReplyMessage)

        val replyType = viewModel.getReplyType()
        when (replyType) {
            "all" -> binding.rgReplyType.check(binding.rbReplyAll.id)
            "contacts" -> binding.rgReplyType.check(binding.rbReplyContacts.id)
            "specific" -> binding.rgReplyType.check(binding.rbReplySpecific.id)
        }
    }

    private fun saveAutoReplySettings() {
        val message = binding.etAutoReplyMessage.text.toString().trim()
        if (message.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_message), Toast.LENGTH_SHORT).show()
            return
        }

        val replyType = when (binding.rgReplyType.checkedRadioButtonId) {
            binding.rbReplyAll.id -> "all"
            binding.rbReplyContacts.id -> "contacts"
            binding.rbReplySpecific.id -> "specific"
            else -> "all"
        }

        viewModel.saveAutoReplySettings(message, replyType)
        Toast.makeText(this, getString(R.string.success_settings_saved), Toast.LENGTH_SHORT).show()
    }

    private fun showAddScheduledMessageDialog(scheduledMessage: ScheduledMessage? = null) {
        AddScheduledMessageDialog(this, scheduledMessage) { message, phoneNumber, time, repeatType ->
            viewModel.addScheduledMessage(message, phoneNumber, time, repeatType)
        }.show()
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityEnabled = Settings.Secure.getInt(
            contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED, 0
        )

        if (accessibilityEnabled == 1) {
            val service = Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            return service?.contains(packageName + "/" + WhatsAppAccessibilityService::class.java.name) == true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkAccessibilityServiceStatus()
    }
}