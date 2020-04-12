package com.lindungisesama.ui.main.fragment.faq

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.lindungisesama.R
import com.lindungisesama.model.raw.faq.Result
import com.lindungisesama.utils.PostContentHelper
import com.lindungisesama.utils.PostContentHelper.sizeInDp
import com.lindungisesama.utils.RichContentType
import com.lindungisesama.utils.RichSpanType
import com.lindungisesama.utils.StyleSpanHelper.boldSpan
import com.lindungisesama.utils.StyleSpanHelper.coloredSpan
import com.lindungisesama.utils.StyleSpanHelper.exclusiveSpanRule
import com.lindungisesama.utils.StyleSpanHelper.italicSpan
import com.lindungisesama.utils.StyleSpanHelper.underlineSpan
import com.stfalcon.imageviewer.StfalconImageViewer
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.layout_faq_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class FAQDetailFragment(
	private val result: Result
) : BottomSheetDialogFragment(), AnkoLogger {

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.layout_faq_detail, container, false)

	@SuppressLint("DefaultLocale")
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		context?.let { context ->
			result.data?.let { data ->
				data.question?.let { question -> txt_question.text = question.capitalize().trim() }
				data.answer?.let { answers ->
					var listItemCounter = 1
					for (answer in answers) {
						when (PostContentHelper.identifyRichContentType(answer.type)) {
							RichContentType.HEADING -> answer.text?.let { contentText ->
								listItemCounter = 1
								val params = FrameLayout.LayoutParams(
									FrameLayout.LayoutParams.MATCH_PARENT,
									FrameLayout.LayoutParams.WRAP_CONTENT
								)
								params.setMargins(0, 0, 0, sizeInDp(2, resources))
								val spannableString = SpannableString(contentText).apply {
									setSpan(
										boldSpan(), 0, contentText.length,
										exclusiveSpanRule()
									)
								}
								val textView = TextView(context).apply {
									layoutParams = params
									typeface =
										ResourcesCompat.getFont(context, R.font.montserrat_bold)
									includeFontPadding = false
									text = spannableString.trim()
									setTextColor(Color.BLACK)
									setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
										justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
								}
								content_layout.addView(textView)
							}
							RichContentType.PARAGRAPH -> answer.text?.let { contentText ->
								listItemCounter = 1
								val params = FrameLayout.LayoutParams(
									FrameLayout.LayoutParams.MATCH_PARENT,
									FrameLayout.LayoutParams.WRAP_CONTENT
								)
								params.setMargins(0, 0, 0, sizeInDp(8, resources))
								val spannableString = SpannableString(contentText).apply {
									answer.spans?.let { spans ->
										for (j in spans) {
											j.end?.let { end ->
												val endSpan =
													if (end > answer.text.length) answer.text.length else end
												when (PostContentHelper.identifyRichSpanType(j.type)) {
													RichSpanType.STRONG -> j.start?.let {
														setSpan(
															boldSpan(), j.start, endSpan,
															exclusiveSpanRule()
														)
													}
													RichSpanType.EM -> j.start?.let {
														setSpan(
															italicSpan(), j.start, endSpan,
															exclusiveSpanRule()
														)
													}
													RichSpanType.HYPERLINK -> j.start?.let {
														setSpan(
															underlineSpan(), j.start, endSpan,
															exclusiveSpanRule()
														)
														setSpan(
															coloredSpan(Color.BLUE),
															j.start,
															endSpan,
															exclusiveSpanRule()
														)
														setSpan(object : ClickableSpan() {
															override fun onClick(widget: View) {
																j.data?.url?.let { url ->
																	FinestWebView
																		.Builder(context)
																		.show(url)
																}
															}
														}, j.start, endSpan, exclusiveSpanRule())
													}
													else -> error { "can't recognize span type: $j" }
												}
											}
										}
									}
								}
								val textView = TextView(context).apply {
									layoutParams = params
									typeface = ResourcesCompat.getFont(context, R.font.montserrat)
									includeFontPadding = false
									movementMethod = LinkMovementMethod.getInstance()
									text = spannableString.trim()
									setTextColor(Color.BLACK)
									setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
										justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
								}
								content_layout.addView(textView)
							}
							RichContentType.LIST_ITEM -> answer.text?.let { contentText ->
								val parentParams = LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT
								)
								val tableParams = TableLayout.LayoutParams(
									TableLayout.LayoutParams.WRAP_CONTENT,
									TableLayout.LayoutParams.WRAP_CONTENT
								)
								val rowNumberParams = TableRow.LayoutParams(
									sizeInDp(18, resources),
									TableRow.LayoutParams.WRAP_CONTENT
								)
								val rowTextParams = TableRow.LayoutParams(
									TableRow.LayoutParams.WRAP_CONTENT,
									TableRow.LayoutParams.WRAP_CONTENT
								)
								parentParams.setMargins(0, 0, 0, sizeInDp(2, resources))
								rowTextParams.weight = 1f
								val spannableString = SpannableString(contentText).apply {
									answer.spans?.let { spans ->
										for (j in spans) {
											j.end?.let { end ->
												val endSpan =
													if (end > answer.text.length) answer.text.length else end
												when (PostContentHelper.identifyRichSpanType(j.type)) {
													RichSpanType.STRONG -> j.start?.let {
														setSpan(
															boldSpan(), j.start, endSpan,
															exclusiveSpanRule()
														)
													}
													RichSpanType.EM -> j.start?.let {
														setSpan(
															italicSpan(), j.start, endSpan,
															exclusiveSpanRule()
														)
													}
													RichSpanType.HYPERLINK -> j.start?.let {
														setSpan(
															underlineSpan(), j.start, endSpan,
															exclusiveSpanRule()
														)
														setSpan(
															coloredSpan(Color.BLUE),
															j.start,
															endSpan,
															exclusiveSpanRule()
														)
														setSpan(object : ClickableSpan() {
															override fun onClick(widget: View) {
																j.data?.url?.let { url ->
																	FinestWebView
																		.Builder(context)
																		.show(url)
																}
															}
														}, j.start, endSpan, exclusiveSpanRule())
													}
													else -> error { "can't recognize span type: $j" }
												}
											}
										}
									}
								}
								val numberTextView = TextView(context).apply {
									layoutParams = rowNumberParams
									typeface = ResourcesCompat.getFont(context, R.font.montserrat)
									includeFontPadding = false
									text = resources.getString(
										R.string.text_post_item_list_number,
										listItemCounter++
									)
									textAlignment = View.TEXT_ALIGNMENT_TEXT_START
									setTextColor(Color.BLACK)
									setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
								}
								val textView = TextView(context).apply {
									layoutParams = rowTextParams
									typeface = ResourcesCompat.getFont(context, R.font.montserrat)
									includeFontPadding = true
									movementMethod = LinkMovementMethod.getInstance()
									text = spannableString.trim()
									textAlignment = View.TEXT_ALIGNMENT_TEXT_START
									setTextColor(Color.BLACK)
									setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
										justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
								}
								val tableLayout = TableLayout(context).apply {
									layoutParams = parentParams
								}
								val tableRow = TableRow(context).apply {
									layoutParams = tableParams
								}
								tableRow.addView(numberTextView)
								tableRow.addView(textView)
								tableLayout.addView(tableRow)
								content_layout.addView(tableLayout)
							}
							RichContentType.O_LIST_ITEM -> answer.text?.let { contentText ->
								val parentParams = LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT
								)
								val tableParams = TableLayout.LayoutParams(
									TableLayout.LayoutParams.WRAP_CONTENT,
									TableLayout.LayoutParams.WRAP_CONTENT
								)
								val rowBulletParams = TableRow.LayoutParams(
									sizeInDp(18, resources),
									TableRow.LayoutParams.WRAP_CONTENT
								)
								val rowTextParams = TableRow.LayoutParams(
									TableRow.LayoutParams.WRAP_CONTENT,
									TableRow.LayoutParams.WRAP_CONTENT
								)
								parentParams.setMargins(0, 0, 0, sizeInDp(2, resources))
								rowTextParams.weight = 1f
								val spannableString = SpannableString(contentText).apply {
									answer.spans?.let { spans ->
										for (j in spans) {
											j.end?.let { end ->
												val endSpan =
													if (end > answer.text.length) answer.text.length else end
												when (PostContentHelper.identifyRichSpanType(j.type)) {
													RichSpanType.STRONG -> j.start?.let {
														setSpan(
															boldSpan(), j.start, endSpan,
															exclusiveSpanRule()
														)
													}
													RichSpanType.EM -> j.start?.let {
														setSpan(
															italicSpan(), j.start, endSpan,
															exclusiveSpanRule()
														)
													}
													RichSpanType.HYPERLINK -> j.start?.let {
														setSpan(
															underlineSpan(), j.start, endSpan,
															exclusiveSpanRule()
														)
														setSpan(
															coloredSpan(Color.BLUE),
															j.start,
															endSpan,
															exclusiveSpanRule()
														)
														setSpan(object : ClickableSpan() {
															override fun onClick(widget: View) {
																j.data?.url?.let { url ->
																	FinestWebView
																		.Builder(context)
																		.show(url)
																}
															}
														}, j.start, endSpan, exclusiveSpanRule())
													}
													else -> error { "can't recognize span type: $j" }
												}
											}
										}
									}
								}
								val bulletTextView = TextView(context).apply {
									layoutParams = rowBulletParams
									typeface =
										ResourcesCompat.getFont(context, R.font.montserrat_bold)
									includeFontPadding = false
									text =
										resources.getString(R.string.text_post_o_item_list_bullet)
									textAlignment = View.TEXT_ALIGNMENT_TEXT_START
									setTextColor(Color.BLACK)
									setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
								}
								val textView = TextView(context).apply {
									layoutParams = rowTextParams
									typeface = ResourcesCompat.getFont(context, R.font.montserrat)
									includeFontPadding = true
									movementMethod = LinkMovementMethod.getInstance()
									text = spannableString.trim()
									textAlignment = View.TEXT_ALIGNMENT_TEXT_START
									setTextColor(Color.BLACK)
									setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
										justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
								}
								val tableLayout = TableLayout(context).apply {
									layoutParams = parentParams
								}
								val tableRow = TableRow(context).apply {
									layoutParams = tableParams
								}
								tableRow.addView(bulletTextView)
								tableRow.addView(textView)
								tableLayout.addView(tableRow)
								content_layout.addView(tableLayout)
							}
							RichContentType.IMAGE -> answer.url?.let { url ->
								listItemCounter = 1
								val params = FrameLayout.LayoutParams(
									FrameLayout.LayoutParams.MATCH_PARENT,
									FrameLayout.LayoutParams.WRAP_CONTENT
								)
								params.setMargins(0, 0, 0, sizeInDp(12, resources))
								val card = MaterialCardView(context).apply {
									layoutParams = params
									isClickable = true
									isFocusable = true
								}
								val image = ImageView(context).apply {
									layoutParams = FrameLayout.LayoutParams(
										FrameLayout.LayoutParams.MATCH_PARENT,
										sizeInDp(200, resources)
									)
									isClickable = true
									isFocusable = true
								}
								Glide.with(this)
									.load(url)
									.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
									.transition(DrawableTransitionOptions.withCrossFade())
									.centerCrop()
									.error(R.color.colorPrimary)
									.listener(object : RequestListener<Drawable> {
										override fun onLoadFailed(
											e: GlideException?,
											model: Any?,
											target: Target<Drawable>?,
											isFirstResource: Boolean
										): Boolean = false

										override fun onResourceReady(
											resource: Drawable?,
											model: Any?,
											target: Target<Drawable>?,
											dataSource: DataSource?,
											isFirstResource: Boolean
										): Boolean {
											image.setOnClickListener {
												StfalconImageViewer.Builder(
													context,
													arrayOf(resource)
												) { view, image ->
													Glide.with(context)
														.load(image)
														.into(view)
												}.withTransitionFrom(image)
													.withHiddenStatusBar(false)
													.show()
											}
											return false
										}
									})
									.into(image)
								card.addView(image)
								content_layout.addView(card)
							}
							else -> error { "can't recognize content type: $answer" }
						}
					}
				}
				data.source.let { source ->
					if (source != null) {
						txt_source.text = source.trim()
						button_source.setOnClickListener {
							FinestWebView.Builder(context).show(source)
						}
					} else button_source.visibility = View.GONE
				}
			}
		}
	}
}