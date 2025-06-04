package ca.doophie.swipelauncher

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import ca.doophie.swipelauncher.MainActivity
import ca.doophie.swipelauncher.data.ApplicationFetcher
import ca.doophie.swipelauncher.data.NotificationWatcher
import ca.doophie.swipelauncher.ui.theme.GrassGreen
import ca.doophie.swipelauncher.ui.theme.SwipeLauncherTheme
import ca.doophie.swipelauncher.views.ScenicLayout

class SettingsActivity : ComponentActivity() {

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fetcher = ApplicationFetcher()

        fetcher.getAllApplications(packageManager)

        setContent {
            SwipeLauncherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GrassGreen
                ) {
                    Column {

                    }
                }
            }
        }
    }
}