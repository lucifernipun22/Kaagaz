package com.nipun.kaagaz_scanner_assignment.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nipun.kaagaz_scanner_assignment.R
import com.nipun.kaagaz_scanner_assignment.adapter.MyAdapter
import com.nipun.kaagaz_scanner_assignment.db.model.MyDataDatabase
import com.nipun.kaagaz_scanner_assignment.db.model.MyDataEntity
import com.nipun.kaagaz_scanner_assignment.onClickListener.OnClickOfItem
import com.nipun.kaagaz_scanner_assignment.repository.MyRepository
import com.nipun.kaagaz_scanner_assignment.db.viewModel.MyViewModel
import com.nipun.kaagaz_scanner_assignment.db.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_image_recycler_view.*


class ImageRecyclerViewActivity : AppCompatActivity(), OnClickOfItem {
    private lateinit var myAdapter: MyAdapter
    val dataModelList = mutableListOf<MyDataEntity>()
    private  var myViewModel: MyViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_recycler_view)

        setRecyclerAdapter()
        val MyDataDao by lazy {
            val roomDatabase = MyDataDatabase.getDatabase(this)
            roomDatabase.getMyDao()
        }
        val repository by lazy {
            MyRepository(MyDataDao)
        }
        val factory = ViewModelFactory(repository)
        myViewModel = ViewModelProvider(this, factory).get(MyViewModel::class.java)

        observeLiveData()

    }

    private fun observeLiveData() {

        myViewModel?.getImage()?.observe(this, androidx.lifecycle.Observer {
            dataModelList.clear()
            dataModelList.addAll(it)
            myAdapter.updateList(dataModelList)
        })

    }

    private fun setRecyclerAdapter() {
        myAdapter = MyAdapter(dataModelList, this)
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.apply {
            this.layoutManager = layoutManager
            adapter = myAdapter
        }
    }


    override fun showImage(dataModelItem: MyDataEntity, position: Int) {
        val intent = Intent(this,ImageViewActivity::class.java)
        intent.putExtra("image",dataModelItem.image)
        startActivity(intent)
    }
}