package com.syncrown.arpang.db.ar_match

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArVideoImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoImage(entity: ArVideoImageEntity)

    @Query("SELECT * FROM ar_video_image_table WHERE videoPath = :videoPath")
    suspend fun getImagePathByVideoPath(videoPath: String): ArVideoImageEntity?

    @Query("SELECT * FROM ar_video_image_table WHERE imagePath = :imagePath")
    suspend fun getVideoPathByImagePath(imagePath: String): ArVideoImageEntity?

    @Query("SELECT * FROM ar_video_image_table")
    suspend fun getAllVideoImageEntities(): List<ArVideoImageEntity>

    @Query("SELECT COUNT(*) FROM ar_video_image_table")
    suspend fun getRowCount(): Int

    @Query("DELETE FROM ar_video_image_table WHERE id IN (SELECT id FROM ar_video_image_table ORDER BY id ASC LIMIT :limit)")
    suspend fun deleteOldestEntries(limit: Int)
}