package com.example.individualtodolist.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "toDo")
data class ToDo(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "date")
    var date: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(date)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ToDo> = object : Parcelable.Creator<ToDo> {
            override fun createFromParcel(source: Parcel): ToDo = ToDo(source)
            override fun newArray(size: Int): Array<ToDo?> = arrayOfNulls(size)
        }
    }
}
