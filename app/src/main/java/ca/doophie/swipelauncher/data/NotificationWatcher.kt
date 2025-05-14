package ca.doophie.swipelauncher.data

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationWatcher : NotificationListenerService() {

    companion object {
        val trackedNotifications = HashMap<String, List<String>>()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn != null) {
            val data = sbn.key

            if (trackedNotifications.contains(sbn.opPkg)) {
                val existingNotifications = trackedNotifications[sbn.opPkg]!!

                trackedNotifications[sbn.opPkg] = listOf(data) + existingNotifications
            } else {
                trackedNotifications[sbn.opPkg] = listOf(data)
            }
        }
    }
}
