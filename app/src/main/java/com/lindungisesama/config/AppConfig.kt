package com.lindungisesama.config

object AppConfig {
	const val COVID_API_ENDPOINT = "https://corona.lmao.ninja"
	const val INDONESIA_COUNTRY_ID = 360

	const val PRISMIC_ENDPOINT = "https://lindungisesama.cdn.prismic.io"
	const val PRISMIC_API_PATH = "api/v2"
	const val PRISMIC_DOCUMENT_PATH = "documents/search"

	const val IO_TIMEOUT = 10L

	const val PRISMIC_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss+0000"
	const val DEFAULT_TIMESTAMP_FORMAT = "dd MMMM yyyy HH:mm:ss z"
	const val POST_TIMESTAMP_FORMAT = "dd MMMM yyyy HH:mm a"

	const val THRESHOLD_LIST_SMALL = 4
	const val THRESHOLD_LIST_MAX = 100
}