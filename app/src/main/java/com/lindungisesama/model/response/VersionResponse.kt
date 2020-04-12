package com.lindungisesama.model.response

import com.lindungisesama.model.raw.version.Result

data class VersionResponse(
	var status: RetrofitStatus = RetrofitStatus.UNKNOWN,
	var data: Result? = null
)