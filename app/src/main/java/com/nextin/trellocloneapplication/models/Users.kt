package com.nextin.trellocloneapplication.models

import android.os.Parcel
import android.os.Parcelable

data class Users (
    val userId : String = "",
    val name : String = "",
    val email : String ="",
    val password : String ="",
    val profileImage : String = "",
    val phoneNo : Long = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()
    ) {
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) =with(dest) {
        writeString(name)
        writeString(email)
        writeString(password)
        writeString(profileImage)
        writeString(userId)
        writeLong(phoneNo)
    }

    companion object CREATOR : Parcelable.Creator<Users> {
        override fun createFromParcel(parcel: Parcel): Users {
            return Users(parcel)
        }

        override fun newArray(size: Int): Array<Users?> {
            return arrayOfNulls(size)
        }
    }
}