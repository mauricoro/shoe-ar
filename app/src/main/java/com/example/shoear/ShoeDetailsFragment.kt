package com.example.shoear

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import io.github.sceneview.sample.armodelviewer.ARcam
import com.example.shoear.*
import com.example.shoear.databinding.NavigationShoeDetailsBinding
import java.util.*

class ShoeDetailsFragment : Fragment() {

    private lateinit var binding: NavigationShoeDetailsBinding
    val args: com.example.shoear.ShoeDetailsFragmentArgs by navArgs()
    private lateinit var dao: ShoeDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = NavigationShoeDetailsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            binding.cartButton.visibility = View.GONE
            binding.cameraButton.visibility = View.VISIBLE
            binding.cartProgressBar.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            binding.error.visibility = View.GONE

            dao = ShoeDatabase.getInstance(requireContext())!!.shoeDao()

            val shoe = dao.getShoeById(args.shoeId)

            binding.progressBar.visibility = View.GONE

            val random = Random()
            val color: Int = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
            binding.pic.setBackgroundColor(color)

            binding.pic.load(shoe.picture) {
                placeholder(R.drawable.ic_launcher_adaptive_fore)
            }
            binding.shoePrice.text = shoe.price
            binding.shoeName.text = shoe.modelname
            binding.description.text = shoe.story

            configureButton(shoe.inCart)
            configureCamBtn()
        }
    }

    private fun configureCamBtn() {
        binding.cameraButton.visibility = View.VISIBLE
        binding.cameraButton.text = "Try On Virtually"
        dao = ShoeDatabase.getInstance(requireContext())!!.shoeDao()
        //obtaining shoe
        val shoe = dao.getShoeById(args.shoeId)
        val filename = shoe.models
        //obtaining foot size
        val footsizeCM = if (AccountFragment.shareFootSize.userID == 0) {
            "24"
        }else{
            val user = dao.getUserById(AccountFragment.shareFootSize.userID)
            user.footsize.toString()
        }
        //Log.e("TEST", footsizeCM.toString())

        binding.cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                binding.cameraButton.error = "Camera Permissions Disabled"
                binding.cameraButton.text = "Camera Permissions Disabled"
            }
            else {
                binding.cameraButton.error = null
                binding.cameraButton.text = "TRY ON VIRTUALLY"
                val intent = Intent(activity, ARcam::class.java)
                intent.putExtra("EXTRA_FILENAME", filename)
                intent.putExtra("EXTRA_FOOTSIZE", footsizeCM)
                startActivity(intent)
            }
        }
    }

    private fun configureButton(inCart: Boolean) {
        binding.cartButton.visibility = View.VISIBLE
        binding.cartProgressBar.visibility = View.GONE

        binding.cartButton.text = if (inCart) {
            getString(R.string.cancel)
        } else {
            getString(R.string.cart_now)
        }

        binding.cartButton.setOnClickListener {

            binding.cartButton.visibility = View.INVISIBLE
            binding.cartProgressBar.visibility = View.VISIBLE

            lifecycleScope.launchWhenResumed {
                if (inCart) {
                    ShoeDatabase.getInstance(requireContext())!!.shoeDao().rmCart(args.shoeId)
                } else {
                    ShoeDatabase.getInstance(requireContext())!!.shoeDao().addCart(args.shoeId)
                }
                configureButton(!inCart)
            }
        }
    }
}
