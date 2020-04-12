package com.lindungisesama.model.raw.preventions

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(
	val `data`: Data? = null,
	val first_publication_date: String? = null,
	val href: String? = null,
	val id: String? = null,
	val lang: String? = null,
	val last_publication_date: String? = null,
	val slugs: List<String>? = null,
	val type: String? = null
) : Parcelable