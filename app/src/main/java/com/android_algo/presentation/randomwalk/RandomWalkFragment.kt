package com.android_algo.presentation.randomwalk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android_algo.databinding.RandomWalkFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomWalkFragment : Fragment() {

    private var _binding: RandomWalkFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RandomWalkViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RandomWalkFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}