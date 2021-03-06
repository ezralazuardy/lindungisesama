package com.lindungisesama.model.raw.preventions

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DimensionsX(
	val height: Int? = null,
	val width: Int? = null
) : Parcelable