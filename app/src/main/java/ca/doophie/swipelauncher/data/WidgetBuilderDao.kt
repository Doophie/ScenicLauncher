package ca.doophie.swipelauncher.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ca.doophie.swipelauncher.widgets.WidgetBuilder

@Dao
interface WidgetBuilderDao {
    @Insert
    suspend fun insert(builder: WidgetBuilder)
    @Update
    suspend fun update(builder: WidgetBuilder)
    @Delete
    suspend fun delete(builder: WidgetBuilder)
    @Query("SELECT * FROM widgets")
    fun getAllApps(): LiveData<List<App>>
}