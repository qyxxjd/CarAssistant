package com.classic.assistant.car.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.classic.assistant.car.databinding.FragmentSettingsBinding
import com.classic.assistant.car.ui.base.AppViewBindingFragment

class SettingsFragment : AppViewBindingFragment<FragmentSettingsBinding>() {
    private val vm: SettingsViewModel by activityViewModels()
    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewBinding = FragmentSettingsBinding.inflate(inflater, container, false).also {
            it.model = vm
            it.lifecycleOwner = viewLifecycleOwner
        }
    }
}
