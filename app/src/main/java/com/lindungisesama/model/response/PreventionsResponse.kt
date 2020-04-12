package com.lindungisesama.model.response

import com.lindungisesama.model.raw.preventions.Result

data class PreventionsResponse(
	var status: RetrofitStatus = RetrofitStatus.UNKNOWN,
	var data: List<Result>? = null
)