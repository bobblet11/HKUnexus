package com.example.hkunexus.ui.homePages.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.hkunexus.databinding.FragmentCreateSlideBinding
import com.example.hkunexus.ui.homePages.myevents.MyEventsListFragment
import com.example.hkunexus.ui.homePages.myevents.MyEventsMapFragment
import com.example.hkunexus.ui.homePages.mygroups.MyGroupsFragment
import com.google.android.material.tabs.TabLayoutMediator

class CreateSlideFragment : Fragment() {

    private var _binding: FragmentCreateSlideBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateSlideBinding.inflate(inflater, container, false)
        val viewPager = binding.createViewPager
        val tabLayout = binding.createTabLayout


        var adapter: MyPagerAdapter2? = MyPagerAdapter2(requireActivity())
        viewPager.adapter = adapter
        adapter?.createFragment(0)
        adapter?.createFragment(1)
        adapter?.createFragment(2)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Create Post"
                1 -> "Create Event"
                2 -> "Create Group"
                else -> null
            }
        }.attach()

        return binding.root
    }
}


class MyPagerAdapter2(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3 // Number of pages
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CreatePostFragment()
            1 -> MyEventsMapFragment()
            2 -> MyGroupsFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}