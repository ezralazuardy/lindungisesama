package com.lindungisesama.model.raw.guides

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(
	val alternate_languages: List<String>? = null,
	val `data`: Data? = null,
	val first_publication_date: String? = null,
	val href: String? = null,
	val id: String? = null,
	val lang: String? = null,
	val last_publication_date: String? = null,
	val linked_documents: List<String>? = null,
	val slugs: List<String>? = null,
	val tags: List<String>? = null,
	val type: String? = null,
	val uid: String? = null
) : Parcelable