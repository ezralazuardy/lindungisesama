package com.lindungisesama.repository

import com.lindungisesama.config.AppConfig
import com.lindungisesama.dao.MythbustersDAO
import com.lindungisesama.model.raw.mythbusters.Mythbusters
import retrofit2.Callback

class MythbustersRepository(private val mythbustersDAO: MythbustersDAO) {

	fun getMythbusters(ref: String, callback: Callback<Mythbusters>) {
		mythbustersDAO.getMythbusters(ref = ref).enqueue(callback)
	}

	fun getAllMythbusters(ref: String, callback: Callback<Mythbusters>) {
		mythbustersDAO.getMythbusters(ref = ref, pageSize = AppConfig.THRESHOLD_LIST_MAX)
			.enqueue(callback)
	}
}