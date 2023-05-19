package com.vitaly.dogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vitaly.dogs.databinding.FragmentHomeBinding
import org.json.JSONArray

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val viewModel = ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java]
        viewModel.loadRandomDogs()

        val adapter = ViewPagerAdapter(requireContext(), JSONArray())
        binding.viewPager2.adapter = adapter

        viewModel.requestData().observe(requireActivity()){
            if (it.isFailed) Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_LONG).show()
            else if (it.images.length() > 0) {
                adapter.updateData(it.images)
                binding.progressBar.visibility = View.GONE
            }
        }

        return binding.root
    }

}