package org.d3ifcool.dissajobrecruiter.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity

@Dao
interface ApplicantDao {
    @Query("SELECT * FROM applicants")
    fun getApplicants(): DataSource.Factory<Int, ApplicantEntity>

    @Query("SELECT * FROM applicant_details WHERE id = :applicantId")
    fun getApplicantDetails(applicantId: String): LiveData<ApplicantDetailsEntity>

    @Query("SELECT * FROM applicants WHERE job_id = :jobId")
    fun getApplicantsByJob(jobId: String): LiveData<ApplicantEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApplicants(applicants: List<ApplicantEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApplicantDetails(applicantDetails: ApplicantDetailsEntity)
}