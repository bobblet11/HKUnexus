package com.example.hkunexus.ui.homePages.myevents

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hkunexus.databinding.FragmentMyEventsMapBinding

class MyEventsMapFragment : Fragment() {
    private var _binding: FragmentMyEventsMapBinding? = null
    private val viewModel: MyEventsViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}