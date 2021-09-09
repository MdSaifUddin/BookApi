package com.learning.bookapi.Data

class Book(title:String?,description:String?,imgSrc:String?) {
    var v1:String?=null
    var v2:String?=null
    var v3:String?=null

    init{
        v1=title
        v2=description
        v3=imgSrc
    }

    fun getTitle():String{
        return v1!!
    }
    fun getDescription():String{
        return v2!!
    }
    fun getImgSrc():String{
        return v3!!
    }
}