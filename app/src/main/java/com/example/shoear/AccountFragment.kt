package com.example.shoear

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shoear.databinding.FragmentAccountBinding



class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    val args: com.example.shoear.AccountFragmentArgs by navArgs()
    private lateinit var dao: ShoeDao
    private lateinit var builder2 : AlertDialog.Builder

    object  shareFootSize   {
        var userID = 0
    }

    fun startUp() {
        builder2 = AlertDialog.Builder(requireContext())
        //title of the dialogue box
        builder2.setTitle("Notice!")
            //output desired message to the user stating instructions on how to use the AR camera
            //for now, the application works with the left foot only
            .setMessage("Have your foot and ID in the camera view in order to find your foot length in cm. Please orient the ID vertically with the photo above the A&M logo.")
            //setup button for the user to click to continue
            .setPositiveButton("Ok"){dialogInterface, it->
                //move on to the AR camera instance
            }
            //show the dialogue box
            .setCancelable(false)
            .show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = ShoeDatabase.getInstance(requireContext())!!.shoeDao()
        val user = dao.getUserById(args.userId)
        shareFootSize.userID = user.id

        lifecycleScope.launchWhenResumed {
            binding.measureFootBtn.visibility = View.VISIBLE

            binding.account.text = "Account"
            binding.email.text = "Email: " + user.email
            binding.size.text = "Foot Size: " + user.footsize.toString() + "cm"
            binding.username.text = "Username: " + user.username

            configureButton()
        }
    }


    private fun configureButton() {
        dao = ShoeDatabase.getInstance(requireContext())!!.shoeDao()
        val user = dao.getUserById(args.userId)
        binding.measureFootBtn.visibility = View.VISIBLE
        binding.measureFootBtn.text = "Measure Foot"
        binding.measureFootBtn.setOnClickListener {
                findNavController().navigate(
                AccountFragmentDirections.openCamera(user.id)
            )
        }
    }
}
