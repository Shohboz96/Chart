package uz.max.anychart.room.dao

import androidx.room.Dao
import androidx.room.Query
import uz.max.anychart.room.entities.UsersData
import uz.max.anychart.room.dao.BaseDao

@Dao
interface UserDao : BaseDao<UsersData> {
    @Query("SELECT * FROM UsersData")
    fun getAllUsers(): List<UsersData>

    @Query("DELETE FROM UsersData")
    fun clearDB()
}