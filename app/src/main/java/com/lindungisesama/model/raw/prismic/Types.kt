package com.lindungisesama.model.raw.prismic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Types(
	val faq: String? = null,
	val guides: String? = null,
	val important_links: String? = null,
	val mythbusters: String? = null,
	val preventions: String? = null,
	val references: String? = null,
	val version: String? = null
) : Parcelable