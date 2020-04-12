package com.lindungisesama.model.raw.prismic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Q(
	val multiple: Boolean? = null,
	val type: String? = null
) : Parcelable