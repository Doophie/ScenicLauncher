package ca.doophie.swipelauncher.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WidgetLayoutData::class, WidgetBuilder::class], version = 1, exportSchema = false)
abstract class WidgetDatabase: RoomDatabase() {
    abstract fun widgetLayoutDataDao(): WidgetLayoutDataDao
    abstract fun widgetBuilderDao(): WidgetBuilderDao

    companion object {
        @Volatile
        private var INSTANCE: WidgetDatabase? = null
        fun getDatabase(context: Context): WidgetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WidgetDatabase::class.java,
                    "widget_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}