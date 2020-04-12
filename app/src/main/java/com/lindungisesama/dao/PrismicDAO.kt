package com.lindungisesama.dao

import com.lindungisesama.config.AppConfig.PRISMIC_API_PATH
import com.lindungisesama.model.raw.prismic.Prismic
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PrismicDAO {

	@GET("/{prismic_api_version}")
	fun getPrismic(
		@Path("prismic_api_version", encoded = true) prismicVersion: String = PRISMIC_API_PATH
	): Call<Prismic>
}