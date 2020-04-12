package com.lindungisesama.repository

import com.lindungisesama.config.AppConfig
import com.lindungisesama.dao.GuidesDAO
import com.lindungisesama.model.raw.guides.Guides
import retrofit2.Callback

class GuidesRepository(private val guidesDAO: GuidesDAO) {

	fun getGuides(ref: String, callback: Callback<Guides>) {
		guidesDAO.getGuides(ref = ref).enqueue(callback)
	}

	fun getAllGuides(ref: String, callback: Callback<Guides>) {
		guidesDAO.getGuides(ref = ref, pageSize = AppConfig.THRESHOLD_LIST_MAX)
			.enqueue(callback)
	}
}