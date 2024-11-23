package com.example.hkunexus.ui.homePages.myevents

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.hkunexus.BuildConfig
import com.example.hkunexus.R
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.databinding.FragmentMyEventsMapBinding

class MyEventsMapFragment : Fragment() {
    private var _binding: FragmentMyEventsMapBinding? = null
    private val binding get() = _binding!!

    private val apiKey = BuildConfig.API_KEY // Replace with your API key

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsMapBinding.inflate(inflater, container, false)

        // Load the dynamic map
        loadStaticMap()

        return binding.root
    }

    private fun loadStaticMap() {
        val listOfJoinedEvents = SupabaseSingleton.getAllJoinedEvents()

        val centerLatLng = "22.3193,114.1694"
        val zoom = 11
        val size = "2160x2160"
        val mapType = "roadmap"
        val scale = 2


        val markers = mutableListOf<String>()

        for ((index, event) in listOfJoinedEvents.withIndex()) {
            val eventLatLng = "${event.location}"
            val marker = "color:blue|label:${index + 1}|$eventLatLng"
            markers.add(marker)
        }


        val markersString = markers.joinToString("|")
        val url =
            "https://maps.googleapis.com/maps/api/staticmap?center=$centerLatLng&zoom=$zoom&size=$size&maptype=$mapType&scale=$scale&markers=$markersString&key=$apiKey"

        Log.d("EventListAdapter", "Loading map from URL: $url")

        // Load the image using Glide into the ImageView, not the MapView
        Glide.with(binding.bigEventMapView.context)
            .asBitmap()
            .load(url)
            .error(R.drawable.placeholder_view_vector) // Placeholder in case of error
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.bigEventMapView.setImageBitmap(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    binding.bigEventMapView.setImageDrawable(errorDrawable)
                }
            })
    }
}