package org.d3ifcool.dissajobrecruiter.utils

import android.net.Uri
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.data.source.remote.response.entity.recruiter.UserResponseEntity
import org.d3ifcool.dissajobrecruiter.data.source.remote.source.RemoteUserSource
import org.d3ifcool.dissajobrecruiter.ui.profile.UpdateProfileCallback
import org.d3ifcool.dissajobrecruiter.ui.profile.UploadProfilePictureCallback
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
                        dataSnapshot.child("email").value.toString(),
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
            callback.onFailure(R.string.txt_failure_update_profile)
        }
    }

    fun uploadProfilePicture(image: Uri, callback: UploadProfilePictureCallback) {
        val storageRef = Firebase.storage.reference
        val imageId = database.push().key
        val uploadImage = storageRef.child("recruiter/profile/images/${imageId}").putFile(image)
        uploadImage.addOnSuccessListener {
            callback.onSuccessUpload(imageId.toString())
        }.addOnFailureListener {
            callback.onFailureUpload(R.string.txt_failure_upload_profile_picture)
        }
    }

    fun updateEmailProfile(
        userId: String,
        newEmail: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        auth.signInWithEmailAndPassword(auth.currentUser?.email.toString(), password)
            .addOnSuccessListener {
                updateEmailAuthentication(userId, newEmail, password, callback)
            }
            .addOnFailureListener {
                callback.onFailure(R.string.wrong_password)
            }
    }

    private fun updateEmailAuthentication(
        userId: String,
        newEmail: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        val credential =
            EmailAuthProvider.getCredential(auth.currentUser?.email.toString(), password)
        auth.currentUser?.reauthenticate(credential)
            ?.addOnCompleteListener {
                auth.currentUser!!.updateEmail(newEmail)
                    .addOnSuccessListener {
                        storeNewEmail(userId, newEmail, callback)
                    }
                    .addOnFailureListener {
                        callback.onFailure(R.string.txt_failure_update_profile)
                    }
            }
    }

    private fun storeNewEmail(userId: String, newEmail: String, callback: UpdateProfileCallback) {
        database.child(userId).child("email").setValue(newEmail).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(R.string.txt_failure_update_profile)
        }
    }

    fun updatePhoneNumberProfile(
        userId: String,
        newPhoneNumber: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        auth.signInWithEmailAndPassword(auth.currentUser?.email.toString(), password)
            .addOnSuccessListener {
                storeNewPhoneNumber(userId, newPhoneNumber, callback)
            }
            .addOnFailureListener {
                callback.onFailure(R.string.wrong_password)
            }
    }

    private fun storeNewPhoneNumber(
        userId: String,
        newPhoneNumber: String,
        callback: UpdateProfileCallback
    ) {
        database.child(userId).child("phoneNumber").setValue(newPhoneNumber).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(R.string.txt_failure_update_profile)
        }
    }

    fun updatePasswordProfile(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) {
        auth.signInWithEmailAndPassword(email, oldPassword)
            .addOnSuccessListener {
                storeNewPassword(email, oldPassword, newPassword, callback)
            }
            .addOnFailureListener {
                callback.onFailure(R.string.wrong_password)
            }
    }

    private fun storeNewPassword(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) {
        val credential = EmailAuthProvider.getCredential(email, oldPassword)
        auth.currentUser?.reauthenticate(credential)
            ?.addOnCompleteListener {
                auth.currentUser?.updatePassword(newPassword)!!
                    .addOnSuccessListener {
                        callback.onSuccess()
                    }
                    .addOnFailureListener {
                        callback.onFailure(R.string.txt_failure_update_profile)
                    }
            }
    }
}