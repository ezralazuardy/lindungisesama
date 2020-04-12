package com.lindungisesama.model.response

import com.lindungisesama.model.raw.prismic.Prismic

data class PrismicResponse(
	var status: RetrofitStatus = RetrofitStatus.UNKNOWN,
	var data: Prismic? = null
)