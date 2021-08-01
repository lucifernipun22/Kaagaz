package com.nipun.kaagaz_scanner_assignment.onClickListener

import com.nipun.kaagaz_scanner_assignment.db.model.MyDataEntity


interface OnClickOfItem {
    fun showImage(dataModelItem: MyDataEntity, position: Int)


}