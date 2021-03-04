package org.d3ifcool.dissajobrecruiter.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.d3ifcool.dissajobrecruiter.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    private lateinit var fragmentNotificationBinding: FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentNotificationBinding =
            FragmentNotificationBinding.inflate(layoutInflater, container, false)
        return fragmentNotificationBinding.root
    }
}