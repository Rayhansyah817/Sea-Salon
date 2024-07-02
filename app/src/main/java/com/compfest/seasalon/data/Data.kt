package com.compfest.seasalon.data

import com.compfest.seasalon.R

data class Salon(
    val name: String,
    val description: String,
    val photo: String,
    val logo: String
)

data class Services(
    val photo:Int,
    val services: String

)

data class Artists(
    val name: String,
    val experience: String,
    val rating: String,
    val profile: Int
)

data class ButtonTime(
    val time: String,
    var isActive: Boolean = true,
    var color: Int = R.color.active_button_color, // Default warna untuk tombol aktif
    var isSelected: Boolean = false // Status apakah tombol dipilih atau tidak
)

data class Booking(
    val service: String = "",
    val artist: String = "",
    val date: String = "",
    val time: String = "",
    val userId: String = "",
    val uniqueCode: String? = null,
    var isApproved: Boolean = false
)

data class Approved(
    val userId: String = "",
    val service: String = "",
    val artist: String = "",
    val date: String = "",
    val time: String = "",
    val uniqueCode: String = ""
)