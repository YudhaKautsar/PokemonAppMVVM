package com.yudha.pokemonapp.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.yudha.pokemonapp.R
import com.yudha.pokemonapp.databinding.FragmentProfileBinding
import com.yudha.pokemonapp.ui.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ProfileViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private var photoUri: Uri? = null
    
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                loadImageToView(uri)
            }
        }
    }
    
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            photoUri?.let { uri ->
                selectedImageUri = uri
                loadImageToView(uri)
            }
        }
    }
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val readPermission = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
        val cameraPermission = permissions[Manifest.permission.CAMERA] ?: false
        
        if (readPermission && cameraPermission) {
            showImageSourceDialog()
        } else {
            Toast.makeText(context, getString(R.string.permissions_required), Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
        loadInitialProfileImage()
    }
    
    private fun loadInitialProfileImage() {
        // Load profile image when fragment is first created
        viewModel.user.value?.let { user ->
            if (!user.profileImagePath.isNullOrEmpty()) {
                loadImageFromPath(user.profileImagePath)
            }
        }
    }
    

    
    private fun setupUI() {
        binding.btnChangePhoto.setOnClickListener {
            checkPermissionAndShowDialog()
        }
        
        binding.btnSave.setOnClickListener {
            saveProfile()
        }
        
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }
    
    private fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.tvUsername.text = it.username
                binding.tvEmail.text = it.email
                binding.etFullname.setText(it.fullName ?: "")
                binding.etPhone.setText(it.phoneNumber ?: "")
                
                // Load profile image only if no new image is selected
                if (selectedImageUri == null && !it.profileImagePath.isNullOrEmpty()) {
                    loadImageFromPath(it.profileImagePath)
                }
            }
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSave.isEnabled = !isLoading
            binding.btnChangePhoto.isEnabled = !isLoading
        }
        
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                binding.tvError.text = errorMessage
                binding.tvError.visibility = View.VISIBLE
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            } else {
                binding.tvError.visibility = View.GONE
            }
        }
        
        viewModel.updateSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
                // Reset selectedImageUri after successful save
                selectedImageUri = null
                viewModel.clearUpdateSuccess()
            }
        }
    }
    
    private fun checkPermissionAndShowDialog() {
        val readPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        
        val cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        
        when {
            readPermission && cameraPermission -> {
                showImageSourceDialog()
            }
            else -> {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    )
                )
            }
        }
    }
    
    private fun showImageSourceDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.select_image_source))
            .setItems(arrayOf(getString(R.string.camera), getString(R.string.gallery))) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openImagePicker()
                }
            }
            .show()
    }
    
    private fun openCamera() {
        val photoFile = File(requireContext().filesDir, "temp_photo_${System.currentTimeMillis()}.jpg")
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile
        )
        
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        cameraLauncher.launch(intent)
    }
    
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }
    
    private fun loadImageToView(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .into(binding.ivProfile)
    }
    
    private fun loadImageFromPath(imagePath: String) {
        val file = File(imagePath)
        if (file.exists()) {
            Glide.with(this)
                .load(file)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(binding.ivProfile)
        } else {
            // If file doesn't exist, load default image
            Glide.with(this)
                .load(R.drawable.ic_person)
                .into(binding.ivProfile)
        }
    }
    
    private fun saveProfile() {
        val fullName = binding.etFullname.text.toString().trim()
        val phoneNumber = binding.etPhone.text.toString().trim()
        
        var imagePath: String? = null
        selectedImageUri?.let { uri ->
            imagePath = saveImageToInternalStorage(uri)
        }
        
        viewModel.updateUserProfile(
            fullName = if (fullName.isNotEmpty()) fullName else null,
            phoneNumber = if (phoneNumber.isNotEmpty()) phoneNumber else null,
            profileImagePath = imagePath ?: viewModel.user.value?.profileImagePath
        )
    }
    
    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val file = File(requireContext().filesDir, fileName)
            val outputStream = FileOutputStream(file)
            
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun logout() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.logout_title))
            .setMessage(getString(R.string.logout_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.logout()
                
                // Navigate to LoginActivity
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}