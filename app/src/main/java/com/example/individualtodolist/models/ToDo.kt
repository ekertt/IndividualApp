package com.example.individualtodolist.models

import android.os.Parcel
import android.os.Parcelable

data class ToDo (
    var title: String="",
    var createdBy: String =""
): Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(createdBy)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ToDo> = object : Parcelable.Creator<ToDo> {
            override fun createFromParcel(source: Parcel): ToDo = ToDo(source)
            override fun newArray(size: Int): Array<ToDo?> = arrayOfNulls(size)
        }
    }
}
