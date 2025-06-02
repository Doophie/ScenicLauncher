package ca.doophie.swipelauncher.data

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ApplicationFetcher: ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _applicationsList = MutableStateFlow(emptyList<App>())
    val filteredApplicationsList = searchText
        .combine(_applicationsList) { text, apps ->
            if (text.isBlank()) {
               return@combine apps
            }

            apps.filter { app ->// filter and return a list of countries based on the text the user typed
                app.name.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _applicationsList.value
        )

    var allApplicationsList: List<App> = emptyList()

    fun getAllApplications(packageManager: PackageManager) {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val pkgAppsList: List<ResolveInfo> =
            packageManager.queryIntentActivities(mainIntent, 0)

        val installedApps = pkgAppsList.map { resolveInfo ->
            App(
                name = resolveInfo.loadLabel(packageManager).toString(),
                packageName = resolveInfo.activityInfo.packageName,
                icon = resolveInfo.loadIcon(packageManager)
            )
        }

        _applicationsList.value = installedApps
        allApplicationsList = installedApps
    }

    fun clearText() {
        _searchText.value = ""
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun getApplication(name: String): App? {
        return allApplicationsList.firstOrNull { it.name.lowercase().contains(name) }
    }

    fun watchNotifications(app: App, callback: (Boolean)->Unit) {
        var hasNotifications = !app.hasNotifications()

        viewModelScope.launch {
            while (true) {
                val new = app.hasNotifications()
                if (new != hasNotifications) callback(new)
                hasNotifications = new
                delay(1500)
            }
        }
    }


}