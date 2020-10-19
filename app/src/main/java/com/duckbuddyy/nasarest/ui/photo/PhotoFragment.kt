package com.duckbuddyy.nasarest.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.duckbuddyy.nasarest.R
import com.duckbuddyy.nasarest.data.Photo
import com.duckbuddyy.nasarest.databinding.FragmentPhotoBinding
import com.duckbuddyy.nasarest.domain.CameraType
import com.duckbuddyy.nasarest.domain.RoverType
import dagger.hilt.android.AndroidEntryPoint

const val SP_NAME = "Preferences"

@AndroidEntryPoint
class PhotoFragment(private val roverType: RoverType) : Fragment(R.layout.fragment_photo), NasaPhotoAdapter.OnItemClickListener {
    private val viewModel by viewModels<PhotoViewModel>()
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    lateinit var preferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = requireActivity().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        _binding = FragmentPhotoBinding.bind(view)
        val adapter = NasaPhotoAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = NasaPhotoLoadStateAdapter { adapter.retry() },
                footer = NasaPhotoLoadStateAdapter { adapter.retry() },
            )
            buttonRetry.setOnClickListener { adapter.retry() }
        }

        val cameraType = preferences.getString(
            getString(R.string.camera_filter),
            getString(R.string.camera_all)
        )
        val newQuery = viewModel.currentQuery.value!!.copy(cameraType = CameraType.valueOf(cameraType!!),rover = roverType)
        viewModel.searchPhotos(newQuery.sol,newQuery.cameraType,newQuery.rover)

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewRetry.isVisible = loadState.source.refresh is LoadState.Error

                //If Api Returns Empty
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> showFilterDialog()
            else -> println("HELLOX2")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFilterDialog() {
        val filterDialog = MaterialDialog(windowContext = requireContext())
            .noAutoDismiss()
            .customView(R.layout.dialog_filter)

        //Setting Initial Preferences
        val filter = preferences.getString(
            getString(R.string.camera_filter),
            getString(R.string.camera_all)
        )

        //Checking Initial RadioButton
        val radioGroup =filterDialog.findViewById<RadioGroup>(R.id.radioGroup)
        for(radioButton in radioGroup.children){
            if(radioButton is RadioButton) {
                if(radioButton.text.toString() == filter){
                    radioButton.isChecked = true
                    break
                }
            }
        }

        filterDialog.findViewById<Button>(R.id.filterButton).setOnClickListener {
            val selectedItem = radioGroup.checkedRadioButtonId
            val selectedRadioButton = filterDialog.findViewById<RadioButton>(selectedItem)
            val selectedCamera = selectedRadioButton.text.toString()
            val cameraType = CameraType.valueOf(selectedCamera)
            preferences.edit().putString(getString(R.string.camera_filter),selectedCamera).apply()

            val newQuery = viewModel.currentQuery.value!!.copy(cameraType = cameraType)
            viewModel.searchPhotos(newQuery.sol,newQuery.cameraType,newQuery.rover)
            binding.recyclerView.scrollToPosition(0)
            filterDialog.dismiss()
        }
        filterDialog.show()
    }

    override fun onItemClick(photo: Photo) {
        val detailDialog = MaterialDialog(windowContext = requireContext())
            .noAutoDismiss()
            .customView(R.layout.dialog_detail)

        val imageView = detailDialog.findViewById<ImageView>(R.id.image_view)
        detailDialog.findViewById<TextView>(R.id.text_view_earth_date).text = photo.earthDate
        detailDialog.findViewById<TextView>(R.id.text_view_rover_name).text = photo.rover.name
        detailDialog.findViewById<TextView>(R.id.text_view_rover_status).text = photo.rover.status
        detailDialog.findViewById<TextView>(R.id.text_view_rover_launch_date).text = photo.rover.launchDate
        detailDialog.findViewById<TextView>(R.id.text_view_rover_landing_date).text = photo.rover.landingDate

        Glide.with(requireContext())
            .load(photo.imgSrc)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_error)
            //.apply(RequestOptions().transform(RoundedCorners(100)))
            .into(imageView)

        detailDialog.show()
    }

}