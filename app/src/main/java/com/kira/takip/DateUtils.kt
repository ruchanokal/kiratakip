package com.kira.takip

import java.util.Calendar

class DateUtils {
    companion object {
        fun getCurrentMonthAndYear(): Pair<String, Int> {
            val calendar = Calendar.getInstance()
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val monthName = when (month) {
                Calendar.JANUARY -> "Ocak"
                Calendar.FEBRUARY -> "Şubat"
                Calendar.MARCH -> "Mart"
                Calendar.APRIL -> "Nisan"
                Calendar.MAY -> "Mayıs"
                Calendar.JUNE -> "Haziran"
                Calendar.JULY -> "Temmuz"
                Calendar.AUGUST -> "Ağustos"
                Calendar.SEPTEMBER -> "Eylül"
                Calendar.OCTOBER -> "Ekim"
                Calendar.NOVEMBER -> "Kasım"
                Calendar.DECEMBER -> "Aralık"
                else -> "Bilinmeyen Ay"
            }

            return Pair(monthName, year)
        }

        fun getMonthIndex(monthName: String): Int {
            return when (monthName.toLowerCase()) {
                "ocak" -> Calendar.JANUARY
                "şubat" -> Calendar.FEBRUARY
                "mart" -> Calendar.MARCH
                "nisan" -> Calendar.APRIL
                "mayıs" -> Calendar.MAY
                "haziran" -> Calendar.JUNE
                "temmuz" -> Calendar.JULY
                "ağustos" -> Calendar.AUGUST
                "eylül" -> Calendar.SEPTEMBER
                "ekim" -> Calendar.OCTOBER
                "kasım" -> Calendar.NOVEMBER
                "aralık" -> Calendar.DECEMBER
                else -> -1 // Hatalı ay ismi durumu
            }
        }

    }
}
