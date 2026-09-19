package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ActivityLogEntity
import com.example.data.model.AdminEntity
import com.example.data.model.AppEntity
import com.example.data.model.DownloadEntity
import com.example.data.model.FavoriteEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM apps WHERE published = 1 ORDER BY createdAt DESC")
    fun getAllPublishedApps(): Flow<List<AppEntity>>

    @Query("SELECT * FROM apps ORDER BY createdAt DESC")
    fun getAllAppsAdmin(): Flow<List<AppEntity>>

    @Query("SELECT * FROM apps WHERE id = :id LIMIT 1")
    fun getAppById(id: String): Flow<AppEntity?>

    @Query("SELECT * FROM apps WHERE id = :id LIMIT 1")
    suspend fun getAppByIdOnce(id: String): AppEntity?

    @Query("SELECT * FROM apps WHERE published = 1 AND isFeatured = 1 ORDER BY rating DESC LIMIT 6")
    fun getFeaturedApps(): Flow<List<AppEntity>>

    @Query("SELECT * FROM apps WHERE published = 1 AND type = 'APP' ORDER BY createdAt DESC")
    fun getPublishedApps(): Flow<List<AppEntity>>

    @Query("SELECT * FROM apps WHERE published = 1 AND type = 'GAME' ORDER BY createdAt DESC")
    fun getPublishedGames(): Flow<List<AppEntity>>

    @Query("SELECT * FROM apps WHERE published = 1 ORDER BY downloadCount DESC LIMIT 10")
    fun getMostDownloaded(): Flow<List<AppEntity>>

    @Query("SELECT * FROM apps WHERE published = 1 AND category = :category ORDER BY downloadCount DESC")
    fun getByCategory(category: String): Flow<List<AppEntity>>

    @Query("""
        SELECT * FROM apps 
        WHERE published = 1 AND (
            name LIKE '%' || :query || '%' OR 
            developer LIKE '%' || :query || '%' OR 
            packageName LIKE '%' || :query || '%' OR
            category LIKE '%' || :query || '%'
        )
        ORDER BY downloadCount DESC
    """)
    fun searchApps(query: String): Flow<List<AppEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: AppEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(apps: List<AppEntity>)

    @Update
    suspend fun updateApp(app: AppEntity)

    @Delete
    suspend fun deleteApp(app: AppEntity)

    @Query("UPDATE apps SET published = :published WHERE id = :id")
    suspend fun setPublishStatus(id: String, published: Boolean)

    @Query("UPDATE apps SET downloadCount = downloadCount + 1 WHERE id = :id")
    suspend fun incrementDownloadCount(id: String)

    @Query("SELECT COUNT(*) FROM apps")
    fun getTotalAppsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE type = 'APP'")
    fun getAppsOnlyCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE type = 'GAME'")
    fun getGamesOnlyCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE published = 1")
    fun getPublishedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE published = 0")
    fun getDraftCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(downloadCount), 0) FROM apps")
    fun getTotalDownloadsCount(): Flow<Long>
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET status = :status WHERE id = :id")
    suspend fun updateUserStatus(id: String, status: String)

    @Query("UPDATE users SET role = :role WHERE email = :email")
    suspend fun updateUserRoleByEmail(email: String, role: String)

    @Query("SELECT COUNT(*) FROM users")
    fun getTotalUsersCount(): Flow<Int>
}

@Dao
interface AdminDao {
    @Query("SELECT * FROM admins ORDER BY createdAt DESC")
    fun getAllAdmins(): Flow<List<AdminEntity>>

    @Query("SELECT * FROM admins WHERE email = :email LIMIT 1")
    suspend fun getAdminByEmail(email: String): AdminEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmins(admins: List<AdminEntity>)

    @Update
    suspend fun updateAdmin(admin: AdminEntity)

    @Delete
    suspend fun deleteAdmin(admin: AdminEntity)

    @Query("UPDATE admins SET status = :status WHERE id = :id")
    suspend fun updateAdminStatus(id: String, status: String)

    @Query("SELECT COUNT(*) FROM admins WHERE status = 'ACTIVE'")
    fun getActiveAdminsCount(): Flow<Int>
}

@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloads ORDER BY createdAt DESC")
    fun getAllDownloads(): Flow<List<DownloadEntity>>

    @Query("SELECT * FROM downloads WHERE id = :id LIMIT 1")
    fun getDownloadById(id: String): Flow<DownloadEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(download: DownloadEntity)

    @Update
    suspend fun updateDownload(download: DownloadEntity)

    @Delete
    suspend fun deleteDownload(download: DownloadEntity)

    @Query("DELETE FROM downloads WHERE id = :id")
    suspend fun deleteDownloadById(id: String)
}

@Dao
interface FavoriteDao {
    @Query("""
        SELECT apps.* FROM apps
        INNER JOIN favorites ON apps.id = favorites.appId
        WHERE favorites.userId = :userId AND apps.published = 1
        ORDER BY favorites.addedAt DESC
    """)
    fun getFavoriteApps(userId: String): Flow<List<AppEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE appId = :appId AND userId = :userId)")
    fun isFavorite(appId: String, userId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE appId = :appId AND userId = :userId")
    suspend fun removeFavorite(appId: String, userId: String)
}

@Dao
interface ActivityLogDao {
    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecentLogs(): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ActivityLogEntity)
}
