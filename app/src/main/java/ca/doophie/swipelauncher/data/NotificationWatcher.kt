package ca.doophie.swipelauncher.data

import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationWatcher : NotificationListenerService() {

    private var spotifyBroadcastReceiver: SpotifyBroadcastReceiver? = null


    override fun onBind(intent: Intent?): IBinder? {
        spotifyBroadcastReceiver = SpotifyBroadcastReceiver()
        registerReceiver(spotifyBroadcastReceiver, IntentFilter(), RECEIVER_EXPORTED)

        return super.onBind(intent)
    }

    override fun onDestroy() {
        unregisterReceiver(spotifyBroadcastReceiver)

        super.onDestroy()
    }


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
