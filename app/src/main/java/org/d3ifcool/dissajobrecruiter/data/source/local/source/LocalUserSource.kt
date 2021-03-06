package org.d3ifcool.dissajobrecruiter.data.source.local.source

import org.d3ifcool.dissajobrecruiter.data.source.local.room.UserDao

class LocalUserSource private constructor(
    private val mUserDao: UserDao
) {

    companion object {
        private var INSTANCE: LocalUserSource? = null

        fun getInstance(userDao: UserDao): LocalUserSource =
            INSTANCE ?: LocalUserSource(userDao)
    }
}