package com.imranmelikov.folt.presentation.venuedetails

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.constants.ItemSearchConstants
import com.imranmelikov.folt.constants.OrderConstants
import com.imranmelikov.folt.databinding.FragmentVenueDetailsBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.VenueMenuConstants
import com.imranmelikov.folt.constants.VenueInformationConstants
import com.imranmelikov.folt.presentation.venue.VenueViewModel
import com.imranmelikov.folt.util.Resource
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("DEPRECATION")
class VenueDetailsFragment : Fragment() {
    private lateinit var binding:FragmentVenueDetailsBinding
    private lateinit var viewModelVenueDetails: VenueDetailsViewModel
    private lateinit var venueDetailsAdapter:VenueDetailsAdapter
    private lateinit var venueViewModel: VenueViewModel
    val bundle=Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentVenueDetailsBinding.inflate(inflater,container,false)
        viewModelVenueDetails=ViewModelProvider(requireActivity())[VenueDetailsViewModel::class.java]
        venueViewModel=ViewModelProvider(requireActivity())[VenueViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFunctions()
    }

    private fun getFunctions(){
        viewModelVenueDetails.getVenueMenuList()
        viewModelVenueDetails.getStoreMenuCategoryList()
        viewModelVenueDetails.getVenueDetailsFromRoom()
        initialiseVenueDetailsRv()
        getControlArguments()
        observeCRUD()
        (activity as MainActivity).hideBottomNav()
    }

    private fun clickBtn(venue: Venue){
        clickBackBtn()
        clickDeliveryBtn()
        clickMoreBtn(venue)
        clickShareBtn(venue)
        clickSearchBtn(venue)
    }
    private fun clickBackBtn(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                viewModelVenueDetails.deleteAllVenueDetailsFromRoom()
                (activity as MainActivity).showBottomNav()
            }
        })
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
            viewModelVenueDetails.deleteAllVenueDetailsFromRoom()
            (activity as MainActivity).showBottomNav()
        }
    }
    private fun clickDeliveryBtn(){
        binding.deliveryBtn.setOnClickListener {
        }
    }
    private fun clickOrderBtn(venue: Venue){
        binding.orderBtn.setOnClickListener {
            val bundleForOrder=Bundle()
            bundleForOrder.apply {
                putSerializable(OrderConstants.venueForOrder,venue)
            }
            findNavController().navigate(R.id.action_venueDetailsFragment_to_orderFragment,bundleForOrder)
        }
    }
    private fun clickSearchBtn(venue: Venue){
        binding.searchBtn.setOnClickListener {
            bundle.apply {
                putString(VenueConstants.venues,venue.id)
            }
            findNavController().navigate(R.id.action_venueDetailsFragment_to_itemSearchFragment,bundle)
        }
    }
    private fun clickMoreBtn(venue: Venue){
        binding.moreTextview.setOnClickListener {
           bundle.apply {
                putSerializable(VenueInformationConstants.venueInformation,venue)
            }
            findNavController().navigate(R.id.action_venueDetailsFragment_to_venueInformationFragment,bundle)
        }
    }
    private fun clickShareBtn(venue: Venue){
        binding.shareBtn.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, venue.venueName +" "+ venue.venueInformation.url)
            }
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }
    }

    private fun getControlArguments(){
        val receiveArgs = arguments

        val receivedVenue = receiveArgs?.getSerializable(VenueConstants.venues) as? Venue
        receivedVenue?.let {venue->
            "Delivery : ${venue.delivery.deliveryPrice} AZN".also { binding.deliveryTextview.text = it }
            binding.venueNameTextview.text=venue.venueName
            "Rating : ${venue.venuePopularity.rating}".also { binding.ratingTextview.text=it }
            "Delivery ${venue.delivery.deliveryTime} min    ▾".also { binding.deliveryBtn.text=it }

            Glide.with(requireActivity())
                .load(venue.imageUrl)
                .into(binding.mainImage)
            Glide.with(requireActivity())
                .load(venue.venueLogoUrl)
                .into(binding.logo)

            observeFavVenues(venue)
            observeVenueDetailsFromRoom(venue)
            // If you are using tabLayout for menus, you will need to use this distinction
            if (venue.restaurant){
                observeRestaurantMenuViewModel(venue)
            }else{
                observeStoreViewModel(venue)
            }
            clickBtn(venue)
        }
    }

    private fun observeFavVenues(venue: Venue){
        venueViewModel.favoriteVenueLiveData.observe(viewLifecycleOwner) {result->
            handleResult(result){venues ->
                clickFavIcon(venues,venue)
            }

        }

    }
    private fun clickFavIcon(venues:List<Venue>,venue: Venue){
        if (venues.isEmpty()){
            binding.favImg.setImageResource(R.drawable.heart_outline_black)
            binding.favBtn.setOnClickListener {
                // Add venue to favorites
                venueViewModel.insertFavoriteVenue(venue)
                binding.favImg.setImageResource(R.drawable.heart_inline_black)
                clickFavIcon(venues,venue)
            }
        }else{
            venues.map {favVenue->
                if (venue.id==favVenue.id){
                    binding.favImg.setImageResource(R.drawable.heart_inline_black)
                    binding.favBtn.setOnClickListener {_->
                        // Remove venue from favorites
                        venueViewModel.deleteFavoriteVenue(favVenue.id)
                        binding.favImg.setImageResource(R.drawable.heart_outline_black)
                        clickFavIcon(venues,venue)
                    }
                }else{
                    binding.favImg.setImageResource(R.drawable.heart_outline_black)
                    binding.favBtn.setOnClickListener {
                        // Add venue to favorites
                        venueViewModel.insertFavoriteVenue(venue)
                        binding.favImg.setImageResource(R.drawable.heart_inline_black)
                        clickFavIcon(venues,venue)
                    }
                }
            }
        }
    }
    private fun observeRestaurantMenuViewModel(venue: Venue){
        viewModelVenueDetails.venueMenuLiveData.observe(viewLifecycleOwner){result->
            handleResult(result){venueDetailsItems->
                val filteredVenueDetailsItems=venueDetailsItems.filter { it.parentId==venue.id }
                venueDetailsItems.map {
                    println(it.venueDetailList)
                }
                if (filteredVenueDetailsItems.isNotEmpty()){
                    filteredVenueDetailsItems.map {
                        println(it.venueDetailList)
                    }
                    venueDetailsAdapter.viewType=VenueMenuConstants.RestaurantMenu
                    venueDetailsAdapter.venueDetailsItemList=filteredVenueDetailsItems
                }

            }
        }
        bundle.apply {
            putInt(ItemSearchConstants.ItemSearch, ItemSearchConstants.ItemSearchRestaurant)
        }
    }
    private fun observeStoreViewModel(venue: Venue){
        viewModelVenueDetails.storeMenuCategoryLiveData.observe(viewLifecycleOwner){result->
            handleResult(result){ venueDetailsItems->
                val filteredStoreMenuCategory=venueDetailsItems.filter { it.parentId==venue.id }
                if(filteredStoreMenuCategory.isNotEmpty()){
                    venueDetailsAdapter.viewType=VenueMenuConstants.StoreMenuCategory
                    venueDetailsAdapter.venueDetailsItemList=filteredStoreMenuCategory
                }
            }
        }
        bundle.apply {
            putInt(ItemSearchConstants.ItemSearch, ItemSearchConstants.ItemSearchStoreCategories)
        }
    }

    private fun observeCRUD(){
        venueViewModel.msgLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {
                        Log.d(it.message,it.success.toString())
                    }
                    binding.venueDetailProgress.visibility=View.GONE
                }
                Status.LOADING->{
                    binding.venueDetailProgress.visibility=View.VISIBLE
                    binding.favBtn.setOnClickListener {  }
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.venueDetailProgress.visibility=View.GONE
                }
            }
        }
    }
    private fun observeVenueDetailsFromRoom(venue: Venue){
        viewModelVenueDetails.venueDetailsLiveData.observe(viewLifecycleOwner){venueDetailList->
            if (venueDetailList.isNotEmpty()){
                var totalCount=0
                var totalPrice=0.0
                binding.orderBtn.visibility=View.VISIBLE
                venueDetailList.map { venueDetails->
                    totalCount += venueDetails.count
                    totalPrice += venueDetails.price
                }
                val formattedTotalPrice = String.format("%.2f", totalPrice)
                "$totalCount View order     $formattedTotalPrice Azn".also { binding.orderBtn.text = it }
                clickOrderBtn(venue)
            }else{
                binding.orderBtn.visibility=View.GONE
            }
            venueDetailsAdapter.venueDetailsList=venueDetailList
        }
    }
    private fun initialiseVenueDetailsRv(){
        venueDetailsAdapter= VenueDetailsAdapter(requireActivity() as AppCompatActivity)
        binding.venueDetailRv.layoutManager=LinearLayoutManager(requireContext())
        binding.venueDetailRv.adapter=venueDetailsAdapter
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

    private fun loadingResult() {
        binding.noResultText.visibility=View.GONE
        binding.venueDetailProgress.visibility=View.VISIBLE
    }

    private fun successResult() {
        binding.noResultText.visibility=View.GONE
        binding.venueDetailProgress.visibility=View.GONE
    }

    private fun errorResult() {
        Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
        binding.noResultText.visibility=View.VISIBLE
        binding.venueDetailProgress.visibility=View.GONE
    }
}