package com.lindungisesama.model.response

import com.lindungisesama.model.raw.references.Result

data class ReferencesResponse(
	var status: RetrofitStatus = RetrofitStatus.UNKNOWN,
	var data: List<Result>? = null
)