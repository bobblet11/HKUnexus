import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.hkunexus.databinding.FragmentMyEventsSlideBinding
import com.example.hkunexus.ui.homePages.home.HomeViewModel
import com.example.hkunexus.ui.homePages.myevents.MyEventsListFragment
import com.example.hkunexus.ui.homePages.myevents.MyEventsMapFragment
import com.example.hkunexus.ui.homePages.myevents.MyEventsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MyEventsSlide : Fragment() {
    private var _binding: FragmentMyEventsSlideBinding? = null
    private val binding get() = _binding!!
    private val myEventsListViewModel: MyEventsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsSlideBinding.inflate(inflater, container, false)
        val viewPager = binding.myEventsViewPager as ViewPager2
        val tabLayout = binding.myEventsTabLayout


        var adapter: MyPagerAdapter? = MyPagerAdapter(requireActivity())
        viewPager.adapter = adapter
        adapter?.createFragment(0)
        adapter?.createFragment(1)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Events List"
                1 -> "Events Map"
                else -> null
            }
        }.attach()




        return binding.root
    }

}

class MyPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2 // Number of pages
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyEventsListFragment()
            1 -> MyEventsMapFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}