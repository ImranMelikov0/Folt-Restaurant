package com.imranmelikov.folt.presentation.restaurants

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentRestaurantBinding

class RestaurantFragment : Fragment() {
    private lateinit var binding:FragmentRestaurantBinding
    private lateinit var viewModel:RestaurantViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRestaurantBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[RestaurantViewModel::class.java]
        viewModel.getVenues()
        initialiseRestaurantRv()
        observeVenues()
        return binding.root
    }

    private fun initialiseRestaurantRv(){
        restaurantAdapter= RestaurantAdapter()
        binding.restaurantRv.layoutManager=LinearLayoutManager(requireContext())
        binding.restaurantRv.adapter=restaurantAdapter
    }

    private fun observeVenues(){
        viewModel.venueLiveData.observe(viewLifecycleOwner, Observer {
            restaurantAdapter.venueList=it
        })
    }
}