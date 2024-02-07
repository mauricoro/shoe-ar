package com.example.shoear

//import Shoe
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoear.databinding.ShoeScrollBinding
import java.util.*


class ScrollListAdapter(private val shoes: List<Shoe>) :
    RecyclerView.Adapter<ScrollListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ShoeScrollBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ShoeScrollBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

        val random = Random()
        val color: Int = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        holder.binding.background.background.setTint(color)


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