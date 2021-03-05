package org.d3ifcool.dissajobrecruiter.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.JobEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, JobEntity::class, JobDetailsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DissajobRecruiterDatabase : RoomDatabase() {

    abstract fun jobDao(): JobDao
    abstract fun userDao(): UserDao

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