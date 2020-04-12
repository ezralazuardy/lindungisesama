package com.lindungisesama.ui.main.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lindungisesama.R
import com.lindungisesama.config.AppConfig.DEFAULT_TIMESTAMP_FORMAT
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.ui.guides.GuideDetailActivity
import com.lindungisesama.ui.guides.GuideDetailActivity.Companion.GUIDE_DETAIL_DATA
import com.lindungisesama.ui.guides.GuideListActivity
import com.lindungisesama.ui.main.MainViewModel
import com.lindungisesama.ui.main.fragment.home.adapter.GuidesAdapter
import com.lindungisesama.ui.main.fragment.home.adapter.GuidesItemClickListener
import com.lindungisesama.ui.main.fragment.home.adapter.MythbustersAdapter
import com.lindungisesama.ui.main.fragment.home.adapter.MythbustersItemClickListener
import com.lindungisesama.ui.mythbusters.MythbusterDetailActivity
import com.lindungisesama.ui.mythbusters.MythbusterDetailActivity.Companion.MYTHBUSTER_DETAIL_DATA
import com.lindungisesama.ui.mythbusters.MythbusterListActivity
import com.lindungisesama.utils.ErrorHandler
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.layout_covid_current_cases.*
import kotlinx.android.synthetic.main.layout_current_updates.*
import kotlinx.android.synthetic.main.layout_guides.*
import kotlinx.android.synthetic.main.layout_mythbusters.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), GuidesItemClickListener, MythbustersItemClickListener {

	private val mainViewModel: MainViewModel by sharedViewModel()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.fragment_home, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		retainInstance = true
		showLoadingCurrentCovidCase()
		showGuidesLoading()
		showMythbustersLoading()
		context?.let { context ->
			card_current_update_1.setOnClickListener {
				FinestWebView.Builder(context)
					.show(resources.getString(R.string.link_current_update_1))
			}
			card_current_update_2.setOnClickListener {
				FinestWebView.Builder(context)
					.show(resources.getString(R.string.link_current_update_2))
			}
			card_current_update_3.setOnClickListener {
				FinestWebView.Builder(context)
					.show(resources.getString(R.string.link_current_update_3))
			}
			card_current_update_4.setOnClickListener {
				FinestWebView.Builder(context)
					.show(resources.getString(R.string.link_current_update_4))
			}
			card_current_update_5.setOnClickListener {
				FinestWebView.Builder(context)
					.show(resources.getString(R.string.link_current_update_5))
			}
			card_current_update_6.setOnClickListener {
				FinestWebView.Builder(context)
					.show(resources.getString(R.string.link_current_update_6))
			}
			button_guides_list.setOnClickListener {
				startActivity(Intent(context, GuideListActivity::class.java))
			}
			button_mythbusters_list.setOnClickListener {
				startActivity(Intent(context, MythbusterListActivity::class.java))
			}
			val guidesAdapter = GuidesAdapter(context, this@HomeFragment)
			val mythbustersAdapter = MythbustersAdapter(context, this@HomeFragment)
			guides_recycler_view.apply {
				adapter = guidesAdapter
				layoutManager = LinearLayoutManager(context)
				setHasFixedSize(true)
			}
			mythbusters_recycler_view.apply {
				adapter = mythbustersAdapter
				layoutManager = LinearLayoutManager(context)
				setHasFixedSize(true)
			}
			mainViewModel.currentCaseResponse.observe(viewLifecycleOwner, Observer {
				when (it.status) {
					RetrofitStatus.SUCCESS -> it.data?.let { data ->
						txt_covid_current_confirmed.text =
							if (data.cases != null) data.cases.toString() else "-"
						txt_covid_current_recovery.text =
							if (data.active != null) data.active.toString() else "-"
						txt_covid_current_recovered.text =
							if (data.recovered != null) data.recovered.toString() else "-"
						txt_covid_current_death.text =
							if (data.deaths != null) data.deaths.toString() else "-"
						data.updated?.let { timestamp ->
							txt_covid_current_timestamp.text = getString(
								R.string.text_current_cases_refreshed_time,
								convertTimeStampToLocalDate(timestamp)
							)
							txt_covid_current_timestamp.visibility = View.VISIBLE
						}
					}
					else -> {
						setEmptyCurrentCovidCase()
						activity?.let { activity ->
							ErrorHandler.show(activity.supportFragmentManager, it.status)
						}
					}
				}
				hideLoadingCurrentCovidCase()
				if (!mainViewModel.guidesIsExecuted
					&& !mainViewModel.guidesLoaded
				) mainViewModel.getGuides()
			})
			mainViewModel.guides.observe(viewLifecycleOwner, Observer {
				when (it.status) {
					RetrofitStatus.SUCCESS -> it.data?.let { data ->
						guidesAdapter.setData(data)
						hideGuidesLoading()
					}
					else -> activity?.let { activity ->
						ErrorHandler.show(activity.supportFragmentManager, it.status)
					}
				}
				if (!mainViewModel.mythbustersIsExecuted
					&& !mainViewModel.mythbustersLoaded
				) mainViewModel.getMythbusters()
			})
			mainViewModel.mythbusters.observe(viewLifecycleOwner, Observer {
				when (it.status) {
					RetrofitStatus.SUCCESS -> it.data?.let { data ->
						mythbustersAdapter.setData(data)
						hideMythbustersLoading()
					}
					else -> activity?.let { activity ->
						ErrorHandler.show(activity.supportFragmentManager, it.status)
					}
				}
			})
			mainViewModel.prismicResponse.observe(viewLifecycleOwner, Observer {
				if (it.status != RetrofitStatus.SUCCESS) activity?.let { activity ->
					ErrorHandler.show(activity.supportFragmentManager, it.status)
				} else if (!mainViewModel.currentCaseIsExecuted
					&& !mainViewModel.currentCaseLoaded
				) mainViewModel.getCurrentCase()
			})
		}
	}

	private fun showLoadingCurrentCovidCase() {
		txt_covid_current_confirmed.visibility = View.GONE
		loading_covid_current_confirmed.visibility = View.VISIBLE
		txt_covid_current_recovery.visibility = View.GONE
		loading_covid_current_recovery.visibility = View.VISIBLE
		txt_covid_current_recovered.visibility = View.GONE
		loading_covid_current_recovered.visibility = View.VISIBLE
		txt_covid_current_death.visibility = View.GONE
		loading_covid_current_death.visibility = View.VISIBLE
	}

	private fun setEmptyCurrentCovidCase() {
		txt_covid_current_confirmed.text = "-"
		txt_covid_current_recovery.text = "-"
		txt_covid_current_recovered.text = "-"
		txt_covid_current_death.text = "-"
		txt_covid_current_timestamp.visibility = View.GONE
	}

	private fun hideLoadingCurrentCovidCase() {
		loading_covid_current_confirmed.visibility = View.GONE
		txt_covid_current_confirmed.visibility = View.VISIBLE
		loading_covid_current_recovery.visibility = View.GONE
		txt_covid_current_recovery.visibility = View.VISIBLE
		loading_covid_current_recovered.visibility = View.GONE
		txt_covid_current_recovered.visibility = View.VISIBLE
		loading_covid_current_death.visibility = View.GONE
		txt_covid_current_death.visibility = View.VISIBLE
	}

	private fun showGuidesLoading() {
		guides_recycler_view.visibility = View.GONE
		guides_loading.visibility = View.VISIBLE
	}

	private fun hideGuidesLoading() {
		guides_loading.visibility = View.GONE
		guides_recycler_view.visibility = View.VISIBLE
	}

	private fun showMythbustersLoading() {
		mythbusters_recycler_view.visibility = View.GONE
		mythbusters_loading.visibility = View.VISIBLE
	}

	private fun hideMythbustersLoading() {
		mythbusters_loading.visibility = View.GONE
		mythbusters_recycler_view.visibility = View.VISIBLE
	}

	private fun convertTimeStampToLocalDate(timestamp: Long): String =
		SimpleDateFormat(DEFAULT_TIMESTAMP_FORMAT, Locale.getDefault()).format(Date(timestamp))

	override fun onGuidesItemClick(result: com.lindungisesama.model.raw.guides.Result) {
		startActivity(
			Intent(activity, GuideDetailActivity::class.java)
				.putExtra(GUIDE_DETAIL_DATA, result)
		)
	}

	override fun onMythbustersItemClick(result: com.lindungisesama.model.raw.mythbusters.Result) {
		startActivity(
			Intent(activity, MythbusterDetailActivity::class.java)
				.putExtra(MYTHBUSTER_DETAIL_DATA, result)
		)
	}
}
