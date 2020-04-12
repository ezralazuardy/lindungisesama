package com.lindungisesama.ui.main.fragment.faq.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lindungisesama.R
import com.lindungisesama.model.raw.faq.Result
import kotlinx.android.synthetic.main.item_list_faq.view.*

class FAQAdapter(
	private val context: Context,
	private val faqItemClickListener: FAQItemClickListener
) : RecyclerView.Adapter<FAQAdapter.ViewHolder>() {

	private var data: ArrayList<Result> = ArrayList()

	fun setData(data: List<Result>) {
		this.data.clear()
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
		ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_faq, parent, false))

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) =
		holder.bind(data[position])

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		@SuppressLint("DefaultLocale")
		fun bind(result: Result) {
			result.data?.let { data ->
				data.question?.let { question ->
					itemView.txt_question.text = question.capitalize().trim()
				}
				itemView.card.setOnClickListener {
					faqItemClickListener.onItemClick(result)
				}
			}
		}
	}
}