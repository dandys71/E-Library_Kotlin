package cz.uhk.fim.student.schmid.kotlin.util

import java.util.*

class BookItemUtil {
    companion object{
        fun authorsToString(authors: List<String>, getShortedNames: Boolean): String {
            var res = ""
            for (a in authors) {
                var name = ""
                if(getShortedNames) {
                    val part = a.split(" ")

                    for (i in 0..part.size - 2) {
                        name += part[i][0].toString() + ". "
                    }
                    name += part[part.size - 1]
                }else{
                    name = a
                }

                res += if (a != authors[authors.size - 1]) "$name, " else name
            }
            return res
        }

        fun getDateFromNow(days: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, days)

            return calendar.time
        }

        fun addDaysToDate(date: Date, days: Int): Date{
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DAY_OF_YEAR, days)

            return calendar.time
        }
    }
}