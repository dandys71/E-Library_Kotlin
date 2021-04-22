package cz.uhk.fim.student.schmid.kotlin.model

import cz.uhk.fim.student.schmid.kotlin.util.BookItemUtil

abstract class BookItemBase(open val id: Int, open val title: String, open val authors: List<String>, open val description: String, open val imgUrl: String):BookItemInterface{

    override fun authorsToString() = BookItemUtil.authorsToString(authors, true)
}