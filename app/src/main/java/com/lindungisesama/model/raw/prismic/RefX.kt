package com.lindungisesama.model.raw.prismic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RefX(
	val id: String? = null,
	val isMasterRef: Boolean? = null,
	val label: String? = null,
	val ref: String? = null
) : Parcelable