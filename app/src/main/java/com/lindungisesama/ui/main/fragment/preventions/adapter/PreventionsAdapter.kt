package com.lindungisesama.ui.main.fragment.preventions.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.lindungisesama.R
import com.lindungisesama.model.raw.preventions.Result
import com.lindungisesama.utils.PostContentHelper.identifyRichContentType
import com.lindungisesama.utils.RichContentType
import kotlinx.android.synthetic.main.item_list_preventions.view.*

class PreventionsAdapter(
	private val context: Context,
	private val preventionsItemClickListener: PreventionsItemClickListener
) : RecyclerView.Adapter<PreventionsAdapter.ViewHolder>() {

	private var data: ArrayList<Result> = ArrayList()

	fun setData(data: List<Result>) {
		this.data.clear()
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
		ViewHolder(
			LayoutInflater.from(context).inflate(R.layout.item_list_preventions, parent, false)
		)

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) =
		holder.bind(data[position])

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		@SuppressLint("DefaultLocale")
		fun bind(result: Result) {
			result.data?.let { data ->
				data.image?.url?.let { url ->
					Glide.with(context)
						.load(url)
						.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
						.transition(withCrossFade())
						.centerCrop()
						.error(R.color.colorPrimary)
						.into(itemView.image_cover)
				}
				data.title?.let { title ->
					itemView.text_title.text = title.capitalize().trim()
				}
				data.content?.let { content ->
					content.firstOrNull {
						identifyRichContentType(it.type) == RichContentType.PARAGRAPH
					}.let { paragraph ->
						if (paragraph?.text != null)
							itemView.text_content.text = paragraph.text.capitalize().trim()
						else content.firstOrNull {
							identifyRichContentType(it.type) == RichContentType.LIST_ITEM
						}.let { list_item ->
							if (list_item?.text != null)
								itemView.text_content.text = list_item.text.capitalize().trim()
							else content.firstOrNull {
								identifyRichContentType(it.type) == RichContentType.O_LIST_ITEM
							}.let { o_list_item ->
								if (o_list_item?.text != null)
									itemView.text_content.text =
										o_list_item.text.capitalize().trim()
								else itemView.text_content.visibility = View.GONE
							}
						}
					}
				}
				itemView.button_read_more.setOnClickListener {
					preventionsItemClickListener.onItemClick(result)
				}
			}
		}
	}
}