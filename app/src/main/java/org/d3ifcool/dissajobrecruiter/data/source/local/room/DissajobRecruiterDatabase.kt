package org.d3ifcool.dissajobrecruiter.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.RecruiterEntity

@Database(
    entities = [RecruiterEntity::class, JobEntity::class, JobDetailsEntity::class, ApplicationEntity::class, ApplicantEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DissajobRecruiterDatabase : RoomDatabase() {

    abstract fun jobDao(): JobDao
    abstract fun recruiterDao(): RecruiterDao
    abstract fun applicationDao(): ApplicationDao
    abstract fun applicantDao(): ApplicantDao

    companion object {

        @Volatile
        private var INSTANCE: DissajobRecruiterDatabase? = null

        fun getInstance(context: Context): DissajobRecruiterDatabase {
            if (INSTANCE == null) {
                synchronized(DissajobRecruiterDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            DissajobRecruiterDatabase::class.java,
                            "dissajobrecruiter.db"
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return INSTANCE as DissajobRecruiterDatabase
        }
    }
}