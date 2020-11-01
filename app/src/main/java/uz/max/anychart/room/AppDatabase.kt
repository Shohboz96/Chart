package uz.max.anychart.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.max.anychart.app.App
import uz.max.anychart.room.dao.ProductDao
import uz.max.anychart.room.dao.UserDao
import uz.max.anychart.room.dao.ValuesAllDao
import uz.max.anychart.room.entities.ValuesAllData
import uz.max.anychart.room.entities.ProductsData
import uz.max.anychart.room.entities.UsersData

@Database(
    entities = [ProductsData::class,UsersData::class,ValuesAllData::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun valuesDao(): ValuesAllDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                   App.instance,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}