package com.imranmelikov.folt.presentation.itemsearch

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.constants.ItemSearchConstants
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.VenueMenuConstants
import com.imranmelikov.folt.databinding.FragmentItemSearchBinding
import com.imranmelikov.folt.presentation.storeitems.StoreMenuAdapter
import com.imranmelikov.folt.presentation.venuedetails.VenueDetailsAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ItemSearchFragment : Fragment() {
   private lateinit var binding:FragmentItemSearchBinding
   private lateinit var viewModel: ItemSearchViewModel
   private lateinit var venueDetailsAdapter:VenueDetailsAdapter
   private lateinit var storeMenuAdapter:StoreMenuAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentItemSearchBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[ItemSearchViewModel::class.java]

        searchEditText()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }
    private fun searchEditText() {
        val getArgumentString=arguments?.getInt(ItemSearchConstants.ItemSearch)
        val searchText=binding.searchEdittext
        searchText.requestFocus()
        searchText.postDelayed({
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT)
        }, 250)
        var job: Job?=null
        searchText.addTextChangedListener{
            job?.cancel()
            job =lifecycleScope.launch {
                it?.let {
                    if (it.toString().isNotEmpty()){
                        binding.searchClose.visibility=View.VISIBLE
                        delay(1000)
                        getArgumentString?.let {intArg->
                            when(intArg){
                                ItemSearchConstants.ItemSearchRestaurant->{
                                    viewModel.getRestaurantMenuList(it.toString())
                                }
                                ItemSearchConstants.ItemSearchStores->{
                                    viewModel.getStoreMenuList(it.toString())
                                }
                                ItemSearchConstants.ItemSearchStoreCategories->{
                                    viewModel.getStoreMenuCategoryList(it.toString())
                                }
                            }
                        }
                    }else{
                        binding.searchClose.visibility=View.GONE
                    }

                }
            }
            binding.searchClose.setOnClickListener {_->
                searchText.text.clear()
                binding.searchClose.visibility=View.GONE
            }
        }
        getArgumentString?.let { int->
            when (int) {
                ItemSearchConstants.ItemSearchStores -> {
                    initializeStoreRv()
                    observeStoreMenuList()
                }
                ItemSearchConstants.ItemSearchRestaurant -> {
                    initializeVenueDetailsRv()
                    observeRestaurantMenu()
                }
                ItemSearchConstants.ItemSearchStoreCategories -> {
                    initializeVenueDetailsRv()
                    observeStoreCategory()
                }
            }
        }
    }
    private fun observeRestaurantMenu(){
        val venueId=arguments?.getInt(VenueConstants.venues)
        viewModel.restaurantMenuLiveData.observe(viewLifecycleOwner){venueDetailsItems->
            val filteredVenueDetailsItems=venueDetailsItems.filter { it.parentId==venueId }
            if (filteredVenueDetailsItems.isNotEmpty()){
                venueDetailsAdapter.viewType= VenueMenuConstants.RestaurantMenu
                venueDetailsAdapter.venueDetailsItemList=filteredVenueDetailsItems
            }
        }
    }
    private fun observeStoreMenuList(){
        val storeCategoryId=arguments?.getInt(ItemSearchConstants.StoreCategoryId)
        viewModel.storeMenuLiveData.observe(viewLifecycleOwner){venueDetailsItems->
            val filteredList= venueDetailsItems.filter { it.parentId==storeCategoryId }
            filteredList.map {storeItem->
                storeItem.venueDetailList?.let {
                    storeMenuAdapter.storeMenuList=it
                }
            }
        }
    }
    private fun observeStoreCategory(){
        val venueId=arguments?.getInt(VenueConstants.venues)
        viewModel.storeMenuCategoryLiveData.observe(viewLifecycleOwner){venueDetailsItems->
            val filteredStoreMenuCategory=venueDetailsItems.filter { it.parentId==venueId }
            if(filteredStoreMenuCategory.isNotEmpty()){
                venueDetailsAdapter.viewType=ItemSearchConstants.ItemSearchStoreCategories
                venueDetailsAdapter.venueDetailsItemList=filteredStoreMenuCategory
            }
        }
    }
    private fun initializeVenueDetailsRv(){
        venueDetailsAdapter=VenueDetailsAdapter()
        binding.itemSearchRv.layoutManager= LinearLayoutManager(requireContext())
        binding.itemSearchRv.adapter=venueDetailsAdapter
    }
    private fun initializeStoreRv(){
        storeMenuAdapter= StoreMenuAdapter()
        binding.itemSearchRv.layoutManager= GridLayoutManager(requireContext(),2)
        binding.itemSearchRv.adapter=storeMenuAdapter
    }
}