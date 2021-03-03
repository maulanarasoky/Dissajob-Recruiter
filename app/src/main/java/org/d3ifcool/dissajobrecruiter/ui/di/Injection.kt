package org.d3ifcool.dissajobrecruiter.ui.di

import android.content.Context
import org.d3ifcool.dissajobrecruiter.data.source.DataRepository
import org.d3ifcool.dissajobrecruiter.data.source.RemoteDataSource
import org.d3ifcool.dissajobrecruiter.utils.AppExecutors
import org.d3ifcool.dissajobrecruiter.utils.JobHelper
import org.d3ifcool.dissajobrecruiter.utils.UserHelper

object Injection {
    fun provideRepository(context: Context): DataRepository {

        val remoteDataSource = RemoteDataSource.getInstance(JobHelper, UserHelper)
        val appExecutors = AppExecutors()

        return DataRepository.getInstance(remoteDataSource, appExecutors)
    }
}