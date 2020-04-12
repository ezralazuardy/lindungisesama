package com.lindungisesama.repository

import com.lindungisesama.dao.VersionDAO
import com.lindungisesama.model.raw.version.Version
import retrofit2.Callback

class VersionRepository(private val versionDAO: VersionDAO) {

	fun getVersion(ref: String, callback: Callback<Version>) =
		versionDAO.getVersion(ref = ref).enqueue(callback)
}