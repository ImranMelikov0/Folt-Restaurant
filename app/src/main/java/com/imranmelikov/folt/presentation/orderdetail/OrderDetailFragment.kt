package com.imranmelikov.folt.presentation.orderdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.constants.OrderConstants
import com.imranmelikov.folt.data.local.entity.VenueDetailsRoom
import com.imranmelikov.folt.databinding.FragmentOrderDetailBinding
import com.imranmelikov.folt.domain.model.StoreMenuCategory
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.domain.model.VenueDetailsItem
import com.imranmelikov.folt.presentation.venuedetails.VenueDetailsViewModel
import com.imranmelikov.folt.util.Resource
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class OrderDetailFragment : Fragment() {
  private lateinit var binding:FragmentOrderDetailBinding
  private lateinit var viewModel: VenueDetailsViewModel
    private var isStockUpdated = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentOrderDetailBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[VenueDetailsViewModel::class.java]

        viewModel.getVenueDetailsFromRoom()
        viewModel.getVenueMenuList()
        viewModel.getStoreMenuCategoryList()
       observeUser()
        observeCRUD()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    private fun clickCompleteBtn(venue: Venue,venueDetailList: List<VenueDetailsRoom>){
        binding.saveBtn.setOnClickListener {
            if (venue.restaurant){
                observeMenuList(venue,venueDetailList)
            }else{
                observeStoreViewModel(venue,venueDetailList)
            }
        }
    }

    private fun observeVenueDetails(){
        val receivedVenue = arguments?.getSerializable(OrderConstants.venueForOrder) as? Venue
        receivedVenue?.let { venue->
            var itemSubtotalPrice=0.0
            viewModel.venueDetailsLiveData.observe(viewLifecycleOwner){venueDetailList->
                venueDetailList.map { venueDetail->
                    itemSubtotalPrice += venueDetail.price
                }
                updateUIWithVenueDetails(venue,itemSubtotalPrice/2)
                clickCompleteBtn(venue,venueDetailList)
            }
        }
    }
    private fun updateUIWithVenueDetails(venue: Venue, itemSubtotalPrice: Double) {
        binding.servicePriceText.text=venue.serviceFee.toString()

        val formattedSubTotalPrice = String.format("%.2f", itemSubtotalPrice)
        binding.itemSubtotalPriceText.text=formattedSubTotalPrice
        binding.deliveryPriceText.text=venue.delivery.deliveryPrice

        val formattedTotalPrice = String.format("%.2f",itemSubtotalPrice + venue.delivery.deliveryPrice.toDouble()
                + venue.serviceFee.toDouble() )
        binding.totalPriceText.text=formattedTotalPrice

        "${venue.delivery.deliveryTime} min".also { binding.deliveryTimeText.text = it }
        binding.restaurantName.text=venue.venueName
    }
    private fun observeMenuList(venue:Venue,venueDetailList: List<VenueDetailsRoom>){
        viewModel.venueMenuLiveData.observe(viewLifecycleOwner){result->
            handleResult(result){venueDetailsItems->
                val filteredVenueDetailsItems=venueDetailsItems.filter { it.parentId==venue.id }
                updateStock(filteredVenueDetailsItems,venueDetailList)
            }
        }
    }
    private fun observeStoreViewModel(venue:Venue,venueDetailList: List<VenueDetailsRoom>){
        viewModel.storeMenuCategoryLiveData.observe(viewLifecycleOwner){result->
            handleResult(result){venueDetailsItems->
                val filteredStoreMenuCategory=venueDetailsItems.filter { it.parentId==venue.id }
                filteredStoreMenuCategory.map {venueDetail->
                    venueDetail.storeMenuCategory?.let {
                        observeMenuListForCategory(it,venueDetailList)
                    }
            }
            }
        }
    }

    private fun updateStock(filteredVenueDetailsItems: List<VenueDetailsItem>,venueDetailList:List<VenueDetailsRoom>) {
            filteredVenueDetailsItems.map {menu->
                menu.venueDetailList?.map {venueDetail->
                    venueDetailList.map {venueDetailRoom->
                        if (venueDetail.id==venueDetailRoom.id){
                            venueDetail.stock = venueDetail.stock.toInt() - venueDetailRoom.count
                        }
                    }
                }
                menu.id?.let {id->
                    menu.venueDetailList?.let {
                        if (!isStockUpdated){
                            viewModel.updateVenueDetailStock(id,it)
                        }
                        isStockUpdated=true
                    }
                }
            }
    }
    private fun observeMenuListForCategory(storeMenuCategoryList:List<StoreMenuCategory>,venueDetailList: List<VenueDetailsRoom>){
        viewModel.venueMenuLiveData.observe(viewLifecycleOwner){result->
            handleResult(result){venueDetailsItems->
                storeMenuCategoryList.map {storeMenuCategory->
                    val filteredVenueDetailsItems=venueDetailsItems.filter { it.parentId==storeMenuCategory.id }
                    updateStock(filteredVenueDetailsItems,venueDetailList)
                }
            }
        }
    }
    private fun observeCRUD(){
        viewModel.msgStockLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {
                        Log.d(it.message,it.success.toString())
                    }
                    viewModel.deleteAllVenueDetailsFromRoom()
                    binding.progress.visibility=View.GONE
                }
                Status.LOADING->{
                    binding.progress.visibility=View.VISIBLE
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.progress.visibility=View.GONE
                }
            }
        }
    }
    private fun errorResult() {
        Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
        binding.noResultText.visibility = View.VISIBLE
        binding.progress.visibility=View.GONE
    }

    private fun loadingResult() {
        binding.progress.visibility = View.VISIBLE
        binding.noResultText.visibility = View.GONE
    }

    private fun successResult() {
        binding.noResultText.visibility = View.GONE
        binding.progress.visibility = View.GONE
    }
    private fun observeAddress(){
        observeVenueDetails()
    }
    private fun observeUser(){
        observeAddress()
    }
    private fun <T> handleResult(result: Resource<T>, actionOnSuccess: (T) -> Unit) {
        when (result.status) {
            Status.ERROR -> {
                errorResult()
            }
            Status.SUCCESS -> {
                result.data?.let(actionOnSuccess)
                successResult()
            }
            Status.LOADING -> {
                loadingResult()
            }
        }
    }
}