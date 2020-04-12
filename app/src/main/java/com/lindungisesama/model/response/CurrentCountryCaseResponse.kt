package com.lindungisesama.model.response

import com.lindungisesama.model.raw.current_country_case.CurrentCountryCase

data class CurrentCountryCaseResponse(
	var status: RetrofitStatus = RetrofitStatus.UNKNOWN,
	var data: CurrentCountryCase? = null
)