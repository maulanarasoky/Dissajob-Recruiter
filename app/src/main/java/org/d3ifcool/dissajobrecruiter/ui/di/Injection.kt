package org.d3ifcool.dissajobrecruiter.ui.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.d3ifcool.dissajobrecruiter.data.source.DataRepository
import org.d3ifcool.dissajobrecruiter.data.source.local.LocalDataSource
import org.d3ifcool.dissajobrecruiter.data.source.local.room.DissajobRecruiterDatabase
import org.d3ifcool.dissajobrecruiter.data.source.remote.RemoteDataSource
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.JobHelper
import org.d3ifcool.dissajobrecruiter.utils.NetworkStateCallback
import org.d3ifcool.dissajobrecruiter.utils.UserHelper

object Injection {
    fun provideRepository(context: Context): DataRepository {

        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteDataSource.getInstance(JobHelper, UserHelper)
        val localDataSource = LocalDataSource.getInstance(database.jobDao(), database.userDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun isConnected(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return DataRepository.getInstance(remoteDataSource, localDataSource, appExecutors, callback)
    }
}