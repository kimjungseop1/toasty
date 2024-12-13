package com.syncrown.arpang.db.search_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SearchWordDao {
    // 새 데이터를 삽입
    @Insert
    suspend fun insertWord(entity: SearchWordEntity)

    // 동일한 searchWord 값이 있으면 삭제 후 삽입
    suspend fun insertOrUpdateWord(entity: SearchWordEntity) {
        deleteWordByText(entity.searchWord) // 동일한 단어 삭제
        insertWord(entity) // 새로운 데이터 삽입
    }

    // 현재 저장된 데이터 개수 반환
    @Query("""
        SELECT COUNT(*)
        FROM search_word_table
    """)
    suspend fun getWordCount(): Int

    // 가장 오래된 데이터 삭제
    @Query("""
        DELETE FROM search_word_table 
        WHERE id = (SELECT id FROM search_word_table ORDER BY id ASC LIMIT 1)
    """)
    suspend fun deleteOldestWord()

    // 최신 데이터 10개 가져오기
    @Query("""
        SELECT * 
        FROM search_word_table 
        ORDER BY id 
        DESC LIMIT 10""")
    suspend fun getWordsEntities(): List<SearchWordEntity>

    // 특정 단어와 동일한 데이터를 삭제
    @Query("""
        DELETE FROM search_word_table
        WHERE searchWord = :word
    """)
    suspend fun deleteWordByText(word: String)

    // 모든 데이터를 삭제
    @Query("""
        DELETE FROM search_word_table
    """)
    suspend fun deleteAllWords()
}