package ca.doophie.swipelauncher.data

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "widgetLayouts")
data class WidgetLayoutData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val widgets: List<WidgetBuilder>,
    val listItemBackground: Int,
    val listItemAltBackground: Int,
    val backgroundImageId: Int,
    val backgroundColors: List<Color>
)