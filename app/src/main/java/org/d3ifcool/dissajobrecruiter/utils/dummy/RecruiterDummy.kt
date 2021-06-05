package org.d3ifcool.dissajobrecruiter.utils.dummy

import org.d3ifcool.dissajobrecruiter.data.source.local.entity.recruiter.RecruiterEntity

object RecruiterDummy {
    fun generateRecruiterDetails(): RecruiterEntity {
        val recruiter = RecruiterEntity(
            "RT7zlmPyqte9uhi6W9zoNh3i88E2",
            "Telkom",
            "University",
            "Telkom University",
            "hahaboi02@gmail.com",
            "Jl. Telekomunikasi No 01",
            "-",
            "HRD",
            "-MZ42Vx55oXuKRp2pPL-"
        )

        return recruiter
    }
}