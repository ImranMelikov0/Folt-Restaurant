package com.imranmelikov.folt.presentation.photogalleryFragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.imranmelikov.folt.constants.AccountConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.constants.SnackBarConstants
import com.imranmelikov.folt.databinding.FragmentPhotoBinding
import com.imranmelikov.folt.domain.model.User
import com.imranmelikov.folt.presentation.account.AccountViewModel
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("DEPRECATION")
class PhotoFragment : Fragment() {
    private lateinit var binding:FragmentPhotoBinding
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private  var selectedImage: Uri?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentPhotoBinding.inflate(inflater,container,false)
        accountViewModel= ViewModelProvider(requireActivity())[AccountViewModel::class.java]

        registerLauncher()
        binding.photoLibrary.setOnClickListener {
            pickImageFromGallery()
        }
        getControlArg()
        observeCRUD()
        binding.closeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun getControlArg(){
        val getArg=arguments?.getSerializable(AccountConstants.user) as? User
        getArg?.let {
            deleteImg(it)
        }
    }

    private fun pickImageFromGallery(){
        if (ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES)){
                // request permission
                Snackbar.make(binding.root,SnackBarConstants.permissionGallery,Snackbar.LENGTH_INDEFINITE).setAction(SnackBarConstants.permissionBtn){
                    // request permission
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                }.show()
            }else{
                // request permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
        }else{
            // permission granted
            val intentToGallery=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLauncher(){
        activityResultLauncher=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedImage = intentFromResult.data
                    selectedImage.let {
                        println(it)
                    }
                }
            }
        }
        permissionLauncher=registerForActivityResult(
            ActivityResultContracts.RequestPermission()) {
            if (it) {
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                Toast.makeText(
                    requireActivity(),
                    SnackBarConstants.permissionGallery,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun deleteImg(user: User){
        binding.deleteProfilePhoto.setOnClickListener {
            user.imageUrl=""
            accountViewModel.updateUser(user)
            findNavController().popBackStack()
        }
    }
    private fun observeCRUD(){
        accountViewModel.msgLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {
                        Log.d(it.message,it.success.toString())
                    }
                }
                Status.LOADING->{
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}