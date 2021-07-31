package com.nipun.kaagaz_scanner_assignment.adapter.viewHolder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.nipun.kaagaz_scanner_assignment.db.model.MyDataEntity
import com.nipun.kaagaz_scanner_assignment.onClickListener.OnClickOfItem
import kotlinx.android.synthetic.main.item_layout.view.*
import java.io.File

class MyViewHolder(private val view: View, var onClick: OnClickOfItem) :
    RecyclerView.ViewHolder(view) {
    fun setData(dataModelItem: MyDataEntity) {
        val file = File(dataModelItem.image)
        val imageUri = Uri.fromFile(file)
        view.apply {
            /*Glide.with(this).load(imageUri).into(ivAvatar)
            itemCard.setOnClickListener {
                onClick.showImage(dataModelItem, adapterPosition)
            }*/

            Glide.with(this).asBitmap().load(imageUri).into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    ivAvatar.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
            itemCard.setOnClickListener {
                onClick.showImage(dataModelItem, adapterPosition)
            }
        }
    }

}