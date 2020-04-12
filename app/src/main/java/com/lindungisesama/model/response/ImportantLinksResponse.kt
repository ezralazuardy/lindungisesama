package com.lindungisesama.model.response

import com.lindungisesama.model.raw.important_links.Result

data class ImportantLinksResponse(
	var status: RetrofitStatus = RetrofitStatus.UNKNOWN,
	var data: List<Result>? = null
)