package org.d3ifcool.dissajobrecruiter.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserProfile(userId: String): LiveData<UserEntity>
}