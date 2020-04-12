package com.lindungisesama.model.raw.prismic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fields(
	val after: After? = null,
	val fetch: Fetch? = null,
	val fetchLinks: FetchLinks? = null,
	val graphQuery: GraphQuery? = null,
	val lang: Lang? = null,
	val orderings: Orderings? = null,
	val page: Page? = null,
	val pageSize: PageSize? = null,
	val q: Q? = null,
	val ref: Ref? = null,
	val referer: Referer? = null
) : Parcelable