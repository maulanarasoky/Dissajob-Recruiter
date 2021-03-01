package org.d3ifcool.dissajobrecruiter.ui.signin

interface SignInCallback {
    fun onSuccess()
    fun onNotVerified()
    fun onFailure()
}