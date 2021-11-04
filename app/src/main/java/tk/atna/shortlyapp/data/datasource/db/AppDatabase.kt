package tk.atna.shortlyapp.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import tk.atna.shortlyapp.data.model.ShortenedUrlEntity

@Database(
    entities = [
        ShortenedUrlEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shortenedUrlsDao(): ShortenedUrlsDao
}