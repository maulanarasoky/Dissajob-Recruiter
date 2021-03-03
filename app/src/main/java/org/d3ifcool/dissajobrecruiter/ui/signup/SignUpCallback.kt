package org.d3ifcool.dissajobrecruiter.ui.signup

interface SignUpCallback {
    fun onSuccess()
    fun onFailure(message: String)
}