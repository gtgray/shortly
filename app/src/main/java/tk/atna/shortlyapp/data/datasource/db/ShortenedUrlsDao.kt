package tk.atna.shortlyapp.data.datasource.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tk.atna.shortlyapp.data.model.ShortenedUrlEntity

@Dao
abstract class ShortenedUrlsDao {

    @Query("SELECT * FROM shortened_urls ORDER BY timestamp DESC")
    abstract fun getShortenedUrls(): Flow<List<ShortenedUrlEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: ShortenedUrlEntity): Long
}