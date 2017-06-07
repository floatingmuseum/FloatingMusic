package floatingmuseum.floatingmusic.test



/**
 * Created by Floatingmuseum on 2017/6/7.
 */

class TestKT4{
    fun test(){
        val kt3 = TestKT3::class

        fun isOdd(x: Int) = x % 2 != 0
        val numbers = listOf(1, 2, 3)
        println(numbers.filter(::isOdd))
    }
}