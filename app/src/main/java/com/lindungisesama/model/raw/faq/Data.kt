package com.lindungisesama.model.raw.faq

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
	val answer: List<Answer>? = null,
	val question: String? = null,
	val source: String? = null
) : Parcelable