package com.example.dailyjournal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyjournal.R

class RvAdapter(private val entries: List<Entry>, private val click:(Entry)->Unit) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.journal_item,parent,false)

        return ViewHolder(listItem)

    }

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(entries[position],click)

    }


}

class ViewHolder( val view: View):RecyclerView.ViewHolder(view){


    fun bind(entry: Entry, list:(Entry)->Unit){

        val tV = view.findViewById<TextView>(R.id.tvName)
        tV.text= entry.title

        val date = view.findViewById<TextView>(R.id.tvDate)
        date.text= entry.date

        view.setOnClickListener{
            list(entry)

        }
    }


}
