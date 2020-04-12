package com.lindungisesama.repository

import com.lindungisesama.dao.CountryDAO
import com.lindungisesama.model.raw.current_country_case.CurrentCountryCase
import retrofit2.Callback

class CountryRepository(private val countryDAO: CountryDAO) {

	fun getCurrentCase(callback: Callback<CurrentCountryCase>) =
		countryDAO.getCurrentCase().enqueue(callback)
}