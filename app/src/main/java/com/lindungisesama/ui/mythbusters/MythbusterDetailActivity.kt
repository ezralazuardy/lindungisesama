package com.lindungisesama.ui.mythbusters

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
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.lindungisesama.R
import com.lindungisesama.model.raw.mythbusters.Result
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.utils.ErrorHandler
import com.lindungisesama.utils.PostContentHelper
import com.lindungisesama.utils.PostContentHelper.identifyRichContentType
import com.lindungisesama.utils.PostContentHelper.identifyRichSpanType
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
import kotlinx.android.synthetic.main.activity_mythbuster_detail.*
import kotlinx.android.synthetic.main.layout_mythbuster_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class MythbusterDetailActivity : AppCompatActivity(), AnkoLogger {

	companion object {
		const val MYTHBUSTER_DETAIL_DATA = "mythbuster_detail_data"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_mythbuster_detail)
		showLoading()
		try {
			val data: Result? = intent.getParcelableExtra(MYTHBUSTER_DETAIL_DATA) as Result?
			if (data != null) setData(data)
			else ErrorHandler.show(supportFragmentManager, RetrofitStatus.FAILURE)
			back_button.setOnClickListener {
				finish()
			}
		} catch (e: Exception) {
			e.printStackTrace()
			error { "cause: {${e.cause}}\nmessage: ${e.message}" }
			ErrorHandler.show(supportFragmentManager, RetrofitStatus.FAILURE)
		}
	}

	@SuppressLint("DefaultLocale")
	private fun setData(result: Result) {
		if (result.data == null) ErrorHandler.show(supportFragmentManager, RetrofitStatus.FAILURE)
		result.first_publication_date?.let { timestamp ->
			text_timestamp.text = resources
				.getString(
					R.string.text_post_timestamp,
					PostContentHelper.convertTimestamp(timestamp)
				)
		}
		result.data?.let { data ->
			data.image?.let { image ->
				Glide.with(this)
					.load(image.url)
					.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
					.transition(DrawableTransitionOptions.withCrossFade())
					.centerCrop()
					.error(R.color.colorPrimary)
					.into(img_header)
			}
			data.title?.let { title ->
				text_title.text = title.capitalize().trim()
				text_title_small.text = title.capitalize().trim()
			}
			data.created_by?.let { creator ->
				text_creator.text = resources
					.getString(R.string.text_post_creator, creator.capitalize().trim())
			}
			data.content?.let { contents ->
				var listItemCounter = 1
				for (content in contents) {
					when (identifyRichContentType(content.type)) {
						RichContentType.HEADING -> content.text?.let { contentText ->
							listItemCounter = 1
							val params = FrameLayout.LayoutParams(
								FrameLayout.LayoutParams.MATCH_PARENT,
								FrameLayout.LayoutParams.WRAP_CONTENT
							)
							params.setMargins(
								sizeInDp(8, resources),
								sizeInDp(8, resources),
								sizeInDp(8, resources),
								0
							)
							val spannableString = SpannableString(contentText).apply {
								setSpan(
									boldSpan(), 0, contentText.length,
									exclusiveSpanRule()
								)
							}
							val textView = TextView(this).apply {
								layoutParams = params
								typeface = ResourcesCompat.getFont(
									this@MythbusterDetailActivity,
									R.font.montserrat_bold
								)
								includeFontPadding = false
								text = spannableString.trim()
								setTextColor(Color.BLACK)
								setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
									justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
							}
							content_layout.addView(textView)
						}
						RichContentType.PARAGRAPH -> content.text?.let { contentText ->
							listItemCounter = 1
							val params = FrameLayout.LayoutParams(
								FrameLayout.LayoutParams.MATCH_PARENT,
								FrameLayout.LayoutParams.WRAP_CONTENT
							)
							params.setMargins(
								sizeInDp(8, resources),
								sizeInDp(8, resources),
								sizeInDp(8, resources),
								sizeInDp(8, resources)
							)
							val spannableString = SpannableString(contentText).apply {
								content.spans?.let { spans ->
									for (j in spans) {
										j.end?.let { end ->
											val endSpan =
												if (end > content.text.length) content.text.length else end
											when (identifyRichSpanType(j.type)) {
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
														coloredSpan(Color.BLUE), j.start, endSpan,
														exclusiveSpanRule()
													)
													setSpan(object : ClickableSpan() {
														override fun onClick(widget: View) {
															j.data?.url?.let { url ->
																FinestWebView
																	.Builder(this@MythbusterDetailActivity)
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
							val textView = TextView(this).apply {
								layoutParams = params
								typeface = ResourcesCompat.getFont(
									this@MythbusterDetailActivity,
									R.font.montserrat
								)
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
						RichContentType.LIST_ITEM -> content.text?.let { contentText ->
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
							parentParams.setMargins(
								sizeInDp(8, resources),
								sizeInDp(8, resources),
								sizeInDp(8, resources),
								sizeInDp(2, resources)
							)
							rowTextParams.weight = 1f
							val spannableString = SpannableString(contentText).apply {
								content.spans?.let { spans ->
									for (j in spans) {
										j.end?.let { end ->
											val endSpan =
												if (end > content.text.length) content.text.length else end
											when (identifyRichSpanType(j.type)) {
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
														coloredSpan(Color.BLUE), j.start, endSpan,
														exclusiveSpanRule()
													)
													setSpan(object : ClickableSpan() {
														override fun onClick(widget: View) {
															j.data?.url?.let { url ->
																FinestWebView
																	.Builder(this@MythbusterDetailActivity)
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
							val numberTextView = TextView(this).apply {
								layoutParams = rowNumberParams
								typeface = ResourcesCompat.getFont(
									this@MythbusterDetailActivity,
									R.font.montserrat
								)
								includeFontPadding = false
								text = resources.getString(
									R.string.text_post_item_list_number,
									listItemCounter++
								)
								textAlignment = View.TEXT_ALIGNMENT_TEXT_START
								setTextColor(Color.BLACK)
								setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
							}
							val textView = TextView(this).apply {
								layoutParams = rowTextParams
								typeface = ResourcesCompat.getFont(
									this@MythbusterDetailActivity,
									R.font.montserrat
								)
								includeFontPadding = true
								movementMethod = LinkMovementMethod.getInstance()
								text = spannableString.trim()
								textAlignment = View.TEXT_ALIGNMENT_TEXT_START
								setTextColor(Color.BLACK)
								setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
									justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
							}
							val tableLayout = TableLayout(this).apply {
								layoutParams = parentParams
							}
							val tableRow = TableRow(this).apply {
								layoutParams = tableParams
							}
							tableRow.addView(numberTextView)
							tableRow.addView(textView)
							tableLayout.addView(tableRow)
							content_layout.addView(tableLayout)
						}
						RichContentType.O_LIST_ITEM -> content.text?.let { contentText ->
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
							parentParams.setMargins(
								sizeInDp(8, resources),
								sizeInDp(8, resources),
								sizeInDp(8, resources),
								sizeInDp(2, resources)
							)
							rowTextParams.weight = 1f
							val spannableString = SpannableString(contentText).apply {
								content.spans?.let { spans ->
									for (j in spans) {
										j.end?.let { end ->
											val endSpan =
												if (end > content.text.length) content.text.length else end
											when (identifyRichSpanType(j.type)) {
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
														coloredSpan(Color.BLUE), j.start, endSpan,
														exclusiveSpanRule()
													)
													setSpan(object : ClickableSpan() {
														override fun onClick(widget: View) {
															j.data?.url?.let { url ->
																FinestWebView
																	.Builder(this@MythbusterDetailActivity)
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
							val bulletTextView = TextView(this).apply {
								layoutParams = rowBulletParams
								typeface = ResourcesCompat.getFont(
									this@MythbusterDetailActivity,
									R.font.montserrat_bold
								)
								includeFontPadding = false
								text = resources.getString(R.string.text_post_o_item_list_bullet)
								textAlignment = View.TEXT_ALIGNMENT_TEXT_START
								setTextColor(Color.BLACK)
								setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
							}
							val textView = TextView(this).apply {
								layoutParams = rowTextParams
								typeface = ResourcesCompat.getFont(
									this@MythbusterDetailActivity,
									R.font.montserrat
								)
								includeFontPadding = true
								movementMethod = LinkMovementMethod.getInstance()
								text = spannableString.trim()
								textAlignment = View.TEXT_ALIGNMENT_TEXT_START
								setTextColor(Color.BLACK)
								setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
									justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
							}
							val tableLayout = TableLayout(this).apply {
								layoutParams = parentParams
							}
							val tableRow = TableRow(this).apply {
								layoutParams = tableParams
							}
							tableRow.addView(bulletTextView)
							tableRow.addView(textView)
							tableLayout.addView(tableRow)
							content_layout.addView(tableLayout)
						}
						RichContentType.IMAGE -> content.url?.let { url ->
							listItemCounter = 1
							val params = FrameLayout.LayoutParams(
								FrameLayout.LayoutParams.MATCH_PARENT,
								FrameLayout.LayoutParams.WRAP_CONTENT
							)
							params.setMargins(
								sizeInDp(8, resources),
								sizeInDp(8, resources),
								sizeInDp(8, resources),
								sizeInDp(8, resources)
							)
							val card = MaterialCardView(this).apply {
								layoutParams = params
								isClickable = true
								isFocusable = true
							}
							val image = ImageView(this).apply {
								layoutParams = FrameLayout.LayoutParams(
									FrameLayout.LayoutParams.MATCH_PARENT,
									sizeInDp(200, resources)
								)
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
												this@MythbusterDetailActivity,
												arrayOf(resource)
											) { view, image ->
												Glide.with(this@MythbusterDetailActivity)
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
						else -> error { "can't recognize content type: $content" }
					}
				}
			}
			hideLoading()
		}
	}

	private fun showLoading() {
		content_layout.visibility = View.GONE
		text_timestamp.visibility = View.GONE
		loading.visibility = View.VISIBLE
	}

	private fun hideLoading() {
		loading.visibility = View.GONE
		content_layout.visibility = View.VISIBLE
		text_timestamp.visibility = View.VISIBLE
	}
}
