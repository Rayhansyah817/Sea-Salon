package com.compfest.seasalon.ui.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.compfest.seasalon.R
import com.compfest.seasalon.data.ButtonTime

class ListTimeAdapter(private val listTime: List<ButtonTime>) : RecyclerView.Adapter<ListTimeAdapter.ListViewHolder>() {

    private var selectedPosition: Int = -1



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_button_time, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTime.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val btnTime = listTime[position]
        holder.bind(btnTime, position)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnTime: TextView = itemView.findViewById(R.id.btnTime)
        val statusText: TextView = itemView.findViewById(R.id.statusText)

        fun bind(btnTime: ButtonTime, position: Int) {
            this.btnTime.text = btnTime.time
            this.btnTime.isEnabled = btnTime.isActive
            this.btnTime.setBackgroundColor(ContextCompat.getColor(itemView.context, btnTime.color))

            statusText.text = if (btnTime.isActive) "Available" else "Not Available"
            statusText.textSize = if (btnTime.isActive) 14f else 12f

            if (btnTime.isSelected) {
                // Atur drawableEnd (icon ceklis) jika tombol dipilih
                this.btnTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_check)
            } else {
                // Hapus drawableEnd (icon ceklis) jika tombol tidak dipilih
                this.btnTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }

            if (!btnTime.isActive) {
                this.btnTime.setOnClickListener(null)
            } else {
                this.btnTime.setOnClickListener {
                    if (btnTime.isSelected) {
                        btnTime.isSelected = false
                        selectedPosition = -1
                    } else {
                        if (selectedPosition != -1) {
                            listTime[selectedPosition].isSelected = false
                            notifyItemChanged(selectedPosition)
                        }
                        btnTime.isSelected = true
                        selectedPosition = adapterPosition
                    }
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }

    // Method to get selected time
    fun getSelectedTime(): ButtonTime? {
        return if (selectedPosition != -1) listTime[selectedPosition] else null
    }
}
