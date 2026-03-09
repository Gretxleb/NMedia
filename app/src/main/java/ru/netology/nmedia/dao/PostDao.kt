package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.nmedia.model.Post

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Update
    fun update(post: Post)

    @Query("""
        UPDATE posts SET 
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = NOT likedByMe
        WHERE id = :id
    """)
    fun likeById(id: Long)

    @Query("DELETE FROM posts WHERE id = :id")
    fun removeById(id: Long)

    @Query("""
        UPDATE posts SET 
        shares = shares + 1
        WHERE id = :id
    """)
    fun shareById(id: Long)
}