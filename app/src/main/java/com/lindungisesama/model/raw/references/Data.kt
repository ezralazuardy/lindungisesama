package com.lindungisesama.model.raw.references

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
	val link: String? = null
) : Parcelable