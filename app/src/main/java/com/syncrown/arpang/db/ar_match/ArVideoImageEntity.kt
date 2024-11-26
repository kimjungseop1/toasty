package com.syncrown.arpang.db.ar_match

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ar_video_image_table")
data class ArVideoImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val videoPath: String,
    val imagePath: String
)
