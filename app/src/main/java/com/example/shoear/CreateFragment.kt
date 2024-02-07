package com.example.shoear

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shoear.databinding.CreateFragmentBinding
import com.example.shoear.databinding.LoginFragmentBinding

class CreateFragment : Fragment() {
    private lateinit var binding: CreateFragmentBinding
    private lateinit var dao: ShoeDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitProgressBar.visibility = View.GONE
        binding.submit.setOnClickListener {

            val username = binding.name.text.toString()
            if (username.isEmpty()){
                binding.nameLayout.error = "Please enter a Username"
                return@setOnClickListener
            }else{
                binding.nameLayout.error = null
            }

            val email = binding.email.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailLayout.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }else{
                binding.emailLayout.error = null
            }

            val password = binding.pw.text.toString()
            if (password.isEmpty()){
                binding.pwLayout.error = "Please enter a Password"
                return@setOnClickListener
            }
            else{
                binding.pwLayout.error = null
            }

            val uid = (0..1000000).random()

            binding.submitProgressBar.visibility = View.VISIBLE
            binding.submit.visibility = View.GONE
            lifecycleScope.launchWhenResumed {

                ShoeDatabase.getInstance(requireContext())!!.shoeDao().insertUser(Users(username, uid, password, email, 0))

                findNavController().navigate(
                    CreateFragmentDirections.openLogin2()
                )
            }
        }
    }
}
