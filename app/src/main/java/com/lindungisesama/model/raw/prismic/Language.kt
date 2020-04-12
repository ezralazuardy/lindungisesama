package com.lindungisesama.model.raw.prismic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Language(
	val id: String? = null,
	val name: String? = null
) : Parcelable