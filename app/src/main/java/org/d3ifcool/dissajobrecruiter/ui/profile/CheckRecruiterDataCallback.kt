package org.d3ifcool.dissajobrecruiter.ui.profile

interface CheckRecruiterDataCallback {
    fun allDataAvailable()
    fun profileDataNotAvailable()
    fun phoneNumberNotAvailable()
}