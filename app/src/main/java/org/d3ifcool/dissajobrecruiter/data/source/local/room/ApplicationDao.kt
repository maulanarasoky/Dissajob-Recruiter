package org.d3ifcool.dissajobrecruiter.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM applications")
    fun getApplications(): DataSource.Factory<Int, ApplicationEntity>

    @Query("SELECT * FROM applications WHERE id = :applicationId")
    fun getApplicationById(applicationId: String): LiveData<ApplicationEntity>

    @Query("SELECT * FROM applications WHERE status = :status")
    fun getApplicationsByStatus(status: String): DataSource.Factory<Int, ApplicationEntity>

    @Query("SELECT * FROM applications WHERE is_marked = 1")
    fun getMarkedApplications(): DataSource.Factory<Int, ApplicationEntity>

    @Query("SELECT * FROM applications WHERE job_id = :jobId")
    fun getApplicationsByJob(jobId: String): DataSource.Factory<Int, ApplicationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApplication(applicants: List<ApplicationEntity>)
}