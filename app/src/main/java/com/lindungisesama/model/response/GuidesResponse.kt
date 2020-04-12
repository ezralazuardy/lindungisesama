package com.lindungisesama.model.response

import com.lindungisesama.model.raw.guides.Result

data class GuidesResponse(
	var status: RetrofitStatus = RetrofitStatus.UNKNOWN,
	var data: List<Result>? = null
)