package com.example.shoear

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shoear.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding
    private lateinit var dao: ShoeDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitProgressBar.visibility = View.GONE
        binding.submit.setOnClickListener {
            val email = binding.email.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailLayout.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }else{
                binding.emailLayout.error = null
            }

            val password = binding.pw.text.toString()

            dao = ShoeDatabase.getInstance(requireContext())!!.shoeDao()
            val user = dao.getUser(email, password)
            if (user == null){
                binding.pwLayout.error = "incorrect email or password"
                return@setOnClickListener
            }else{
                binding.pwLayout.error = null
            }

            binding.submitProgressBar.visibility = View.VISIBLE
            binding.submit.visibility = View.GONE
            lifecycleScope.launchWhenResumed {
                findNavController().navigate(
                    LoginFragmentDirections.openAccount(user.id)
                )
            }
        }
        //takes you to create account page
        binding.create.setOnClickListener{
            findNavController().navigate(
                LoginFragmentDirections.createAccount()
            )
        }
    }
}
