package com.lindungisesama.repository

import com.lindungisesama.config.AppConfig.THRESHOLD_LIST_MAX
import com.lindungisesama.dao.ReferencesDAO
import com.lindungisesama.model.raw.references.References
import retrofit2.Callback

class ReferencesRepository(private val referencesDAO: ReferencesDAO) {

	fun getReferences(ref: String, callback: Callback<References>) {
		referencesDAO.getReferences(ref = ref).enqueue(callback)
	}

	fun getAllReferences(ref: String, callback: Callback<References>) {
		referencesDAO
			.getReferences(ref = ref, pageSize = THRESHOLD_LIST_MAX)
			.enqueue(callback)
	}
}