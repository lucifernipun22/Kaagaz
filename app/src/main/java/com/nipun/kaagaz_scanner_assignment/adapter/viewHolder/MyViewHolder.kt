package com.nipun.kaagaz_scanner_assignment.adapter.viewHolder

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.nipun.kaagaz_scanner_assignment.db.convertors.ImageConverter
import com.nipun.kaagaz_scanner_assignment.db.model.MyDataEntity
import com.nipun.kaagaz_scanner_assignment.onClickListener.OnClickOfItem
import kotlinx.android.synthetic.main.item_layout.view.*
import java.io.File

class MyViewHolder(private val view: View, var onClick: OnClickOfItem) :
    RecyclerView.ViewHolder(view) {

    fun setData(dataModelItem: MyDataEntity) {
        val photoFile = File(dataModelItem.image)
        val savedPath = photoFile.absolutePath

        view.apply {
           /* Glide.with(this).load(imageUri).into(ivAvatar)
            itemCard.setOnClickListener {
                onClick.showImage(dataModelItem, adapterPosition)
            }*/

            /*Glide.with(this).asBitmap().load(savedPath).into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    ivAvatar.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })*/
            Glide.with(this).load(savedPath).into(ivAvatar)
            //ivAvatar.load(File(dataModelItem.image))


            itemCard.setOnClickListener {
                onClick.showImage(dataModelItem, adapterPosition)
            }
        }
    }

}


