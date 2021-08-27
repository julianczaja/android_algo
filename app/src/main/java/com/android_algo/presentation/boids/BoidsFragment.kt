package com.android_algo.presentation.boids

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android_algo.databinding.BoidsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoidsFragment : Fragment() {

    private var _binding: BoidsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BoidsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BoidsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.surfaceView.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}