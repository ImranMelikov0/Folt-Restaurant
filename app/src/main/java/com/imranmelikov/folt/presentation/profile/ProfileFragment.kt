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
import com.imranmelikov.folt.domain.model.DiscoveryItem
import com.imranmelikov.folt.presentation.discovery.DiscoveryAdapter
import com.imranmelikov.folt.presentation.restaurants.RestaurantViewModel
import com.imranmelikov.folt.presentation.stores.StoreViewModel
import com.imranmelikov.folt.constants.DiscoveryTitles
import com.imranmelikov.folt.constants.ViewTypeDiscovery

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var viewModelRestaurant: RestaurantViewModel
    private lateinit var storeViewModel: StoreViewModel
    private lateinit var discoveryAdapter: DiscoveryAdapter
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
        viewModelRestaurant.venueLiveData.observe(viewLifecycleOwner) {restaurantVenues->
            storeViewModel.venueLiveData.observe(viewLifecycleOwner) {storeVenues->
                val filteredFavRestaurants=restaurantVenues.filter { it.venuePopularity.favorite }
                val filteredFavStores=storeVenues.filter { it.venuePopularity.favorite }
                val discoveryItemRestaurant=DiscoveryItem(DiscoveryTitles.yourFavRestaurants,ViewTypeDiscovery.ProfileRestaurant,filteredFavRestaurants)
                val discoveryItemStore=DiscoveryItem(DiscoveryTitles.yourFavStores,ViewTypeDiscovery.ProfileStore,filteredFavStores)
                val discoveryItemList= listOf(discoveryItemRestaurant,discoveryItemStore)
                discoveryAdapter.discoveryItemList=discoveryItemList
            }
        }

    }
    private fun initialiseRv(){
        discoveryAdapter= DiscoveryAdapter()
        binding.profileRv.layoutManager=LinearLayoutManager(requireContext())
        binding.profileRv.adapter=discoveryAdapter
    }
}