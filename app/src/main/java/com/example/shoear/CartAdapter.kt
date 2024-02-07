package com.example.shoear

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.shoear.*
import com.example.shoear.databinding.FragmentCartAdapterBinding

class CartAdapter(private val carts: List<Shoe>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentCartAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentCartAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    var onEndOfListReached: (() -> Unit)? = null
    var onItemClicked: ((Shoe) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = carts[position]
        holder.binding.price.text = cart.price ?: ""
        holder.binding.shoeName.text = cart.modelname
        holder.binding.pic.load(cart.picture) {
            placeholder(R.drawable.ic_placeholder)
        }

        if (position == carts.size - 1) {
            onEndOfListReached?.invoke()
        }

        holder.binding.root.setOnClickListener {
            onItemClicked?.invoke(cart)
        }
    }
}