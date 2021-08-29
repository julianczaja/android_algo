package com.android_algo.presentation.boids

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android_algo.databinding.BoidsFragmentBinding
import com.google.android.material.slider.Slider
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

        binding.bottomSheetContent.boidOptions.sliderSeparation.addOnChangeListener { _, value, _ ->
            binding.boidsSimulationView.setSeparationGain(value)
        }

        binding.bottomSheetContent.boidOptions.sliderAlignment.addOnChangeListener { _, value, _ ->
            binding.boidsSimulationView.setAlignmentGain(value)
        }

        binding.bottomSheetContent.boidOptions.sliderCohesion.addOnChangeListener { _, value, _ ->
            binding.boidsSimulationView.setCohesionGain(value)
        }

        binding.bottomSheetContent.boidOptions.sliderSight.addOnChangeListener { _, value, _ ->
            binding.boidsSimulationView.setSightRange(value)
        }

//        binding.bottomSheetContent.boidOptions.sliderBoidsCount.addOnChangeListener { _, value, _ ->
//            binding.boidsSimulationView.setBoidsCount(value.toInt())
//        }


        binding.bottomSheetContent.boidOptions.sliderBoidsCount.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                binding.boidsSimulationView.setBoidsCount(slider.value.toInt())
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}