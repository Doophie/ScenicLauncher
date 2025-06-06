package ca.doophie.swipelauncher.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ca.doophie.swipelauncher.data.WidgetBuilder

@Dao
interface WidgetLayoutDataDao {
    @Insert
    suspend fun insert(builder: WidgetLayoutData)
    @Update
    suspend fun update(builder: WidgetLayoutData)
    @Delete
    suspend fun delete(builder: WidgetLayoutData)
    @Query("SELECT * FROM widgetLayouts")
    fun getAllWidgetLayoutDatas(): LiveData<List<WidgetLayoutData>>
}