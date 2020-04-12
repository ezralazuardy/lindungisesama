package com.lindungisesama.repository

import com.lindungisesama.dao.PreventionsDAO
import com.lindungisesama.model.raw.preventions.Preventions
import retrofit2.Callback

class PreventionsRepository(private val preventionsDAO: PreventionsDAO) {

	fun getPreventions(ref: String, callback: Callback<Preventions>) {
		preventionsDAO.getPreventions(ref = ref).enqueue(callback)
	}
}