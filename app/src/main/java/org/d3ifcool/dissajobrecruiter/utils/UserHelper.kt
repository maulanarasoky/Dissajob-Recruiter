package org.d3ifcool.dissajobrecruiter.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.UserResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteUserSource
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInCallback
import org.d3ifcool.dissajobrecruiter.ui.signup.SignUpCallback

object UserHelper {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users").child("recruiters")

    fun signUp(
        email: String,
        password: String,
        user: UserResponseEntity,
        callback: SignUpCallback
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { insert ->
            if (insert.isSuccessful) {
                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { verify ->
                    if (verify.isSuccessful) {
                        user.id = auth.currentUser?.uid.toString()
                        insertData(user, callback)
                        auth.signOut()
                    }
                }
            }
        }
    }

    fun signIn(email: String, password: String, callback: SignInCallback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { check ->
            if (check.isSuccessful) {
                if (auth.currentUser?.isEmailVerified!!) {
                    callback.onSuccess()
                } else {
                    callback.onNotVerified()
                }
            } else {
                callback.onFailure()
            }
        }
    }

    private fun insertData(user: UserResponseEntity, callback: SignUpCallback) {
        database.child(user.id.toString()).setValue(user).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(it.message.toString())
        }
    }

    fun getUserProfile(userId: String, callback: RemoteUserSource.LoadUserProfileCallback) {
        database.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userProfile = UserResponseEntity(
                        dataSnapshot.key.toString(),
                        dataSnapshot.child("firstName").value.toString(),
                        dataSnapshot.child("lastName").value.toString(),
                        dataSnapshot.child("fullName").value.toString(),
                        dataSnapshot.child("address").value.toString(),
                        dataSnapshot.child("phoneNumber").value.toString(),
                        dataSnapshot.child("role").value.toString(),
                        dataSnapshot.child("imagePath").value.toString()
                    )
                    callback.onUserProfileReceived(userProfile)
                }
            }

        })
    }

    fun updateProfileData(user: UserResponseEntity, callback: UpdateProfileCallback) {
        database.child(user.id.toString()).setValue(user).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(it.message.toString())
        }
    }

    fun updateEmailProfile(userId: String, email: String, callback: UpdateProfileCallback) {
        database.child(userId).child("email").setValue(email).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(it.message.toString())
        }
    }
}