package com.lindungisesama.ui.main.fragment.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lindungisesama.R
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.ui.main.MainViewModel
import com.lindungisesama.ui.main.fragment.about.adapter.ImportantLinksAdapter
import com.lindungisesama.ui.main.fragment.about.adapter.ImportantLinksItemClickListener
import com.lindungisesama.ui.main.fragment.about.adapter.ReferencesAdapter
import com.lindungisesama.ui.main.fragment.about.adapter.ReferencesItemClickListener
import com.lindungisesama.utils.ErrorHandler
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.layout_about_app.*
import kotlinx.android.synthetic.main.layout_important_links.*
import kotlinx.android.synthetic.main.layout_references.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class AboutFragment : Fragment(), ReferencesItemClickListener, ImportantLinksItemClickListener {

	private val mainViewModel: MainViewModel by sharedViewModel()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.fragment_about, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		retainInstance = true
		showReferencesLoading()
		showImportantLinksLoading()
		context?.let { context ->
			context.packageManager.getPackageInfo(context.packageName, 0).versionName.let {
				text_version.text = resources.getString(R.string.text_version, it)
			}
			button_official_web.setOnClickListener {
				FinestWebView.Builder(context).show(resources.getString(R.string.app_webpage))
			}
			val importantLinksAdapter = ImportantLinksAdapter(context, this)
			val referencesAdapter = ReferencesAdapter(context, this)
			recycler_view_important_links.apply {
				adapter = importantLinksAdapter
				layoutManager = LinearLayoutManager(context)
				setHasFixedSize(true)
			}
			recycler_view_references.apply {
				adapter = referencesAdapter
				layoutManager = LinearLayoutManager(context)
				setHasFixedSize(true)
			}
			button_references_detail.setOnClickListener {
				activity?.let { activity ->
					ReferencesDetailFragment()
						.show(
							activity.supportFragmentManager,
							ReferencesDetailFragment::javaClass.name
						)
				}
			}
			button_important_links_detail.setOnClickListener {
				activity?.let { activity ->
					ImportantLinksDetailFragment()
						.show(
							activity.supportFragmentManager,
							ImportantLinksDetailFragment::javaClass.name
						)
				}
			}
			mainViewModel.importantLinks.observe(viewLifecycleOwner, Observer {
				when (it.status) {
					RetrofitStatus.SUCCESS -> it.data?.let { data ->
						importantLinksAdapter.setData(data)
						hideImportantLinksLoading()
					}
					else -> activity?.let { activity ->
						ErrorHandler.show(activity.supportFragmentManager, it.status)
					}
				}
				if (importantLinksAdapter.getRawDataCount() > 3) showImportantLinksDetailButton()
				if (!mainViewModel.referencesIsExecuted
					&& !mainViewModel.referencesLoaded
				) mainViewModel.getReferences()
			})
			mainViewModel.references.observe(viewLifecycleOwner, Observer {
				when (it.status) {
					RetrofitStatus.SUCCESS -> it.data?.let { data ->
						referencesAdapter.setData(data)
						hideReferencesLoading()
					}
					else -> activity?.let { activity ->
						ErrorHandler.show(activity.supportFragmentManager, it.status)
					}
				}
				if (referencesAdapter.getRawDataCount() > 3) showReferencesDetailButton()
			})
			mainViewModel.prismicResponse.observe(viewLifecycleOwner, Observer {
				if (it.status != RetrofitStatus.SUCCESS) activity?.let { activity ->
					ErrorHandler.show(activity.supportFragmentManager, it.status)
				} else if (!mainViewModel.importantLinksIsExecuted
					&& !mainViewModel.importantLinksLoaded
				) mainViewModel.getImportantLinks()
			})
		}
	}

	private fun showReferencesLoading() {
		recycler_view_references.visibility = View.GONE
		button_references_detail.visibility = View.GONE
		loading_references.visibility = View.VISIBLE
	}

	private fun hideReferencesLoading() {
		loading_references.visibility = View.GONE
		recycler_view_references.visibility = View.VISIBLE
	}

	private fun showReferencesDetailButton() {
		button_references_detail.visibility = View.VISIBLE
	}

	private fun showImportantLinksLoading() {
		recycler_view_important_links.visibility = View.GONE
		button_important_links_detail.visibility = View.GONE
		loading_important_links.visibility = View.VISIBLE
	}

	private fun hideImportantLinksLoading() {
		loading_important_links.visibility = View.GONE
		recycler_view_important_links.visibility = View.VISIBLE
	}

	private fun showImportantLinksDetailButton() {
		button_important_links_detail.visibility = View.VISIBLE
	}

	override fun onReferenceItemClick(result: com.lindungisesama.model.raw.references.Result) {
		context?.let { context ->
			result.data?.link?.let { link ->
				if (link.isNotEmpty()) FinestWebView.Builder(context).show(link)
			}
		}
	}

	override fun onImportantLinkItemClick(result: com.lindungisesama.model.raw.important_links.Result) {
		context?.let { context ->
			result.data?.link?.let { link ->
				if (link.isNotEmpty()) FinestWebView.Builder(context).show(link)
			}
		}
	}
}
