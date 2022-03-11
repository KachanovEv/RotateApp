package com.mobile.rotationapp.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ProcessLifecycleOwner
import com.mobile.rotationapp.R
import com.mobile.rotationapp.databinding.FragmentHomeBinding
import com.mobile.rotationapp.utils.helper.SessionsHelper
import org.koin.android.ext.android.inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val sh: SessionsHelper by inject()
    private val vm: HomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        ProcessLifecycleOwner.get().lifecycle.addObserver(sh)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setContent()
        observeDegrees()
        observeSensors()
    }

    private fun setContent() {
        binding.tvSessionCount.text = getString(R.string.sessionCount, vm.sessionsCount)
        binding.tvSessionCount.textSize = 16F
        sh.sessionsLiveData.observe(viewLifecycleOwner, { sessionCount ->
            binding.tvSessionCount.text = getString(R.string.sessionCount, sessionCount)
        })
    }

    private fun observeDegrees() {
        vm.azimuthLiveData.observe(viewLifecycleOwner, { azimuth ->
            Log.d("azimuth ", azimuth.toString())
            when {
                azimuth in 31..75 -> {
                    binding.tvSessionCount.textSize = 16F
                }
                azimuth < 30 -> {
                    binding.tvSessionCount.textSize = 12F
                }
                azimuth in 76..300 -> {
                    binding.tvSessionCount.textSize = 20F
                }
            }
        })

        vm.rollLiveData.observe(viewLifecycleOwner, { roll ->
            Log.d("roll ", roll.toString())
        })

        vm.pitchLiveData.observe(viewLifecycleOwner, { pitch ->
            Log.d("pitch ", pitch.toString())
        })
    }

    private fun observeSensors() {
        vm.sensorsUnavailableData.observe(viewLifecycleOwner, { isAvailable ->
            if (!isAvailable) {
                Toast.makeText(
                    requireContext(),
                    R.string.toast_sensors_unavailable,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }
}
