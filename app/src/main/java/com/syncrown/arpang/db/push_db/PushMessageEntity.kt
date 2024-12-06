package com.syncrown.arpang.db.push_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "push_message_table")
data class PushMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val body: String,
    val imageUrl: String,
    val receiveTime: Long
)
