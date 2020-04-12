package com.lindungisesama.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.TypedValue
import com.lindungisesama.config.AppConfig.POST_TIMESTAMP_FORMAT
import com.lindungisesama.config.AppConfig.PRISMIC_TIMESTAMP_FORMAT
import java.text.SimpleDateFormat
import java.util.*

object PostContentHelper {

	fun sizeInDp(size: Int, resources: Resources): Int =
		TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, size.toFloat(), resources.displayMetrics
		).toInt()

	fun convertTimestamp(
		timestamp: String,
		format: String = POST_TIMESTAMP_FORMAT
	): String =
		SimpleDateFormat(PRISMIC_TIMESTAMP_FORMAT, Locale.getDefault()).parse(timestamp).let {
			if (it != null) SimpleDateFormat(format, Locale.getDefault()).format(it) else "-"
		}

	@SuppressLint("DefaultLocale")
	fun identifyRichContentType(type: String?): RichContentType =
		when (type?.replace("-", "_")?.toUpperCase()?.trim()) {
			RichContentType.HEADING1.name,
			RichContentType.HEADING2.name,
			RichContentType.HEADING3.name,
			RichContentType.HEADING4.name,
			RichContentType.HEADING5.name,
			RichContentType.HEADING6.name -> RichContentType.HEADING
			RichContentType.PARAGRAPH.name -> RichContentType.PARAGRAPH
			RichContentType.IMAGE.name -> RichContentType.IMAGE
			RichContentType.VIDEO.name -> RichContentType.VIDEO
			RichContentType.PREFORMATTED.name -> RichContentType.PREFORMATTED
			RichContentType.LIST_ITEM.name -> RichContentType.LIST_ITEM
			RichContentType.O_LIST_ITEM.name -> RichContentType.O_LIST_ITEM
			else -> RichContentType.UNKNOWN
		}

	@SuppressLint("DefaultLocale")
	fun identifyRichSpanType(type: String?): RichSpanType =
		when (type?.replace("-", "_")?.toUpperCase()?.trim()) {
			RichSpanType.STRONG.name -> RichSpanType.STRONG
			RichSpanType.EM.name -> RichSpanType.EM
			RichSpanType.HYPERLINK.name -> RichSpanType.HYPERLINK
			else -> RichSpanType.UNKNOWN
		}
}