package com.example.cocktailbook.ui.user

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cocktailbook.R
import com.example.cocktailbook.databinding.FragmentCreateRecipeBinding
import com.example.cocktailbook.model.Drink
import com.example.cocktailbook.ui.adapter.AddIngredientsAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class CreateRecipeFragment : Fragment(R.layout.fragment_create_recipe) {

    private val RESULT_LOAD_IMG: Int = 2
    private val REQUEST_IMAGE_CAPTURE = 1
    private var _binding: FragmentCreateRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : AddIngredientsAdapter
    private lateinit var createRecipeViewModel: CreateRecipeViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var imagesRef: StorageReference
    private lateinit var dbRef: DatabaseReference
    private val newDrink = Drink()
    lateinit var currentPhotoPath: String
    private lateinit var photoURI: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRecipeViewModel = ViewModelProvider(requireActivity()).get(CreateRecipeViewModel::class.java)
        userViewModel = ViewModelProvider(this, UserViewModelFactory()).get(UserViewModel::class.java)
        _binding = FragmentCreateRecipeBinding.bind(view)
        adapter = AddIngredientsAdapter(mutableListOf(""), mutableListOf(""), requireActivity())
        binding.ingredient.adapter = adapter
        val storageRef = Firebase.storage.reference


        binding.drinkImageDet.setOnClickListener {
            if (!binding.drinkName.text.isNullOrEmpty()){
                imagesRef = storageRef
                    .child("${Firebase.auth.currentUser.email}/${binding.drinkName.text}")
                photoURI = Uri.EMPTY
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
            }else
                Toast.makeText(requireContext(),
                    "Please enter the name of cocktail", Toast.LENGTH_LONG).show()
        }
        binding.drinkImageDet.setOnLongClickListener {
            if (!binding.drinkName.text.isNullOrEmpty()){
                imagesRef = storageRef
                    .child("${Firebase.auth.currentUser.email}/${binding.drinkName.text}")
                dispatchTakePictureIntent()
            }
            else
                Toast.makeText(requireContext(),
                    "Please enter the name of cocktail", Toast.LENGTH_LONG).show()
            return@setOnLongClickListener true
        }


        dbRef = Firebase.database.getReference("${Firebase.auth.uid}/Owns")
        binding.saveRecipe.setOnClickListener {
            when {
                binding.drinkName.text.isNullOrEmpty() ->
                    Toast.makeText(requireContext(), "Enter name", Toast.LENGTH_LONG).show()
                binding.drinkAlcocholic.text.isNullOrEmpty() ->
                    Toast.makeText(requireContext(), "Enter Alcoholic", Toast.LENGTH_LONG).show()
                binding.drinkGlass.text.isNullOrEmpty() ->
                    Toast.makeText(requireContext(), "Enter Glass", Toast.LENGTH_LONG).show()
                adapter.ingredients.size < 2 ->
                    Toast.makeText(requireContext(), "Enter at least 1 ingredient",
                        Toast.LENGTH_LONG).show()
                binding.drinkInstruction.text.isNullOrEmpty() ->
                    Toast.makeText(requireContext(), "Enter Instruction", Toast.LENGTH_LONG).show()
                binding.drinkImageDet.drawable == null ->
                    Toast.makeText(requireContext(), "Upload picture", Toast.LENGTH_LONG).show()
                else ->{
                    createNewRecipe(dbRef)
                    findNavController().popBackStack()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        lifecycleScope.launch(Dispatchers.IO) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val imageUri: Uri = if(requestCode == RESULT_LOAD_IMG)
                        data!!.data!!
                    else
                        photoURI

                    launch(Dispatchers.Main) {
                        binding.drinkImageDet.load(imageUri) {
                            transformations(RoundedCornersTransformation(25f))
                        }
                        binding.pickUpPic.visibility = View.GONE
                    }
                    val selectedImage = MediaStore.Images.Media.getBitmap(
                        requireActivity().applicationContext.contentResolver, imageUri)
                    File(photoURI.path).delete()   //delete temp photo from camera
                    val stream = ByteArrayOutputStream()
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                    val image = stream.toByteArray()
                    val uploadTask = imagesRef.putBytes(image)
                    uploadTask.addOnFailureListener {
                        launch(Dispatchers.Main) {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }
            }else {
                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Refuse", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createNewRecipe(reference: DatabaseReference){
        imagesRef.downloadUrl.addOnCompleteListener {
            newDrink.idDrink = "Own${binding.drinkName.text}"
            newDrink.strDrink = binding.drinkName.text.toString()
            newDrink.strAlcoholic = binding.drinkAlcocholic.text.toString()
            newDrink.strGlass = binding.drinkGlass.text.toString()
            newDrink.ingredients = adapter.ingredients as ArrayList<String>
            newDrink.measure = adapter.measures as ArrayList<String>
            newDrink.strInstructions = binding.drinkInstruction.text.toString()
            newDrink.strDrinkThumb = it.result.toString()
            reference.child(newDrink.strDrink).setValue(newDrink)
        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        requireActivity(),
                        "com.example.cocktailbook.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                findNavController().popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}