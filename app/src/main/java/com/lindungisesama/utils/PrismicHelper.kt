package com.lindungisesama.utils

import android.annotation.SuppressLint

object PrismicHelper {

	const val PRISMIC_PARAM_REF = "ref"
	const val PRISMIC_PARAM_QUERY = "q"
	const val PRISMIC_PARAM_ORDERING = "orderings"
	const val PRISMIC_PARAM_PAGE_SIZE = "pageSize"

	@SuppressLint("DefaultLocale")
	fun generateQueryByType(type: String): String =
		"[[at(document.type,\"${type.trim().toLowerCase()}\")]]"

	fun generateOrderingByDate(): String = "[document.first_publication_date]"

	fun generateOrderingByDateDescending(): String = "[document.first_publication_date desc]"
}