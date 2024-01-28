package com.imranmelikov.folt.presentation.venuedetails

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.bumptech.glide.Glide
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentVenueDetailsBinding
import com.imranmelikov.folt.domain.model.RestaurantMenuCategory
import com.imranmelikov.folt.domain.model.VenueDetailsItem
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.util.ArgumentConstants
import com.imranmelikov.folt.util.DataViewTypes
import com.imranmelikov.folt.util.StoreCategoryTitle
import com.imranmelikov.folt.util.VenueCategoryConstants

@Suppress("DEPRECATION")
class VenueDetailsFragment : Fragment() {
    private lateinit var binding:FragmentVenueDetailsBinding
    private lateinit var viewModelVenueDetails: VenueDetailsViewModel
    private lateinit var venueDetailsAdapter:VenueDetailsAdapter
    private lateinit var restaurantMenuCategoryAdp: RestaurantMenuCategoryAdp
    val bundle=Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentVenueDetailsBinding.inflate(inflater,container,false)
        viewModelVenueDetails=ViewModelProvider(requireActivity())[VenueDetailsViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFunctions()
    }

    private fun getFunctions(){
        viewModelVenueDetails.getRestaurantMenuList()
        viewModelVenueDetails.getRestaurantMenuCategoryList()
        viewModelVenueDetails.getStoreMenuCategoryList()
        initialiseVenueDetailsRv()
//        initialiseCategoryRv()
        getControlArguments()
        (activity as MainActivity).hideBottomNav()
    }

    private fun clickBtn(venue: Venue){
        clickBackBtn()
        clickDeliveryBtn()
        clickFavBtn(venue)
        clickMoreBtn(venue)
        clickShareBtn(venue)
        clickSearchBtn(venue)
    }
    private fun clickBackBtn(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
            (activity as MainActivity).showBottomNav()
        }
    }
    private fun clickDeliveryBtn(){
        // !!!!!!
        binding.deliveryBtn.setOnClickListener {
        }
    }
    private fun clickFavBtn(venue: Venue){
        //!!!!!!!
        if (venue.venuePopularity.favorite){
            binding.favImg.setImageResource(R.drawable.heart_inline)
            binding.favBtn.setOnClickListener {
                binding.favImg.setImageResource(R.drawable.heart_outline)
               venue.venuePopularity.favorite=false
                clickFavBtn(venue)
            }
        }else{
            binding.favImg.setImageResource(R.drawable.heart_outline)
            binding.favBtn.setOnClickListener {
                binding.favImg.setImageResource(R.drawable.heart_inline)
                venue.venuePopularity.favorite=true
                clickFavBtn(venue)
            }
        }
    }
    private fun clickSearchBtn(venue: Venue){
        binding.searchBtn.setOnClickListener {
            bundle.apply {
                putSerializable(ArgumentConstants.venues,venue)
            }
            findNavController().navigate(R.id.action_venueDetailsFragment_to_itemSearchFragment)
        }
    }
    private fun clickMoreBtn(venue: Venue){
        binding.moreTextview.setOnClickListener {
           bundle.apply {
                putSerializable(ArgumentConstants.venueInformation,venue)
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

        val receivedVenueString=receiveArgs?.getString(VenueCategoryConstants.Venue)

        val receivedVenue = receiveArgs?.getSerializable(ArgumentConstants.venues) as? Venue
        receivedVenue?.let {venue->
            "Delivery : ${venue.delivery.deliveryPrice} AZN".also { binding.deliveryTextview.text = it }
            binding.venueNameTextview.text=venue.venueName
            "Rating : ${venue.venuePopularity.rating}".also { binding.ratingTextview.text=it }
            "Delivery ${venue.delivery.deliveryTime} min    ▾".also { binding.deliveryBtn.text=it }

            Glide.with(requireActivity())
                .load(venue.image)
                .into(binding.mainImage)

            if (receivedVenueString==VenueCategoryConstants.Restaurant){
                observeRestaurantViewModel(venue)
            }else if(receivedVenueString==VenueCategoryConstants.Store){
                observeStoreViewModel(venue)
                binding.toolbarLinear.visibility=View.GONE
                binding.toolbarCardView.visibility=View.GONE
                binding.appBarLayout.visibility=View.GONE
                binding.toolbar.visibility=View.GONE
            }
            clickBtn(venue)
        }
    }
    private fun observeRestaurantViewModel(venue: Venue){
        val mutableMenuList:MutableList<VenueDetailsItem> = mutableListOf()
        val mutableStringList:MutableList<String> = mutableListOf()
        viewModelVenueDetails.restaurantMenuCategoryLiveData.observe(viewLifecycleOwner){restaurantMenuCategoryList->
            viewModelVenueDetails.restaurantMenuLiveData.observe(viewLifecycleOwner){venueDetails->
                val filteredRestaurantMenuCategory=restaurantMenuCategoryList.filter {
                    it.parentId==venue.id }
                filteredRestaurantMenuCategory.map {restaurantMenuCategory ->
                    val filteredMenuList=venueDetails.filter { it.parentId==restaurantMenuCategory.id }
                    if(filteredMenuList.isNotEmpty()){
                       val venueDetailsItem=VenueDetailsItem(restaurantMenuCategory.title,filteredMenuList)
                        mutableMenuList.add(venueDetailsItem)
                        venueDetailsAdapter.viewType=DataViewTypes.RestaurantMenu
                        venueDetailsAdapter.venueDetailsItemList= mutableMenuList.toList()
                        initTabLayout(restaurantMenuCategory)
                        initMediator(mutableMenuList.toList())
//                        mutableStringList.add(restaurantMenuCategory.title)
//                        restaurantMenuCategoryAdp.categoryNameList=mutableStringList
                    }
                }
            }
        }
    }
    private fun observeStoreViewModel(venue: Venue){
        val mutableCategoryList:MutableList<VenueDetailsItem> = mutableListOf()
        viewModelVenueDetails.storeMenuCategoryLiveData.observe(viewLifecycleOwner){storeMenuCategoryList->
                val filteredStoreMenuCategory=storeMenuCategoryList.filter {
                    it.restaurantMenuCategory.parentId==venue.id }
                    if (filteredStoreMenuCategory.isNotEmpty()){
                        val venueDetailsItem=VenueDetailsItem(StoreCategoryTitle.storeCategoryTitle,filteredStoreMenuCategory,false)
                        mutableCategoryList.add(venueDetailsItem)
                        venueDetailsAdapter.viewType=DataViewTypes.StoreMenuCategory
                        venueDetailsAdapter.venueDetailsItemList= mutableCategoryList.toList()
            }
        }
    }
    private fun initialiseVenueDetailsRv(){
        venueDetailsAdapter= VenueDetailsAdapter()
        binding.venueDetailRv.layoutManager=LinearLayoutManager(requireContext())
        binding.venueDetailRv.adapter=venueDetailsAdapter
    }
    private fun initMediator(venueDetailsItem: List<VenueDetailsItem>) {
        TabbedListMediator(
            binding.venueDetailRv,
            binding.tabLayout,
            venueDetailsItem.indices.toList(),
            true
        ).attach()
    }
    private fun initTabLayout(restaurantMenuCategory: RestaurantMenuCategory) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(restaurantMenuCategory.title))
    }
//    private fun initialiseCategoryRv(){
//        restaurantMenuCategoryAdp= RestaurantMenuCategoryAdp()
//        binding.toolbarVenueRv.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//        binding.toolbarVenueRv.adapter=restaurantMenuCategoryAdp
//    }
}