package com.syncrown.arpang.db.push_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PushMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(entity: PushMessageEntity)

    // 오늘의 메시지만 가져오는 쿼리
    @Query("""
        SELECT * 
        FROM push_message_table 
        WHERE DATE(receiveTime / 1000, 'unixepoch') = DATE('now')
    """)
    suspend fun getMessageTodayEntities(): List<PushMessageEntity>

    // 일주일 이내의 메시지를 가져오는 쿼리
    @Query("""
        SELECT * 
        FROM push_message_table 
        WHERE receiveTime / 1000 >= strftime('%s', 'now', '-7 days')
    """)
    suspend fun getMessageUntilWeekEntities(): List<PushMessageEntity>

    // 30일이 넘는 데이터를 삭제하는 쿼리
    @Query("""
        DELETE 
        FROM push_message_table 
        WHERE receiveTime / 1000 < strftime('%s', 'now', '-30 days')
    """)
    suspend fun deleteOldMessages()
}