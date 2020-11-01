package uz.max.anychart.room.dao

import androidx.room.Dao
import androidx.room.Query
import uz.max.anychart.room.entities.ProductsData
import uz.max.anychart.room.dao.BaseDao

@Dao
interface ProductDao : BaseDao<ProductsData> {
    @Query("SELECT * FROM ProductsData")
    fun getAllProducts(): List<ProductsData>

    @Query("DELETE FROM ProductsData")
    fun clearDB()
}