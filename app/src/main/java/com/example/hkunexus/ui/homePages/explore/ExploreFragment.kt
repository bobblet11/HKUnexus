package com.example.hkunexus.ui.homePages.explore

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.databinding.FragmentExploreBinding
import com.example.hkunexus.ui.login.LoginActivityViewModel

class ExploreFragment : Fragment() {

    private val viewModel: ExploreViewModel by viewModels()
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.setupRecycler(context, binding);

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}