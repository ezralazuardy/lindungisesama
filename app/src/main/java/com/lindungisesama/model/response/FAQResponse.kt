package com.lindungisesama.model.response

import com.lindungisesama.model.raw.faq.Result

data class FAQResponse(
	var status: RetrofitStatus = RetrofitStatus.UNKNOWN,
	var data: List<Result>? = null
)