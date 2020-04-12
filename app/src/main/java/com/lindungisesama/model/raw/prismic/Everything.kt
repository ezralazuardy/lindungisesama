package com.lindungisesama.model.raw.prismic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Everything(
	val action: String? = null,
	val enctype: String? = null,
	val fields: Fields? = null,
	val method: String? = null
) : Parcelable