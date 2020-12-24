package com.classic.assistant.car.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.classic.assistant.car.databinding.FragmentListBinding
import com.classic.assistant.car.extension.applyLinearConfig
import com.classic.assistant.car.extension.observe
import com.classic.assistant.car.extension.uiTask
import com.classic.assistant.car.ui.base.RefreshFragment
import com.classic.assistant.car.ui.timeline.TimelineViewModel

class ListFragment : RefreshFragment<FragmentListBinding>() {
    private val vm: TimelineViewModel by activityViewModels()
    override fun targetRefreshLayout(): SwipeRefreshLayout? {
        return viewBinding?.refreshLayout
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmentListBinding.inflate(inflater, container, false).also {
            // it.model = vm
            it.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listAdapter = ListAdapter()
        // viewBinding?.listRecyclerView?.applyLinearConfig(adapter = listAdapter)
        viewBinding?.fastScrollView?.applyLinearConfig(adapter = listAdapter)

        observe(vm.all, {
            listAdapter.submitList(it)
            uiTask(2000) { finishRefresh() }
        })
        vm.subscribe()
        onRefresh()
    }

    override fun onDestroyView() {
        vm.unsubscribe()
        super.onDestroyView()
    }

    override fun onRefresh() {
        vm.queryAll()
    }
}
