package com.example.shoear

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoear.databinding.FragmentShoeBinding

class ShoeListAdapter(private val shoes: List<Shoe>) :
    RecyclerView.Adapter<ShoeListAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentShoeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentShoeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return shoes.size
    }

    var onEndOfListReached: (() -> Unit)? = null
    var onItemClicked: ((Shoe) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shoe = shoes[position]
        holder.binding.price.text = shoe.price ?: ""
        holder.binding.shoeName.text = shoe.modelname
        holder.binding.pic.load(shoe.picture) {
            placeholder(R.drawable.ic_launcher_adaptive_fore)
        }

        if (position == shoes.size - 1) {
            onEndOfListReached?.invoke()
        }

        holder.binding.root.setOnClickListener {
            onItemClicked?.invoke(shoe)
        }
    }
}