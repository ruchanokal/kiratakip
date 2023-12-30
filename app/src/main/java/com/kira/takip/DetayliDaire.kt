package com.kira.takip

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
class DetayliDaire (val kiraciAdi : String,
                    val telefonNo: String,
                    val tcNo: String,
                    val kiraBedeli : String,
                    val depozitoBedeli : String,
                    val blokAdi : String,
                    val sozlesmeBaslangicTarihi : String,
                    val sozlesmeBitisTarihi : String,
                    val daireNo : String,
                    val siteAdi: String,
                    val odenenAylar: ArrayList<String>? = null) : Parcelable{
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }

}
