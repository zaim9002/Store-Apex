package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ActivityLogEntity
import com.example.data.model.AdminEntity
import com.example.data.model.AppEntity
import com.example.data.model.DownloadEntity
import com.example.data.model.FavoriteEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        AppEntity::class,
        UserEntity::class,
        AdminEntity::class,
        DownloadEntity::class,
        FavoriteEntity::class,
        ActivityLogEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ApexStoreDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
    abstract fun userDao(): UserDao
    abstract fun adminDao(): AdminDao
    abstract fun downloadDao(): DownloadDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun activityLogDao(): ActivityLogDao

    companion object {
        @Volatile
        private var INSTANCE: ApexStoreDatabase? = null

        fun getDatabase(context: Context): ApexStoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApexStoreDatabase::class.java,
                    "apex_store_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
