package com.example.shoear

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoear.databinding.FragmentCartBinding
import kotlinx.coroutines.channels.Channel

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var dao: ShoeDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.empty.visibility = View.GONE

        dao = ShoeDatabase.getInstance(requireContext())!!.shoeDao()
        val shoes = dao.getCart() //list of shoes
        val adapter = CartAdapter(shoes)
        binding.cartShoes.layoutManager = LinearLayoutManager(requireContext())
        binding.cartShoes.adapter = adapter

        val channel = Channel<Unit>(Channel.CONFLATED)

        // Send a first item to do the initial load else the list will stay empty forever
        channel.trySend(Unit)
        adapter.onEndOfListReached = {
            channel.trySend(Unit)
        }

        lifecycleScope.launchWhenResumed {
            adapter.onEndOfListReached = null
            channel.close()
        }

        adapter.onItemClicked = { shoe ->
            findNavController().navigate(
                com.example.shoear.CartFragmentDirections.openShoeDetails2(shoe.id)
            )
        }
    }
}