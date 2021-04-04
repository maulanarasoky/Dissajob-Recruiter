package org.d3ifcool.dissajobrecruiter.ui.profile

interface UpdateProfileCallback {
    fun onSuccess()
    fun onFailure(message: String)
}