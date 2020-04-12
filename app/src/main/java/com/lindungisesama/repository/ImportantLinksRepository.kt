package com.lindungisesama.repository

import com.lindungisesama.config.AppConfig
import com.lindungisesama.dao.ImportantLinksDAO
import com.lindungisesama.model.raw.important_links.ImportantLinks
import retrofit2.Callback

class ImportantLinksRepository(private val importantLinksDAO: ImportantLinksDAO) {

	fun getImportantLinks(ref: String, callback: Callback<ImportantLinks>) {
		importantLinksDAO.getImportantLinks(ref = ref).enqueue(callback)
	}

	fun getAllImportantLinks(ref: String, callback: Callback<ImportantLinks>) {
		importantLinksDAO
			.getImportantLinks(ref = ref, pageSize = AppConfig.THRESHOLD_LIST_MAX)
			.enqueue(callback)
	}
}