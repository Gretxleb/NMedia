package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: PostEntity)

    @Query("UPDATE PostEntity SET likes = likes + 1, likedByMe = 1 WHERE id = :id AND likedByMe = 0")
    fun likeById(id: Long)

    @Query("UPDATE PostEntity SET likes = likes - 1, likedByMe = 0 WHERE id = :id AND likedByMe = 1")
    fun unlikeById(id: Long)

    @Query("UPDATE PostEntity SET shares = shares + 1 WHERE id = :id")
    fun shareById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)
}