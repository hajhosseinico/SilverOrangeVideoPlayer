package com.silverorange.videoplayer.view.fragmentfactory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.silverorange.videoplayer.view.videodetail.VideoDetailFragment
import javax.inject.Inject

/**
 * MainFragmentFactory
 */

class MainFragmentFactory
@Inject
constructor() : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        // Using factory design pattern to create my fragment and pass them to the navigation component
        return when (className) {
            VideoDetailFragment::class.java.name -> {
                VideoDetailFragment()
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}
