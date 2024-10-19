package com.example.hkunexus

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.hkunexus.databinding.FragmentExploreBinding
import com.example.hkunexus.databinding.FragmentMyGroupsBinding
import com.example.hkunexus.ui.homePages.explore.ExploreViewModel

class MyGroupsFragment : Fragment() {

    private var _binding: FragmentMyGroupsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myGroupsViewModel =
            ViewModelProvider(this).get(MyGroupsViewModel::class.java)

        _binding = FragmentMyGroupsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMyGroups
        myGroupsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}