package com.imranmelikov.folt.presentation.bottomsheetfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.imranmelikov.folt.data.local.entity.VenueDetailsRoom
import com.imranmelikov.folt.databinding.FragmentMenuBottomSheetBinding
import com.imranmelikov.folt.domain.model.VenueDetails
import com.imranmelikov.folt.presentation.venuedetails.VenueDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuBottomSheetFragment:BottomSheetDialogFragment() {
    private lateinit var binding:FragmentMenuBottomSheetBinding
     lateinit var venueDetails: VenueDetails
    private lateinit var viewModel: VenueDetailsViewModel
    var onItemClick:((Int)->Unit)?=null
    private var count=1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMenuBottomSheetBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[VenueDetailsViewModel::class.java]
        viewModel.getVenueDetailsFromRoom()

        binding.downBtn.setOnClickListener {
            dismiss()
        }
        observeVenueDetails()
        return binding.root
    }

   private fun getVenueDetail(venueDetailListFromRoom:List<VenueDetailsRoom>){
       binding.menuTitleText.text=venueDetails.menuName
       binding.menuText.text=venueDetails.about
       binding.menuPriceText.text=venueDetails.price.toDouble().toString()
       binding.countText.text=venueDetails.count.toString()
       "Add order ${venueDetails.price.toDouble()} Azn".also { binding.orderBtn.text = it }

       venueDetailListFromRoom.map {venueDetail->
               binding.countText.text=venueDetail.count.toString()
               "Add order ${venueDetail.price} Azn".also { binding.orderBtn.text = it }
               count=venueDetail.count
       }

       if(venueDetails.stock.toInt()==0){
           count=0
           binding.countText.text=count.toString()
       }

       binding.plusBtn.setOnClickListener {
           if (count<venueDetails.stock.toInt()){
               count+=1
               val formattedResult = String.format("%.2f", count * venueDetails.price.toDouble())
               "Add order $formattedResult Azn".also { binding.orderBtn.text = it }
               binding.countText.text=count.toString()
           }
       }
       binding.minusBtn.setOnClickListener {
           if (count>0){
               count-=1
               val formattedResult = String.format("%.2f", count * venueDetails.price.toDouble())
               "Add order $formattedResult Azn".also { binding.orderBtn.text = it }
               binding.countText.text=count.toString()
           }
       }
       clickOrderBtn(venueDetailListFromRoom)
    }
    private fun clickOrderBtn(venueDetailListFromRoom: List<VenueDetailsRoom>){
        if (venueDetails.stock.toInt()==0){
            binding.orderBtn.isEnabled=false
        }else{
            binding.orderBtn.setOnClickListener {
                if (venueDetailListFromRoom.isNotEmpty()){
                    venueDetailListFromRoom.map {
                        if (count==0){
                            viewModel.deleteVenueDetailsFromRoom(it)
                        }else{
                            it.count=count
                            it.price=count * venueDetails.price.toDouble()
                            viewModel.updateVenueDetailsFromRoom(it)
                        }
                    }
                }else{
                    if (count!=0){
                        val venueDetailsRoom=
                            VenueDetailsRoom(venueDetails.id,venueDetails.imageUrl,count * venueDetails.price.toDouble(),
                            venueDetails.menuName,venueDetails.about,venueDetails.popularity,venueDetails.stock.toInt(),
                            count,venueDetails.selected)
                        viewModel.insertVenueDetailsToRoom(venueDetailsRoom)
                    }
                }
                onItemClick?.let {
                    it(count)
                    count=1
                }
                dismiss()
            }
        }
    }
    private fun observeVenueDetails(){
        viewModel.venueDetailsLiveData.observe(viewLifecycleOwner){venueDetailList->
           val filteredList= venueDetailList.filter {
               it.id==venueDetails.id
           }
            getVenueDetail(filteredList)
        }
    }
}