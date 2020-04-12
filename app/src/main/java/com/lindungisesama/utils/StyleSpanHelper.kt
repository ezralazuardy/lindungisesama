package com.lindungisesama.utils

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan

object StyleSpanHelper {

	fun exclusiveSpanRule(): Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE

	fun boldSpan(): StyleSpan = StyleSpan(Typeface.BOLD)

	fun italicSpan(): StyleSpan = StyleSpan(Typeface.ITALIC)

	fun underlineSpan(): UnderlineSpan = UnderlineSpan()

	fun coloredSpan(color: Int): ForegroundColorSpan = ForegroundColorSpan(color)
}