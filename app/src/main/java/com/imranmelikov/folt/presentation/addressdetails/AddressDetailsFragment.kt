package com.imranmelikov.folt.presentation.addressdetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.AddressConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.constants.OrderConstants
import com.imranmelikov.folt.databinding.FragmentAddressDetailsBinding
import com.imranmelikov.folt.domain.model.Address
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.presentation.address.AddressViewModel
import com.imranmelikov.folt.util.Status

@Suppress("DEPRECATION")
class AddressDetailsFragment : Fragment() {
  private lateinit var binding:FragmentAddressDetailsBinding
  private lateinit var addressViewModel: AddressViewModel
  private lateinit var bundle: Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddressDetailsBinding.inflate(inflater,container,false)
        addressViewModel=ViewModelProvider(requireActivity())[AddressViewModel::class.java]
        bundle=Bundle()
        controlArguments()
        clickBackBtn()
        observeCRUD()
        return binding.root
    }

    private fun clickBackBtn(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun controlArguments(){
        val receiveString=arguments?.getString(AddressConstants.addressDetailBundleKey)
        receiveString?.let { string->
            when(string){
                AddressConstants.address->{
                    updateAddress()
                }
                else->{
                    writeNamesToTextView(string)
                }
            }
        }
    }
    private fun writeNamesToTextView(string: String){
        val receiveCountryName=arguments?.getString(AddressConstants.country)
        val receiveStreetName=arguments?.getString(AddressConstants.streetAddress)
        receiveCountryName?.let {countryName->
            receiveStreetName?.let { streetName->
                binding.addressInformation.text=streetName
                binding.countryInformation.text=countryName
                binding.addressToolbarText.text=streetName
                insertAddress(string,countryName,streetName)
            }
        }
    }
    private fun insertAddress(string: String,countryName:String,streetName:String){
        val buildingEditText=binding.buildingEditText.text
        val entranceEditText=binding.entranceEditText.text
        val floor=binding.floorEditText.text
        val apartment=binding.apartmentEditText.text
        val doorCode=binding.doorCodeEditText.text

        binding.saveBtn.setOnClickListener {
            if (entranceEditText.isEmpty()||floor.isEmpty()||apartment.isEmpty()){
                Toast.makeText(requireContext(),ErrorMsgConstants.editTextMsg,Toast.LENGTH_SHORT).show()
            }else{
                val address=Address("",streetName,countryName,buildingEditText.toString(),entranceEditText.toString().toInt(),
                    floor.toString().toInt(),apartment.toString().toInt(),doorCode.toString())
                when(string){
                    AddressConstants.orderDetail->{
                        val receiveVenue=arguments?.getSerializable(OrderConstants.venueForOrder) as? Venue
                        receiveVenue?.let { venue->
                            bundle.apply {
                                putSerializable(OrderConstants.venueForOrder,venue)
                            }
                            addressViewModel.insertAddress(address)
                            findNavController().navigate(R.id.action_addressDetailsFragment_to_orderDetailFragment,bundle)
                        }
                    }
                    AddressConstants.newAddress->{
                        addressViewModel.insertAddress(address)
                        findNavController().navigate(R.id.action_addressDetailsFragment_to_addressFragment)
                    }
                }
            }
        }
    }
    private fun updateAddress(){
        val receiveAddress=arguments?.getSerializable(AddressConstants.address) as? Address
        receiveAddress?.let { address->
            val buildingEditText=binding.buildingEditText
            val entranceEditText=binding.entranceEditText
            val floor=binding.floorEditText
            val apartment=binding.apartmentEditText
            val doorCode=binding.doorCodeEditText

            binding.addressToolbarText.text=address.addressName
            binding.addressInformation.text=address.addressName
            binding.countryInformation.text=address.countryName
            buildingEditText.setText(address.buildingName)
            doorCode.setText(address.doorCode)
            apartment.setText(address.apartment.toInt().toString())
            entranceEditText.setText(address.entrance.toInt().toString())
            floor.setText(address.floor.toInt().toString())
            binding.deleteBtn.visibility=View.VISIBLE

            binding.saveBtn.setOnClickListener {
                if (apartment.text.isEmpty()||entranceEditText.text.isEmpty()||floor.text.isEmpty()){
                    Toast.makeText(requireContext(),ErrorMsgConstants.editTextMsg,Toast.LENGTH_SHORT).show()
                }else{
                    address.buildingName=buildingEditText.text.toString()
                    address.entrance=entranceEditText.text.toString().toInt()
                    address.floor=floor.text.toString().toInt()
                    address.apartment=apartment.text.toString().toInt()
                    address.doorCode=doorCode.text.toString()
                    addressViewModel.updateAddress(address.id,address)
                    findNavController().popBackStack()
                }
            }
            clickDeleteBtn(address.id)
        }
    }
    private fun clickDeleteBtn(documentId:String){
        binding.deleteBtn.setOnClickListener {
            addressViewModel.deleteAddress(documentId)
            findNavController().navigate(R.id.action_addressDetailsFragment_to_addressFragment)
        }
    }
    private fun observeCRUD(){
        addressViewModel.msgLiveData.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS->{
                    it.data?.let {crud->
                        Log.d(crud.message,crud.success.toString())
                        binding.progress.visibility=View.GONE
                        binding.noResultText.visibility=View.GONE
                    }
                }
                Status.LOADING->{
                    binding.progress.visibility=View.VISIBLE
                }
                Status.ERROR->{
                    binding.progress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
            }
        }
    }
}