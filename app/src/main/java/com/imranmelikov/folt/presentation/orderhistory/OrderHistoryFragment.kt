package com.imranmelikov.folt.presentation.orderhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentOrderHistoryBinding
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.presentation.orderdetail.OrderViewModel
import com.imranmelikov.folt.util.Status

class OrderHistoryFragment : Fragment() {
   private lateinit var binding:FragmentOrderHistoryBinding
   private lateinit var orderViewModel: OrderViewModel
   private lateinit var adapter: OrderHistoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderHistoryBinding.inflate(inflater,container,false)
        orderViewModel=ViewModelProvider(requireActivity())[OrderViewModel::class.java]
        clickBackBtn()
        initializeRv()
        orderViewModel.getOrderList()
        observeOrder()
        clickBrowseBtn()
        return binding.root
    }

    private fun clickBackBtn(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_orderHistoryFragment_to_profileFragment)
                (activity as MainActivity).showBottomNav()
            }
        })
        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_orderHistoryFragment_to_profileFragment)
            (activity as MainActivity).showBottomNav()
        }
    }

    private fun clickBrowseBtn(){
        binding.saveBtn.setOnClickListener {
            findNavController().navigate(R.id.action_orderHistoryFragment_to_discoveryFragment)
            (activity as MainActivity).showBottomNav()
        }
    }
    private fun observeOrder(){
        orderViewModel.orderListLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.ERROR->{
                    binding.progress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
                Status.SUCCESS->{
                    result.data?.let { orderList->
                        adapter.orderList=orderList
                        if (orderList.isEmpty()){
                            binding.saveBtn.visibility=View.VISIBLE
                            binding.noOrderText.visibility=View.VISIBLE
                        }else{
                            binding.saveBtn.visibility=View.GONE
                            binding.noOrderText.visibility=View.GONE
                        }
                    }
                    binding.progress.visibility=View.GONE
                    binding.noResultText.visibility=View.GONE
                }
                Status.LOADING->{
                    binding.progress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
            }
        }
    }
    private fun initializeRv(){
        adapter= OrderHistoryAdapter()
        binding.orderRv.layoutManager=LinearLayoutManager(requireContext())
        binding.orderRv.adapter=adapter
    }
}