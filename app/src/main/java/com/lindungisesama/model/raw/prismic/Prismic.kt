package com.lindungisesama.model.raw.prismic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Prismic(
	val bookmarks: Bookmarks? = null,
	val experiments: Experiments? = null,
	val forms: Forms? = null,
	val integrationFieldsRef: String? = null,
	val languages: List<Language>? = null,
	val license: String? = null,
	val oauth_initiate: String? = null,
	val oauth_token: String? = null,
	val refs: List<RefX>? = null,
	val tags: List<String>? = null,
	val types: Types? = null,
	val version: String? = null
) : Parcelable