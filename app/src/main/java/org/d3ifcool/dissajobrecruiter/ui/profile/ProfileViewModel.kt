package org.d3ifcool.dissajobrecruiter.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.UserEntity
import org.d3ifcool.dissajobrecruiter.data.source.repository.user.UserRepository
import org.d3ifcool.dissajobrecruiter.vo.Resource

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserProfile(userId: String): LiveData<Resource<UserEntity>> =
        userRepository.getUserProfile(userId)
}