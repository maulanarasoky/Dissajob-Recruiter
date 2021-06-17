package org.d3ifcool.dissajobrecruiter.utils.dummy

import org.d3ifcool.dissajobrecruiter.data.source.local.entity.application.ApplicationEntity

object ApplicationDummy {
    fun generateApplicationsData(): List<ApplicationEntity> {
        val applications = ArrayList<ApplicationEntity>()
        applications.add(
            ApplicationEntity(
                "-MbklxQlRxjUnNpducxi",
                "MiGz2j0NTyTJXxsAP8uLyIOv8QN2",
                "-MaToCvLrfqh9fc595kh",
                "2021-06-09 19:38:18",
                "2021-06-14 10:13:02",
                "Accepted",
                false,
                "jtXzHFCNlTfNkgMeBE7014kLnZq2"
            )
        )

        return applications
    }
}