package org.d3ifcool.dissajobrecruiter.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.RecruiterEntity

@Dao
interface RecruiterDao {
    @Query("SELECT * FROM recruiters WHERE id = :userId")
    fun getRecruiterData(userId: String): LiveData<RecruiterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecruiterData(recruiterProfile: RecruiterEntity)
}