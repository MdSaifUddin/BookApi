package com.learning.bookapi

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.learning.bookapi.Data.Book

class BookAdapter(bookList:ArrayList<Book>,c: Context): RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    var list=bookList
    var context=c
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.book,parent,false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.title.text=list.get(position).getTitle()
        holder.description.text=list.get(position).getDescription()
        Glide.with(context)
            .load(list.get(position).getImgSrc().replace("http","https"))
            .into(holder.image)
        Log.i("img",list.get(position).getImgSrc())
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class BookViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var image=item.findViewById<ImageView>(R.id.imageView)
        var title=item.findViewById<TextView>(R.id.title)
        var description=item.findViewById<TextView>(R.id.description)
    }
}