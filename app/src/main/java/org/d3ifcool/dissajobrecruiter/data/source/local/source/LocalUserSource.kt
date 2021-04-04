package org.d3ifcool.dissajobrecruiter.data.source.local.source

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.UserEntity
import org.d3ifcool.dissajobrecruiter.data.source.local.room.UserDao

class LocalUserSource private constructor(
    private val mUserDao: UserDao
) {

    companion object {
        private var INSTANCE: LocalUserSource? = null

        fun getInstance(userDao: UserDao): LocalUserSource =
            INSTANCE ?: LocalUserSource(userDao)
    }

    fun getUserProfile(userId: String): LiveData<UserEntity> = mUserDao.getUserProfile(userId)

    fun insertUserProfile(userProfile: UserEntity) = mUserDao.insertUserProfile(userProfile)

//    fun updateJobDetails(userProfile: UserEntity) = mUserDao.updateUserProfile(userProfile)
}