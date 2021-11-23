package kz.kakainchik.fourthwalllpicsum.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Picture(val id: Long, val imageUrl: String, val author: String) : Parcelable