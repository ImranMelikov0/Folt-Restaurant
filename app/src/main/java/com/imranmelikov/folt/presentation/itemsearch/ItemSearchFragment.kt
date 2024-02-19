package com.imranmelikov.folt.presentation.itemsearch

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.constants.ItemSearchConstants
import com.imranmelikov.folt.constants.VenueConstants
import com.imranmelikov.folt.constants.VenueMenuConstants
import com.imranmelikov.folt.databinding.FragmentItemSearchBinding
import com.imranmelikov.folt.presentation.storeitems.StoreMenuAdapter
import com.imranmelikov.folt.presentation.venuedetails.VenueDetailsAdapter
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
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
        val getArgumentInt=arguments?.getInt(ItemSearchConstants.ItemSearch)
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
                    getArgumentInt?.let {intArg->
                        when(intArg){
                            ItemSearchConstants.ItemSearchRestaurant->{
                                if (it.toString().isNotEmpty()) {
                                    binding.searchClose.visibility = View.VISIBLE
                                    delay(1000)
                                    viewModel.searchVenueMenuList(it.toString())
                                    observeRestaurantMenu()
                                }else{
                                    venueDetailsAdapter.venueDetailsItemList= emptyList()
                                    binding.searchClose.visibility=View.GONE
                                }
                            }
                            ItemSearchConstants.ItemSearchStores->{
                                if (it.toString().isNotEmpty()) {
                                    binding.searchClose.visibility = View.VISIBLE
                                    delay(1000)
                                    viewModel.searchVenueMenuList(it.toString())
                                    observeStoreMenuList()
                                }else{
                                    storeMenuAdapter.storeMenuList= emptyList()
                                    binding.searchClose.visibility=View.GONE
                                }
                            }
                            ItemSearchConstants.ItemSearchStoreCategories->{
                                if (it.toString().isNotEmpty()) {
                                    binding.searchClose.visibility = View.VISIBLE
                                    delay(1000)
                                    viewModel.searchStoreMenuCategoryList(it.toString())
                                    observeStoreCategory()
                                }else{
                                    venueDetailsAdapter.venueDetailsItemList= emptyList()
                                    binding.searchClose.visibility=View.GONE
                                }
                            }
                        }
                    }
                }
            }
            binding.searchClose.setOnClickListener {_->
                getArgumentInt?.let {intArg->
                    when(intArg){
                        ItemSearchConstants.ItemSearchRestaurant->{
                            venueDetailsAdapter.venueDetailsItemList= emptyList()
                        }
                        ItemSearchConstants.ItemSearchStores->{
                            storeMenuAdapter.storeMenuList= emptyList()
                        }
                        ItemSearchConstants.ItemSearchStoreCategories->{
                            venueDetailsAdapter.venueDetailsItemList= emptyList()
                        }
                    }
                }
                searchText.text.clear()
                binding.searchClose.visibility=View.GONE
            }
        }
        getArgumentInt?.let { int->
            when (int) {
                ItemSearchConstants.ItemSearchStores -> {
                    initializeStoreRv()
                }
                ItemSearchConstants.ItemSearchRestaurant -> {
                    initializeVenueDetailsRv()
                }
                ItemSearchConstants.ItemSearchStoreCategories -> {
                    initializeVenueDetailsRv()
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observeRestaurantMenu(){
        val venueId=arguments?.getString(VenueConstants.venues)
        viewModel.venueMenuLiveData.observe(requireActivity()){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {venueDetailsItems->
                        val filteredVenueDetailsItems=venueDetailsItems.filter { it.parentId==venueId }
                        venueDetailsAdapter.viewType= VenueMenuConstants.RestaurantMenu
                        venueDetailsAdapter.venueDetailsItemList=filteredVenueDetailsItems
                        venueDetailsAdapter.notifyDataSetChanged()
                        binding.itemSearchProgress.visibility=View.GONE
                        binding.noResultText.visibility=View.GONE
                    }
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.itemSearchProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
                Status.LOADING->{
                    binding.itemSearchProgress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
            }
        }
    }
    private fun observeStoreMenuList(){
        val storeCategoryId=arguments?.getString(ItemSearchConstants.StoreCategoryId)
        viewModel.venueMenuLiveData.observe(requireActivity()){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {venueDetailsItems ->
                        val filteredList= venueDetailsItems.filter { it.parentId==storeCategoryId }
                        filteredList.map {storeItem->
                            storeItem.venueDetailList?.let {
                                storeMenuAdapter.storeMenuList=it
                            }
                        }
                        binding.itemSearchProgress.visibility=View.GONE
                        binding.noResultText.visibility=View.GONE
                    }
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.itemSearchProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
                Status.LOADING->{
                    binding.itemSearchProgress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observeStoreCategory(){
        val venueId=arguments?.getString(VenueConstants.venues)
        viewModel.storeMenuCategoryLiveData.observe(requireActivity()){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {venueDetailsItems ->
                        val filteredStoreMenuCategory=venueDetailsItems.filter { it.parentId==venueId }
                        venueDetailsAdapter.viewType=ItemSearchConstants.ItemSearchStoreCategories
                        venueDetailsAdapter.venueDetailsItemList=filteredStoreMenuCategory
                        venueDetailsAdapter.notifyDataSetChanged()
                        binding.itemSearchProgress.visibility=View.GONE
                        binding.noResultText.visibility=View.GONE
                    }
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.itemSearchProgress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
                Status.LOADING->{
                    binding.itemSearchProgress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
            }
        }
    }
    private fun initializeVenueDetailsRv(){
        venueDetailsAdapter=VenueDetailsAdapter(requireActivity() as AppCompatActivity)
        binding.itemSearchRv.layoutManager= LinearLayoutManager(requireContext())
        binding.itemSearchRv.adapter=venueDetailsAdapter
    }
    private fun initializeStoreRv(){
        storeMenuAdapter= StoreMenuAdapter(requireActivity() as AppCompatActivity)
        binding.itemSearchRv.layoutManager= GridLayoutManager(requireContext(),2)
        binding.itemSearchRv.adapter=storeMenuAdapter
    }
}