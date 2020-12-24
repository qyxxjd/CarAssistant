package com.classic.assistant.car.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.classic.assistant.car.R
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.databinding.FragmentHomeBinding
import com.classic.assistant.car.extension.applyLinearConfig
import com.classic.assistant.car.extension.observe
import com.classic.assistant.car.extension.uiTask
import com.classic.assistant.car.ui.add.TextChooseFragment
import com.classic.assistant.car.ui.add.TextChooseListener
import com.classic.assistant.car.ui.base.RefreshFragment
import com.classic.assistant.car.ui.main.MainActivity
import com.classic.assistant.car.util.CarLog
import com.classic.assistant.car.util.ChooseCallback
import com.classic.assistant.car.util.DatePickerUtil

class HomeFragment : RefreshFragment<FragmentHomeBinding>() {
    private val vm: HomeViewModel by activityViewModels()
    // private var fabOffset: Int = 0
    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false).also {
            // it.model = vm
            it.lifecycleOwner = viewLifecycleOwner
        }
    }
    override fun targetRefreshLayout(): SwipeRefreshLayout? {
        return viewBinding?.refreshLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val homeAdapter = HomeAdapter()
        viewBinding?.apply {
            // homeFab.setOnClickListener { AddActivity.start(requireActivity()) }
            // homeRecyclerView.applyLinearConfig(adapter = homeAdapter)
            fastScrollView.applyLinearConfig(adapter = homeAdapter)
        }
        observe(vm.consumptionList, {
            homeAdapter.submitList(it)
            uiTask(2000) { finishRefresh() }
        })
        vm.subscribe()
        onRefresh()
    }

    override fun onDestroyView() {
        vm.unsubscribe()
        super.onDestroyView()
    }

    // override fun onResume() {
    //     super.onResume()
    //     viewBinding?.homeRecyclerView?.addOnScrollListener(scrollControl)
    // }
    //
    // override fun onPause() {
    //     super.onPause()
    //     viewBinding?.homeRecyclerView?.removeOnScrollListener(scrollControl)
    // }

    override fun onRefresh() {
        vm.loadData()
        updateFilterHint()
    }

    private var filterMenu: MenuItem? = null
    private fun updateFilterHint() {
        filterMenu?.title = vm.getFilterTitle()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.filter_menu, menu)
        filterMenu = menu.findItem(R.id.action_filter_menu)
        updateFilterHint()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        showFilterMenu()
        return true
    }

    private fun showFilterMenu() {
        val act = requireActivity()
        val view = (act as MainActivity).toolbar
        val popup = HomeFilterMenuPopup(act, vm.filterType, object : HomeFilterMenuPopup.Listener {
            override fun onFilterTypeChange(type: Int) {
                vm.filterType = type
                when (type) {
                    HomeFilterMenuPopup.FILTER_MENU_YEAR -> chooseYear()
                    HomeFilterMenuPopup.FILTER_MENU_MONTH -> chooseMonth()
                    HomeFilterMenuPopup.FILTER_MENU_TYPE -> chooseType()
                }
            }
        })
        popup.showAsDropDown(view, view!!.width - popup.width, 0)
    }
    private fun chooseYear() {
        DatePickerUtil.year(requireActivity(), vm.currentYear, object : ChooseCallback<Int> {
            override fun onChoose(t: Int) {
                vm.currentYear = t
                onRefresh()
            }
        })
    }
    private fun chooseMonth() {
        DatePickerUtil.month(requireActivity(), vm.currentTime) { date, _ ->
            date?.let {
                vm.currentTime = it.time
                onRefresh()
            }
        }
    }
    private fun chooseType() {
        TextChooseFragment()
            .title("类型选择")
            .columnCount(2)
            .items(ConsumptionInfo.TYPE_LABELS)
            .listener(object : TextChooseListener {
                override fun onChoose(index: Int, text: String) {
                    CarLog.d("消费类型选择：index=$index, text=$text")
                    vm.currentType = index
                    onRefresh()
                }
            })
            .show(childFragmentManager)
    }

    // private var isFabVisible = true
    // private val scrollControl = object : RecyclerView.OnScrollListener() {
    //     override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
    //         super.onScrollStateChanged(recyclerView, newState)
    //         // 停止滚动时
    //         if (RecyclerView.SCROLL_STATE_IDLE == newState) {
    //             onShow()
    //         } else if (isFabVisible) {
    //             onHide()
    //         }
    //     }
    // }
    //
    // private fun onHide() {
    //     viewBinding?.homeFab?.let {
    //         if (fabOffset == 0) fabOffset = it.height + it.bottom
    //     }
    //     toggleFab(false)
    // }
    // private fun onShow() {
    //     toggleFab(true)
    // }
    //
    // private fun toggleFab(show: Boolean) {
    //     viewBinding?.homeFab?.let {
    //         if (show) {
    //             it.animate().translationY(0f).setInterpolator(DecelerateInterpolator(2f)).start()
    //         } else {
    //             it.animate().translationY(fabOffset.toFloat()).interpolator = AccelerateInterpolator(2f)
    //         }
    //     }
    //     isFabVisible = show
    // }
}
