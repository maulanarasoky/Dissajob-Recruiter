package org.d3ifcool.dissajobrecruiter.ui.auth

import androidx.lifecycle.ViewModel

class AuthViewModel: ViewModel() {
    val authState = FirebaseUserLiveData()
}