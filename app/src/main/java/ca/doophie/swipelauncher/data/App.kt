package ca.doophie.swipelauncher.data

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
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

fun App.openSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

    val uri: Uri = Uri.fromParts("package", packageName, null)
    intent.data = uri

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

