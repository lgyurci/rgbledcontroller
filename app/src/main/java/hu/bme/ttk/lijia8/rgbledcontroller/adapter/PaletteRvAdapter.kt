package hu.bme.ttk.lijia8.rgbledcontroller.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.ttk.lijia8.rgbledcontroller.R
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.ItemPaletteBinding
import hu.bme.ttk.lijia8.rgbledcontroller.singletons.CurrentRGB

class PaletteRvAdapter : RecyclerView.Adapter<PaletteRvAdapter.ViewHolder>() {

    private lateinit var binding: ItemPaletteBinding

    var listener: OnColorClickedListener? = null

    private var colors : ArrayList<IntArray> =
        arrayListOf(intArrayOf(255,0,0), intArrayOf(0,255,0), intArrayOf(0,0,255), intArrayOf(255,255,255))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemPaletteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = colors[position]
        holder.icon.setColorFilter(Color.rgb(color[0], color[1], color[2]))
        holder.icon.setOnClickListener{
            holder.icon.let {
                listener?.onColorClicked(color[0],color[1],color[2])
            }
        }

    }

    override fun getItemCount(): Int = colors.size

 /*   fun setApps(apps: List<AppInfo>) {
        applications.clear()
        applications.addAll(0, apps)
        notifyDataSetChanged()
    }*/

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = binding.ivIcon
    }

    interface OnColorClickedListener {
        fun onColorClicked(r:Int,g:Int,b:Int)
    }
}