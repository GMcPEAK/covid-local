package com.example.covid_local

import android.content.Intent
import android.net.Uri
import android.opengl.ETC1.getHeight
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.w3c.dom.Text


class StoryAdapter(val stories: List<Story>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>(),
    View.OnClickListener {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.title)
        val author: TextView = itemView.findViewById(R.id.author)
        val descriptor: TextView = itemView.findViewById(R.id.description)
        val im: ImageView = itemView.findViewById(R.id.storyImage)
    }

    // The adapter needs to render a new row and needs to know what XML file to use
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Layout inflation: read, parse XML file and return a reference to the root layout
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.story, parent, false)
        return ViewHolder(view)
    }

    //The adapter has a row that's ready to be rendered and needs the content filled in
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentStory = stories[position]
        holder.title.text = currentStory.title
        holder.author.text = currentStory.author
        holder.descriptor.text = currentStory.description
        Picasso.get().load(currentStory.imgUrl).resize(holder.im.measuredWidth, 450).centerCrop().into(holder.im)
    }

    //Return the total number of rows you expect your list to have
    override fun getItemCount(): Int {
        return stories.size
    }

    override fun onClick(v: View?) {

    }
}