package com.nipun.kaagaz_scanner_assignment.db.convertors

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream



class ImageConverter {

    @TypeConverter
    fun BitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toString()
    }

    @TypeConverter
    fun StringToBitMap(encodeString : String) : Bitmap? {
        return  BitmapFactory.decodeByteArray(encodeString.toByteArray(),0,encodeString.length)
    }

}

