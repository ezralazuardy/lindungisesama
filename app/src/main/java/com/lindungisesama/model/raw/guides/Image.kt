package com.lindungisesama.model.raw.guides

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
	val alt: String? = null,
	val copyright: String? = null,
	val dimensions: Dimensions? = null,
	val url: String? = null
) : Parcelable