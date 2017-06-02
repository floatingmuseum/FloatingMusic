package floatingmuseum.floatingmusic

import android.content.Context


/**
 * Created by Floatingmuseum on 2017/6/2.
 */
fun Context.getResString(resID:Int):String{
    return this.getString(resID)
}