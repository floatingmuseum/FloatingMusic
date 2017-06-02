package floatingmuseum.floatingmusic.test

/**
 * Created by Floatingmuseum on 2017/6/2.
 */
object TestUtil{
    fun <T>swap(list:ArrayList<T>,index1:Int,index2:Int):ArrayList<T>{
        val temp = list[index1]
        list[index1] = list[index2]
        list[index2] = temp
        return list
    }
}
