package com.imranmelikov.folt.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentProfileBinding
import com.imranmelikov.folt.domain.model.DiscoveryItem
import com.imranmelikov.folt.presentation.discovery.DiscoveryAdapter
import com.imranmelikov.folt.constants.DiscoveryTitles
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.constants.ViewTypeDiscovery
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.presentation.venue.VenueViewModel
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        viewModelRestaurant.getFavoriteVenues("a")
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
        viewModelRestaurant.favoriteVenueLiveData.observe(viewLifecycleOwner) {result->
            when(result.status){
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.profileProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
                Status.SUCCESS->{
                    result.data?.let {venues ->
                        val discoveryItemRestaurant=DiscoveryItem(DiscoveryTitles.yourFav,ViewTypeDiscovery.Profile,venues)
                        val discoveryItemList= listOf(discoveryItemRestaurant)
                        discoveryAdapter.discoveryItemList=discoveryItemList
                    }
                    binding.profileProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.GONE
                }
                Status.LOADING->{
                    binding.profileProgress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
            }

        }

    }
    private fun initialiseRv(){
        discoveryAdapter= DiscoveryAdapter()
        binding.profileRv.layoutManager=LinearLayoutManager(requireContext())
        binding.profileRv.adapter=discoveryAdapter
    }
}