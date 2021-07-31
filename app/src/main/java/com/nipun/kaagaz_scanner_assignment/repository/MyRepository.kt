package com.nipun.kaagaz_scanner_assignment.repository


import androidx.lifecycle.LiveData
import com.nipun.kaagaz_scanner_assignment.db.model.MyDataDao
import com.nipun.kaagaz_scanner_assignment.db.model.MyDataEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyRepository(private val myDataDao: MyDataDao) {

    fun addNewCustomer(imageEntity: MyDataEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myDataDao.insert(imageEntity)
        }
    }

    fun getCustomerList(): LiveData<List<MyDataEntity>> {
        return myDataDao.getTask()
    }
}