package com.syncrown.arpang.db.push_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "push_message_table")
data class PushMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "receiveTime") val receiveTime: Long
)
