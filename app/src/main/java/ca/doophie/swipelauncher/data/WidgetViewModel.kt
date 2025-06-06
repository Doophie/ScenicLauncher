package ca.doophie.swipelauncher.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel


class WidgetViewModel(
    private val widgetLayoutDataDao: WidgetLayoutDataDao,
    private val widgetBuilderDao: WidgetBuilderDao
) : ViewModel() {
    val widgetBuilders: LiveData<List<WidgetBuilder>> = widgetBuilderDao.getAllWidgets()
    val widgetLayouts: LiveData<List<WidgetLayoutData>> = widgetLayoutDataDao.getAllWidgetLayoutDatas()

    suspend fun insertLayout(layoutData: WidgetLayoutData) {
        widgetLayoutDataDao.insert(layoutData)
    }
}