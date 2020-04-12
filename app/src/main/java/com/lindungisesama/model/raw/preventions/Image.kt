package com.lindungisesama.model.raw.preventions

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
	val alt: String? = null,
	val copyright: String? = null,
	val dimensions: DimensionsX? = null,
	val url: String? = null
) : Parcelable