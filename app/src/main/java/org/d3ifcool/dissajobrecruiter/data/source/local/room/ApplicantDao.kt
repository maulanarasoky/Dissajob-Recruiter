package org.d3ifcool.dissajobrecruiter.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity

@Dao
interface ApplicantDao {
    @Query("SELECT * FROM applicants WHERE id = :applicantId")
    fun getApplicantDetails(applicantId: String): LiveData<ApplicantEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApplicant(applicantDetails: ApplicantEntity)
}