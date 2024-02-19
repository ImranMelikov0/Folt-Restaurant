package com.imranmelikov.folt.presentation.venue

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.databinding.FragmentVenueBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueCategory
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.VenueCategoryConstants
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("DEPRECATION")
class VenueFragment : Fragment() {
  private lateinit var binding:FragmentVenueBinding
  private lateinit var venueAdapter: VenueAdapter
  private lateinit var venueList: List<Venue>
  private lateinit var viewModel: VenueViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentVenueBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[VenueViewModel::class.java]
        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        initialiseVenueRv()
        observeFavVenues()
        getControlArguments()
        observeCRUD()
        clickBackBtn()
    }

    private fun clickBackBtn(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getControlArguments(){

        val receivedVenueList = arguments?.getSerializable(VenueConstants.venues) as? ArrayList<*>
        venueList = receivedVenueList?.filterIsInstance<Venue>() ?: emptyList()
        venueAdapter.venueList=venueList
       venueAdapter.viewType=VenueCategoryConstants.Venue

        val receiveDiscoveryTitleString=arguments?.getString(VenueCategoryConstants.DiscoveryTitle)

        if (receiveDiscoveryTitleString!=null){
            binding.fastestDelivery.text=receiveDiscoveryTitleString
            binding.toolbarVenueText.text=receiveDiscoveryTitleString
        }

        val receivedVenueCategory = arguments?.getSerializable(VenueCategoryConstants.venueCategories) as? VenueCategory

        if (receivedVenueCategory!=null){
                binding.fastestDelivery.text=receivedVenueCategory.title
                binding.toolbarVenueText.text=receivedVenueCategory.title
        }
    }
    private fun observeFavVenues(){
        viewModel.favoriteVenueLiveData.observe(viewLifecycleOwner) {result->
            when(result.status){
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.venueProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
                Status.SUCCESS->{
                    result.data?.let {venues ->
                        venueAdapter.favVenueList=venues
                    }
                    binding.venueProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.GONE
                }
                Status.LOADING->{
                    binding.venueProgress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
            }

        }

    }
    private fun observeCRUD(){
        viewModel.msgLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {
                        Log.d(it.message,it.success.toString())
                    }
                }
                Status.LOADING->{}
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun initialiseVenueRv(){
        venueAdapter= VenueAdapter()
        venueAdapter.viewModel=viewModel
        binding.venuesRv.layoutManager=LinearLayoutManager(requireContext())
        binding.venuesRv.adapter=venueAdapter
    }
}