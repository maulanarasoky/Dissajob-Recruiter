package org.d3ifcool.dissajobrecruiter.ui.resetpassword

interface ResetPasswordCallback {
    fun onSuccess()
    fun onFailure(messageId: Int)
}