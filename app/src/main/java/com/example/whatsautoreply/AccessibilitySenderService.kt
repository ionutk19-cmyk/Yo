package com.example.whatsautoreply

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class AccessibilitySenderService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // This skeleton can be expanded to automatically press the send button in WhatsApp UI
    }

    override fun onInterrupt() {
        // Required override
    }
}