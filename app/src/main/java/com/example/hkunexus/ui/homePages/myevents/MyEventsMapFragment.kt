package com.example.hkunexus.ui.homePages.myevents

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.hkunexus.BuildConfig
import com.example.hkunexus.R
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.databinding.FragmentMyEventsMapBinding
import kotlinx.coroutines.launch
import java.net.URLEncoder

class MyEventsMapFragment : Fragment() {
    private var _binding: FragmentMyEventsMapBinding? = null
    private val binding get() = _binding!!

    private val apiKey = BuildConfig.API_KEY // replace with your API key

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsMapBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            loadStaticMap()
        }

        val swipeRefreshLayout = binding.refreshLayout

        swipeRefreshLayout.setOnRefreshListener{

            lifecycleScope.launch {
                loadStaticMap()
            }
            swipeRefreshLayout.isRefreshing = false

        }

        return binding.root
    }

    private suspend fun loadStaticMap() {
        val listOfJoinedEvents = SupabaseSingleton.getAllJoinedEventsFromRecentAsync()

        val centerLatLng = "22.3193,114.1694"
        val zoom = 11
        val size = "2048x2048"
        val mapType = "roadmap"
        val scale = 2

        val markers = mutableListOf<String>()
        for (i in listOfJoinedEvents.indices) {
            val event = listOfJoinedEvents[i]
            val eventLatLng = "${event.coordinates}"
            val marker = "color:blue|size:medium|label:${i + 1}|$eventLatLng"
            markers.add(marker)
        }

        val markersString = markers.joinToString("|&markers=") { URLEncoder.encode(it, "UTF-8") }
        val url = "https://maps.googleapis.com/maps/api/staticmap?center=$centerLatLng&zoom=$zoom&size=$size&maptype=$mapType&scale=$scale&markers=$markersString&key=$apiKey"

        // load the image using Glide into the TouchImageView
        Glide.with(binding.bigEventMapView.context)
            .asBitmap()
            .load(url)
            .error(R.drawable.placeholder_view_vector) // placeholder in case of error
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}