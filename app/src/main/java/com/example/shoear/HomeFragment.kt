package com.example.shoear


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoear.databinding.FragmentHomeBinding
import kotlinx.coroutines.channels.Channel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dao: ShoeDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = ShoeDatabase.getInstance(requireContext())!!.shoeDao()

        val shoes = dao.getShoes() //list of shoes
        val adapter = ShoeListAdapter(shoes) //calls adapter function ShoeListAdapter.kt for vertical scroll
        val adapt = ScrollListAdapter(shoes) //calls adapter function ScrollListAdapter.kt for horizontal card scroll

        val scrollManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val listManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.shoes.layoutManager = listManager
        binding.scroll.layoutManager = scrollManager
        binding.shoes.adapter = adapter
        binding.scroll.adapter = adapt

        val channel = Channel<Unit>(Channel.CONFLATED)

        // Send a first item to do the initial load else the list will stay empty forever
        channel.trySend(Unit)
        adapter.onEndOfListReached = {
            channel.trySend(Unit)
        }

        adapter.onItemClicked = { shoe ->
            findNavController().navigate(
                com.example.shoear.HomeFragmentDirections.openShoeDetails(shoe.id)
            )
        }
        adapt.onItemClicked = { shoe ->
            findNavController().navigate(
                com.example.shoear.HomeFragmentDirections.openShoeDetails(shoe.id)
            )
        }
    }
}