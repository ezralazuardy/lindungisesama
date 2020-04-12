package com.lindungisesama.model.response

import com.lindungisesama.model.raw.mythbusters.Result

data class MythbustersResponse(
	var status: RetrofitStatus = RetrofitStatus.UNKNOWN,
	var data: List<Result>? = null
)