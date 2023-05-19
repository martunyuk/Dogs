package com.vitaly.dogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.vitaly.dogs.databinding.FragmentSearchBinding
import org.json.JSONArray

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private var itemsArray: Array<String> = arrayOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        binding.spinner.visibility = View.GONE

        val viewModel = ViewModelProvider(requireActivity())[SearchFragmentViewModel::class.java]
        viewModel.loadAllBreeds()
        viewModel.requestBreedsData().observe(requireActivity()){
        if (it.isFailed) Toast.makeText(requireActivity(), "Failed to load data", Toast.LENGTH_LONG).show()
        else if (it.breeds.isNotEmpty()) {
                binding.spinner.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE

                if(binding.spinner.adapter == null){
                    itemsArray = it.breeds.toTypedArray()
                    val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(requireActivity(), R.layout.spinner_item, itemsArray)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinner.adapter = spinnerAdapter
                    binding.spinner.setSelection(viewModel.lastSelectedBreedId)
                    binding.spinner.onItemSelectedListener = object : OnItemSelectedListener{
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            viewModel.lastSelectedBreedId = p2
                            val selectedBreed = itemsArray[p2]
                            viewModel.loadDogsByBreed(selectedBreed)
                            val viewPagerAdapter = ViewPagerAdapter(requireContext(), JSONArray())
                            binding.viewPager2.adapter = viewPagerAdapter
                            viewModel.requestDogsByBreed().observe(requireActivity()){ requestData ->
                                if(requestData.isFailed) Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_LONG).show()
                                else if (requestData.images.length() > 0){
                                    viewPagerAdapter.updateData(requestData.images)
                                }
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {}

                    }
                }
            }
        }

        return binding.root
    }

}