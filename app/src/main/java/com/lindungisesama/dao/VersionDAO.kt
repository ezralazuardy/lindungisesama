package com.lindungisesama.dao

import android.annotation.SuppressLint
import com.lindungisesama.config.AppConfig.PRISMIC_API_PATH
import com.lindungisesama.config.AppConfig.PRISMIC_DOCUMENT_PATH
import com.lindungisesama.model.raw.version.Version
import com.lindungisesama.utils.PrismicHelper.PRISMIC_PARAM_QUERY
import com.lindungisesama.utils.PrismicHelper.PRISMIC_PARAM_REF
import com.lindungisesama.utils.PrismicHelper.generateQueryByType
import com.lindungisesama.utils.PrismicTypes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@SuppressLint("DefaultLocale")
interface VersionDAO {

	@GET("/{prismic_api_version}/{prismic_document}")
	fun getVersion(
		@Path("prismic_api_version", encoded = true) prismicVersion: String = PRISMIC_API_PATH,
		@Path("prismic_document", encoded = true) prismicDocument: String = PRISMIC_DOCUMENT_PATH,
		@Query(PRISMIC_PARAM_REF) ref: String,
		@Query(PRISMIC_PARAM_QUERY) query: String = generateQueryByType(PrismicTypes.VERSION.name.toLowerCase())
	): Call<Version>
}