package org.d3ifcool.dissajobrecruiter.ui.applicant.experience

import org.d3ifcool.dissajobrecruiter.data.source.local.entity.experience.ExperienceEntity

interface OnExperienceItemClickListener {
    fun onClickItem(experienceData: ExperienceEntity)
}