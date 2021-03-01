package org.d3ifcool.dissajobrecruiter.utils

import com.google.firebase.auth.FirebaseAuth

object AuthHelper {
    val currentUser = FirebaseAuth.getInstance().currentUser
}