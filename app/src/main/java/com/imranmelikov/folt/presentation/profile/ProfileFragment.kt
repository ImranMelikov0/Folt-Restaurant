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
import com.imranmelikov.folt.constants.DiscoveryTitles
import com.imranmelikov.folt.constants.ViewTypeDiscovery
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.presentation.venue.VenueViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var viewModelRestaurant: VenueViewModel
    private lateinit var discoveryAdapter: DiscoveryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        viewModelRestaurant= ViewModelProvider(requireActivity())[VenueViewModel::class.java]


        clickToFragments()
        initialiseRv()
        viewModelRestaurant.getVenues()
        observeVenues()
        return binding.root
    }
    private fun clickToFragments(){
        binding.accountLinear.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_accountFragment)
            (activity as MainActivity).hideBottomNav()
        }
        binding.profileLinear.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_accountFragment)
            (activity as MainActivity).hideBottomNav()
        }
        binding.myAddressLinear.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addressFragment)
            (activity as MainActivity).hideBottomNav()
        }
        binding.orderHistoryLinear.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_orderHistoryFragment)
            (activity as MainActivity).hideBottomNav()
        }
    }

    private fun observeVenues(){
        viewModelRestaurant.venueLiveData.observe(viewLifecycleOwner) {venues->
                val filteredFavVenues=venues.filter { it.venuePopularity.favorite }
                val discoveryItemRestaurant=DiscoveryItem(DiscoveryTitles.yourFav,ViewTypeDiscovery.Profile,filteredFavVenues)
                val discoveryItemList= listOf(discoveryItemRestaurant)
                discoveryAdapter.discoveryItemList=discoveryItemList
        }

    }
    private fun initialiseRv(){
        discoveryAdapter= DiscoveryAdapter()
        binding.profileRv.layoutManager=LinearLayoutManager(requireContext())
        binding.profileRv.adapter=discoveryAdapter
    }
}