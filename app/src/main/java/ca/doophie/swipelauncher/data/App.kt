package ca.doophie.swipelauncher.data

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import ca.doophie.swipelauncher.utils.UniversalTextClearer

data class App(
    val name: String,
    val packageName: String,
    val icon: Drawable?
)

fun App.launch(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(packageName)

    if (intent == null) {
        Log.e("App.launch", "Failed to get launch intent for package: $packageName")
        return
    }

    clearNotifications()
    UniversalTextClearer.callback.invoke()

    context.startActivity(intent)
}

fun App.hasNotifications(): Boolean {
    return NotificationWatcher.trackedNotifications.contains(packageName)
}

fun App.clearNotifications() {
    if (NotificationWatcher.trackedNotifications.contains(packageName))
        NotificationWatcher.trackedNotifications.remove(packageName)
}

