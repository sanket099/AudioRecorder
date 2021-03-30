package com.sanket.audiorecorder


import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text


class AudioFileAdapter(context: Context, audioArrayList: ArrayList<AudioFileClass>) : RecyclerView.Adapter<AudioFileAdapter.viewHolder>() {
    private var context: Context
    private var audioArrayList: ArrayList<AudioFileClass>
    var onItemClickListener: OnItemClickListener? = null

    init {
        this.context = context
        this.audioArrayList = audioArrayList
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): viewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.audio_files_list, viewGroup, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, i: Int) {
        holder.title.text = audioArrayList[i].getTitle()
        holder.date.text = audioArrayList[i].getDate()
        holder.duration.text = audioArrayList[i].getDuration()
        holder.storage.text = audioArrayList[i].getStorage()
    }

    override fun getItemCount(): Int {
        return audioArrayList.size
    }

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var date: TextView
        var duration: TextView
        var storage : TextView

        init {
            title = itemView.findViewById(R.id.audio_title)
            date = itemView.findViewById(R.id.audio_date)
            duration = itemView.findViewById(R.id.audio_duration)
            storage = itemView.findViewById(R.id.audio_size)
            itemView.setOnClickListener { v -> onItemClickListener!!.onItemClick(getItemId(adapterPosition),v) }
        }
    }


    fun setOnItemClick(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(itemId: Long, v: View? )
    }


}