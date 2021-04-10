package org.d3ifcool.dissajobrecruiter.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.ui.signin.SignInActivity

class LogoutDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.AlertDialogTheme)
            val view =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.logout_dialog_view, null, false)
            with(builder) {
                setView(view)
                setCancelable(false)
                setPositiveButton(R.string.confirm_logout_text) { _, _ ->
                    logout()
                }
                setNegativeButton(R.string.confirm_cancel_text) { _, _ ->
                    dismiss()
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        activity?.startActivity(Intent(activity, SignInActivity::class.java))
        activity?.finish()
    }
}