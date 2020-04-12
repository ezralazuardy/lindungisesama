package com.lindungisesama.ui.main.fragment.about.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lindungisesama.R
import com.lindungisesama.model.raw.important_links.Result
import kotlinx.android.synthetic.main.item_list_important_links_2.view.*

class ImportantLinksDetailAdapter(
	private val context: Context,
	private val importantLinksItemClickListener: ImportantLinksItemClickListener
) : RecyclerView.Adapter<ImportantLinksDetailAdapter.ViewHolder>() {

	private var data: ArrayList<Result> = ArrayList()

	fun setData(data: List<Result>) {
		this.data.clear()
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
		ViewHolder(
			LayoutInflater.from(context)
				.inflate(R.layout.item_list_important_links_2, parent, false)
		)

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) =
		holder.bind(data[position])

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		fun bind(result: Result) {
			result.data?.let { data ->
				data.name?.let { name -> itemView.text_name.text = name.trim() }
				itemView.frame.setOnClickListener {
					importantLinksItemClickListener.onImportantLinkItemClick(result)
				}
			}
		}
	}
}