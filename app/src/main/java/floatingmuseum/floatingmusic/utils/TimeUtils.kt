package floatingmuseum.floatingmusic.utils

import java.text.SimpleDateFormat

/**
 * Created by Floatingmuseum on 2017/6/9.
 */
fun formatMilliseconds(milliseconds: Long): String {
    if (milliseconds < 1000 * 60 * 60) {
        val sdf = SimpleDateFormat("mm:ss")
        return sdf.format(milliseconds)
    } else {
        val sdf = SimpleDateFormat("HH:mm:ss")
        return sdf.format(milliseconds)
    }
}