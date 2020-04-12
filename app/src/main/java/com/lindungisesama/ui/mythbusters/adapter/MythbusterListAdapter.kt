package com.lindungisesama.ui.mythbusters.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lindungisesama.R
import com.lindungisesama.model.raw.mythbusters.Result
import com.lindungisesama.ui.main.fragment.home.adapter.MythbustersItemClickListener
import com.lindungisesama.utils.PostContentHelper.identifyRichContentType
import com.lindungisesama.utils.RichContentType
import kotlinx.android.synthetic.main.item_list_mythbusters.view.*

class MythbusterListAdapter(
	private val context: Context,
	private val mythbustersItemClickListener: MythbustersItemClickListener
) : RecyclerView.Adapter<MythbusterListAdapter.ViewHolder>() {

	private var data: ArrayList<Result> = ArrayList()

	fun setData(data: List<Result>) {
		this.data.clear()
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
		ViewHolder(
			LayoutInflater.from(context).inflate(R.layout.item_list_mythbusters, parent, false)
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
						.transition(DrawableTransitionOptions.withCrossFade())
						.centerCrop()
						.error(R.color.colorPrimary)
						.into(itemView.image_cover)
				}
				data.title?.let { title ->
					itemView.text_title.text = title.capitalize().trim()
				}
				data.created_by?.let { creator ->
					itemView.text_creator.text = context.resources
						.getString(R.string.text_post_creator, creator.capitalize().trim())
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
					mythbustersItemClickListener.onMythbustersItemClick(result)
				}
			}
		}
	}
}