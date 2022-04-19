package home.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import home.fragments.HueColorFragment
import home.fragments.HueLampsFragment
import home.fragments.HueScenesFragment

class HueDetailsTabAdapter(
        activity: FragmentActivity,
        private val isRoom: Boolean
) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return if (isRoom) {
            when (position) {
                0 -> HueColorFragment()
                1 -> HueScenesFragment()
                2 -> HueLampsFragment()
                else -> Fragment()
            }
        } else {
            HueColorFragment()
        }
    }

    override fun getItemCount(): Int = if (isRoom) 3 else 1
}