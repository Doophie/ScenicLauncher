package ca.doophie.swipelauncher

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import ca.doophie.swipelauncher.data.ApplicationFetcher
import ca.doophie.swipelauncher.data.NotificationWatcher
import ca.doophie.swipelauncher.data.WidgetViewModel
import ca.doophie.swipelauncher.ui.theme.GrassGreen
import ca.doophie.swipelauncher.ui.theme.SwipeLauncherTheme
import ca.doophie.swipelauncher.views.ScenicLayout


class MainActivity : ComponentActivity() {

    val widgetBuilderViewModel: WidgetViewModel by viewModels()

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * START FULLSCREEN CODE
         */
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.statusBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        /**
         * END FULLSCREEN CODE
         */

        /**
         * START NOTIFICATION ACCESS
         */

        if (!hasNotificationAccess()) openPermissions()

        startService(Intent(this, NotificationWatcher::class.java))



        /**
         * END NOTIFICATION ACCESS
         */

        val fetcher = ApplicationFetcher()

        fetcher.getAllApplications(packageManager)

        setContent {
            SwipeLauncherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GrassGreen
                ) {
                    HideSystemBars()

                    Column {

                        //ScenicView(context = this@MainActivity, fetcher = fetcher)
                        ScenicLayout(context = this@MainActivity, fetcher = fetcher)
                    }
                }
            }
        }
    }

    @Composable
    fun HideSystemBars() {
        DisposableEffect(Unit) {
            val window = this@MainActivity.window ?: return@DisposableEffect onDispose {}
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            insetsController.apply {
                hide(WindowInsetsCompat.Type.statusBars())
                hide(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            onDispose {
                insetsController.apply {
                    show(WindowInsetsCompat.Type.statusBars())
                    show(WindowInsetsCompat.Type.navigationBars())
                    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                }
            }
        }
    }

    private fun hasNotificationAccess(): Boolean {
        return Settings.Secure.getString(
            applicationContext.contentResolver,
            "enabled_notification_listeners"
        ).contains(applicationContext.packageName)
    }

    private fun openPermissions() {
        try {
            val settingsIntent =
                Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(settingsIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}

