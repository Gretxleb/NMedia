package ru.netology.nmedia.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getById(id: Long): LiveData<PostEntity?>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getByIdOnce(id: Long): PostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: PostEntity): Long

    @Update
    fun update(post: PostEntity)

    @Query("DELETE FROM posts WHERE id = :id")
    fun deleteById(id: Long)
}