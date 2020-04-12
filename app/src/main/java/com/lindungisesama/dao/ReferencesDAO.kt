package com.lindungisesama.dao

import android.annotation.SuppressLint
import com.lindungisesama.config.AppConfig.PRISMIC_API_PATH
import com.lindungisesama.config.AppConfig.PRISMIC_DOCUMENT_PATH
import com.lindungisesama.config.AppConfig.THRESHOLD_LIST_SMALL
import com.lindungisesama.model.raw.references.References
import com.lindungisesama.utils.PrismicHelper.PRISMIC_PARAM_ORDERING
import com.lindungisesama.utils.PrismicHelper.PRISMIC_PARAM_PAGE_SIZE
import com.lindungisesama.utils.PrismicHelper.PRISMIC_PARAM_QUERY
import com.lindungisesama.utils.PrismicHelper.PRISMIC_PARAM_REF
import com.lindungisesama.utils.PrismicHelper.generateOrderingByDate
import com.lindungisesama.utils.PrismicHelper.generateQueryByType
import com.lindungisesama.utils.PrismicTypes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@SuppressLint("DefaultLocale")
interface ReferencesDAO {

	@GET("/{prismic_api_version}/{prismic_document}")
	fun getReferences(
		@Path("prismic_api_version", encoded = true) prismicVersion: String = PRISMIC_API_PATH,
		@Path("prismic_document", encoded = true) prismicDocument: String = PRISMIC_DOCUMENT_PATH,
		@Query(PRISMIC_PARAM_REF) ref: String,
		@Query(PRISMIC_PARAM_PAGE_SIZE) pageSize: Int = THRESHOLD_LIST_SMALL,
		@Query(PRISMIC_PARAM_QUERY) query: String = generateQueryByType(PrismicTypes.REFERENCES.name.toLowerCase()),
		@Query(PRISMIC_PARAM_ORDERING) ordering: String = generateOrderingByDate()
	): Call<References>
}