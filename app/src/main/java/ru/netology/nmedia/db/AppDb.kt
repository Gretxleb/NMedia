package ru.netology.nmedia.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia.model.Post

@Database(entities = [Post::class], version = 1)
abstract class AppDb : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "app.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}