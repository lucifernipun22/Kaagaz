package com.nipun.kaagaz_scanner_assignment.db.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nipun.kaagaz_scanner_assignment.db.model.MyDataEntity
import com.nipun.kaagaz_scanner_assignment.repository.MyRepository

class MyViewModel(val repository: MyRepository) : ViewModel() {

    fun addImage(customerEntity: MyDataEntity) {
        repository.addNewCustomer(customerEntity)
    }

    fun getImage(): LiveData<List<MyDataEntity>> {
        return repository.getCustomerList()
    }
}