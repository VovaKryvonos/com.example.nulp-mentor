package com.example.notification

import com.example.notification.data.Notification

interface OneSignalService {

    suspend fun sendNotification(notification: Notification): Boolean

    companion object {
        const val ONESIGNAL_APP_ID = "be9b5a8f-e593-4cac-8122-f18a5f87381a"

        const val NOTIFICATIONS = "https://onesignal.com/api/v1/notifications"
    }
}