package com.learning.bookapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.learning.bookapi.Data.Book
import pl.droidsonroids.gif.GifImageView

class MainActivity : AppCompatActivity() {
    var bookList:ArrayList<Book> = ArrayList()
    var resultShown=0
    var adapter:BookAdapter?=null
    lateinit var recyclerView: RecyclerView
    var loadmore=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView=findViewById(R.id.recyclerView)
        recyclerView.layoutManager=LinearLayoutManager(this)
        callApi("https://www.googleapis.com/books/v1/volumes?q=flowers&startIndex=0&maxResults=10")

        loadMore()

    }

    fun loadMore(){
        findViewById<Button>(R.id.loadMore).setOnClickListener{
            it.visibility=View.GONE
            callApi("https://www.googleapis.com/books/v1/volumes?q=flowers&startIndex=$resultShown&maxResults=10")
        }
    }

    fun callApi(url:String){
        findViewById<GifImageView>(R.id.loading).visibility= View.VISIBLE
        var queue= Volley.newRequestQueue(this)
        var request= JsonObjectRequest(Request.Method.GET,url,null, Response.Listener {
            Log.i("Success : ",""+it.getJSONArray("items").length())
            var items=it.getString("totalItems").toInt()
            findViewById<GifImageView>(R.id.loading).visibility= View.GONE
            for(i in 0 until 10){
                var descBoolean=it.getJSONArray("items").getJSONObject(i).getJSONObject("volumeInfo").has("description")
                var desc=if(descBoolean) it.getJSONArray("items").getJSONObject(i).getJSONObject("volumeInfo").getString("description") else ""
                var book=Book(
                    it.getJSONArray("items").getJSONObject(i).getJSONObject("volumeInfo").getString("title"),
                    desc,
                    it.getJSONArray("items").getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail"))
                bookList.add(book)
            }
            resultShown+=it.getJSONArray("items").length()
            Log.i("After For : ",""+bookList.size)
            if(adapter==null){
                adapter=BookAdapter(bookList,this@MainActivity)
                recyclerView.adapter=adapter
            }else{
                adapter?.notifyDataSetChanged()
            }

            loadmore=resultShown<items
            if(loadmore)
                findViewById<Button>(R.id.loadMore).visibility=View.VISIBLE
            else
                findViewById<Button>(R.id.loadMore).visibility=View.GONE

            recyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    var layoutManager=recyclerView.layoutManager as LinearLayoutManager
                    var totalItemCount = layoutManager?.getItemCount();
                    var lastVisible = layoutManager?.findLastCompletelyVisibleItemPosition()

                    var endHasBeenReached = lastVisible + 5 >= totalItemCount
                    if (totalItemCount != null) {
                        if (totalItemCount > 0 && endHasBeenReached) {
                            findViewById<Button>(R.id.loadMore).visibility=View.VISIBLE
                        }else{
                            findViewById<Button>(R.id.loadMore).visibility=View.GONE
                        }
                    }
                }
            })
        },Response.ErrorListener {
            findViewById<GifImageView>(R.id.loading).visibility= View.GONE
            Toast.makeText(applicationContext,"Check your Internet connection!",Toast.LENGTH_SHORT).show()
        })
        queue.add(request)
    }
}