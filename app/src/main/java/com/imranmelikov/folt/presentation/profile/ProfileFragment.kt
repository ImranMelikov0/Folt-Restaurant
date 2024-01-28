package com.imranmelikov.folt.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentProfileBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.presentation.restaurants.RestaurantViewModel
import com.imranmelikov.folt.presentation.stores.StoreViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var viewModelRestaurant: RestaurantViewModel
    private lateinit var storeViewModel: StoreViewModel
    private lateinit var profileAdapter: ProfileAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        viewModelRestaurant= ViewModelProvider(requireActivity())[RestaurantViewModel::class.java]
        storeViewModel= ViewModelProvider(requireActivity())[StoreViewModel::class.java]

        binding.accountLinear.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_accountFragment)
        }

        initialiseRv()
        viewModelRestaurant.getVenues()
        storeViewModel.getVenues()
        observeVenues()
        return binding.root
    }

    private fun observeVenues(){
        val combinedList: MutableList<Venue> = mutableListOf()
        viewModelRestaurant.venueLiveData.observe(viewLifecycleOwner) {restaurantVenues->
            storeViewModel.venueLiveData.observe(viewLifecycleOwner) {storeVenues->
                combinedList.addAll(restaurantVenues)
                combinedList.addAll(storeVenues)
                val filteredFavVenues=combinedList.filter { it.venuePopularity.favorite }
                profileAdapter.venueList=filteredFavVenues
            }
        }

    }
    private fun initialiseRv(){
        profileAdapter= ProfileAdapter()
        binding.profileRv.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.profileRv.adapter=profileAdapter
    }
}