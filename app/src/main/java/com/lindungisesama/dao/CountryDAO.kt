package com.lindungisesama.dao

import com.lindungisesama.config.AppConfig.INDONESIA_COUNTRY_ID
import com.lindungisesama.model.raw.current_country_case.CurrentCountryCase
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryDAO {

	@GET("/v2/countries/{country_id}")
	fun getCurrentCase(
		@Path("country_id") countryCode: Int = INDONESIA_COUNTRY_ID
	): Call<CurrentCountryCase>
}