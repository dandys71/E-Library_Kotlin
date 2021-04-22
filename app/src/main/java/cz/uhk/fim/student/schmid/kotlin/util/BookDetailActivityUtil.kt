package cz.uhk.fim.student.schmid.kotlin.util

import android.content.Context
import cz.uhk.fim.student.schmid.R

class BookDetailActivityUtil {
    companion object{
        /**
         * Vrací vyskloňované slovo pro "kusy" v závislosti na počtu kusů (pouze estetické využití)
         **/
        fun getPiecesString(c: Context, num: Int): String {
            var str = when (num) {
                1 -> c.resources.getText(R.string.one_piece)
                in 2..4 -> c.resources.getText(R.string.two_to_four_pieces)
                else -> c.resources.getText(R.string.five_and_more_pieces)
            }
            return str.toString()
        }

        /**
         * Vrací vyskloňované slovo pro "zbývá" v závislosti na počtu kusů (pouze estetické využití)
         **/
        fun getPiecesLeftString(c: Context, num: Int): String {
            var str = when (num) {
                1 -> c.resources.getText(R.string.one_piece_left)
                in 2..4 -> c.resources.getText(R.string.two_to_four_pieces_left)
                else -> c.resources.getText(R.string.five_and_more_pieces_left)
            }
            return str.toString()
        }
    }
}