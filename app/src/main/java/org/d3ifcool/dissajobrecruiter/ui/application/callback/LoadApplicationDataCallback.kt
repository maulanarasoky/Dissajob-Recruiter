package org.d3ifcool.dissajobrecruiter.ui.application.callback

import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.application.ApplicationResponseEntity

interface LoadApplicationDataCallback {
    fun onApplicationDataReceived(applicationResponse: ApplicationResponseEntity): ApplicationResponseEntity
}