package hu.bme.ttk.lijia8.rgbledcontroller.adapter

import android.annotation.SuppressLint
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
import hu.bme.ttk.lijia8.rgbledcontroller.database.RoomRGB
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.ItemPaletteBinding
import hu.bme.ttk.lijia8.rgbledcontroller.singletons.CurrentRGB

class PaletteRvAdapter : RecyclerView.Adapter<PaletteRvAdapter.ViewHolder>() {

    private lateinit var binding: ItemPaletteBinding

    var listener: OnColorClickedListener? = null

    private var colors : ArrayList<IntArray> =
        arrayListOf(intArrayOf(255,0,0), intArrayOf(0,255,0), intArrayOf(0,0,255), intArrayOf(255,255,255))
    private lateinit var rgbroomlist : List<RoomRGB>

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
        holder.icon.setOnLongClickListener{
            holder.icon.let {
                if (position >= 4)
                    listener?.onColorLongClick(rgbroomlist[position-4])
            }
            true
        }

    }

    override fun getItemCount(): Int = colors.size

    private fun addColor(r:Int, g:Int, b:Int){
        colors.add(intArrayOf(r,g,b))
        reload()
    }


    fun reloadColors(colors: List<RoomRGB>){
        resetColors()

        for (i in colors){
            addColor(i.red,i.green,i.blue)
        }
        rgbroomlist = colors
        reload()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = binding.ivIcon
    }

    private fun resetColors(){
        colors = arrayListOf(intArrayOf(255,0,0), intArrayOf(0,255,0), intArrayOf(0,0,255), intArrayOf(255,255,255))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reload(){
        notifyDataSetChanged()
    }

    interface OnColorClickedListener {
        fun onColorClicked(r:Int,g:Int,b:Int)
        fun onColorLongClick(color: RoomRGB) : Boolean
    }
}