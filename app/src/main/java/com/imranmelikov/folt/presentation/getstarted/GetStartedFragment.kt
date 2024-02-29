package com.imranmelikov.folt.presentation.getstarted

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.constants.FireStoreConstants
import com.imranmelikov.folt.constants.SnackBarConstants
import com.imranmelikov.folt.databinding.FragmentGetStartedBinding
import com.imranmelikov.folt.presentation.LoginActivity
import com.imranmelikov.folt.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GetStartedFragment : Fragment() {
    private lateinit var binding:FragmentGetStartedBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var bundle: Bundle
   @Inject lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentGetStartedBinding.inflate(inflater,container,false)

        if (auth.currentUser!=null){
            val intent=Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
            (activity as LoginActivity).finish()
        }
        bundle=Bundle()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        registerLauncher()
        binding.saveBtn.setOnClickListener {
            getLocation()
//            findNavController().navigate(R.id.action_getStartedFragment_to_signUpFragment,bundle)
        }
        return binding.root
    }

    private fun getLocation(){
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(binding.root, SnackBarConstants.permission, Snackbar.LENGTH_INDEFINITE).setAction(
                    SnackBarConstants.permissionBtn){
                    // request permission
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }.show()
            }else{
                // request permission
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }else{
            // permission granted
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Use location data
                    bundle.apply {
                        putDouble(FireStoreConstants.lat,location.latitude)
                        putDouble(FireStoreConstants.lng,location.longitude)
                    }
                    println(location.latitude.toString())
                    findNavController().navigate(R.id.action_getStartedFragment_to_signUpFragment,bundle)
                }
            }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "${ErrorMsgConstants.location} ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun registerLauncher(){
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->
            if (result){
                if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    // permission granted
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Use location data
                            bundle.apply {
                                putDouble(FireStoreConstants.lat,location.latitude)
                                putDouble(FireStoreConstants.lng,location.longitude)
                            }
                            println(location.latitude.toString())
                            findNavController().navigate(R.id.action_getStartedFragment_to_signUpFragment,bundle)
                        }
                    }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "${ErrorMsgConstants.location} ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }else{
                // permission denied
                Toast.makeText(requireContext(), SnackBarConstants.permission, Toast.LENGTH_SHORT).show()
            }
        }
    }
}