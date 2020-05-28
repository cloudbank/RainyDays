package com.droidteahouse.rainydays.ui.home

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.droidteahouse.rainydays.R
import com.droidteahouse.rainydays.RainyDaysApplication
import com.droidteahouse.rainydays.ui.AuthFragment
import com.droidteahouse.rainydays.ui.login.LoginViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeFragment : AuthFragment() {



    @Inject
    lateinit var factory: ViewModelProvider.Factory
    val loginViewModel: LoginViewModel by viewModels(
        ownerProducer = { requireParentFragment() },
        factoryProducer = { factory })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as RainyDaysApplication).appComponent.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)





        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val assetFileName = "rainbowstars.gif"
        updateImageWithImageDecoder(assetFileName, action_image)



        Handler().postDelayed({
            if (!loginViewModel.isLoggedIn()) {
                findNavController().navigate(R.id.action_HomeFragment_to_LoginFragment)
            } else {
                findNavController().navigate(R.id.action_HomeFragment_to_GalleryFragment)
            }
        }, 3500)
    }

    private fun updateImageWithImageDecoder(assetFileName: String, imageView: ImageView) {
        val context = requireContext()
        viewLifecycleOwner.lifecycleScope.launch {
            val d = withContext(Dispatchers.Default) {
                val source = ImageDecoder.createSource(context.assets, assetFileName)
                val drawable = ImageDecoder.decodeDrawable(source)
                return@withContext drawable
            }

            withContext(Dispatchers.Main) {
                imageView.setImageDrawable(d)
                if (d is AnimatedImageDrawable) {
                    d.start()
                    d.repeatCount = AnimatedImageDrawable.REPEAT_INFINITE
                }
            }

        }
    }
}
