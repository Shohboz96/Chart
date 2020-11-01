package uz.max.anychart.room.dao

import androidx.room.Dao
import androidx.room.Query
import uz.max.anychart.room.entities.ValuesAllData
import uz.max.anychart.room.dao.BaseDao

@Dao
interface ValuesAllDao : BaseDao<ValuesAllData> {
    @Query("SELECT * FROM ValuesAllData where belong = 0")
    fun getAllMiddle(): List<ValuesAllData>

    @Query("DELETE FROM ValuesAllData where belong = 0")
    fun clearMiddleDB()

    @Query("SELECT * FROM ValuesAllData where belong = 1")
    fun getAllBalance(): List<ValuesAllData>

    @Query("DELETE FROM ValuesAllData where belong = 1")
    fun clearBalanceDB()

    @Query("SELECT * FROM ValuesAllData where belong = 2")
    fun getAllTasks(): List<ValuesAllData>

    @Query("DELETE FROM ValuesAllData where belong = 2")
    fun clearTasksDB()

    @Query("SELECT * FROM ValuesAllData where belong = 3")
    fun getAllWorkers(): List<ValuesAllData>

    @Query("DELETE FROM ValuesAllData where belong = 3")
    fun clearWorkersDB()
}