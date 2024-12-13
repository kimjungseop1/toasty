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
    @Query(
        """
        SELECT * 
        FROM push_message_table 
        WHERE datetime(receiveTime / 1000, 'unixepoch') 
            BETWEEN datetime('now', 'start of day') 
            AND datetime('now', 'start of day', '+1 day')
        ORDER BY receiveTime DESC
    """
    )
    suspend fun getMessageTodayEntities(): List<PushMessageEntity>

    // 오늘의 현재시간기준 일주일전 24시까지의 데이터들만 보여줌
    @Query(
        """
        SELECT * 
        FROM push_message_table 
        WHERE datetime(receiveTime / 1000, 'unixepoch') 
            BETWEEN datetime('now', '-7 day') 
            AND datetime('now', 'start of day')
        ORDER BY receiveTime DESC
    """
    )
    suspend fun getMessageUntilWeekEntities(): List<PushMessageEntity>

    // 오늘 날짜시간기준 30일이 넘어가는 데이터들 삭제
    @Query(
        """
        DELETE 
        FROM push_message_table 
        WHERE datetime(receiveTime/1000, 'unixepoch') < datetime('now', '-30 days', 'start of day');
    """
    )
    suspend fun deleteOldMessages()
}